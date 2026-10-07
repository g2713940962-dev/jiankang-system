package com.jiankang.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiankang.common.constant.RoleConstant;
import com.jiankang.entity.CheckRecord;
import com.jiankang.entity.ExerciseGoal;
import com.jiankang.entity.Notice;
import com.jiankang.entity.User;
import com.jiankang.mapper.CheckRecordMapper;
import com.jiankang.mapper.ExerciseGoalMapper;
import com.jiankang.mapper.NoticeMapper;
import com.jiankang.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * 定时任务：异常提醒（创新功能）
 * <p>
 * 1. 每日 20:00 扫描当日未打卡用户，推送提醒
 * 2. 每周日 20:00 检查本周目标达成情况，未达标推送提醒
 */
@Slf4j
@Component
public class ReminderTask {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CheckRecordMapper checkRecordMapper;

    @Resource
    private ExerciseGoalMapper goalMapper;

    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 未打卡提醒
     */
    @Scheduled(cron = "${reminder.check-cron}")
    public void remindNotChecked() {
        log.info("开始执行【未打卡提醒】定时任务");
        LocalDate today = LocalDate.now();
        List<User> users = userMapper.selectList(new QueryWrapper<User>()
                .eq("role", RoleConstant.USER).eq("status", 1));
        for (User user : users) {
            Long count = checkRecordMapper.selectCount(new QueryWrapper<CheckRecord>()
                    .eq("user_id", user.getId()).eq("check_date", today));
            if (count == null || count == 0) {
                insertNotice(user.getId(), "今日尚未打卡",
                        "你还没有完成今天的运动打卡，记得抽出时间锻炼身体哦！", "REMIND");
            }
        }
        log.info("【未打卡提醒】执行完毕，共扫描 {} 个用户", users.size());
    }

    /**
     * 周目标未达标提醒
     */
    @Scheduled(cron = "${reminder.goal-cron}")
    public void remindGoalNotMet() {
        log.info("开始执行【周目标未达标提醒】定时任务");
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        List<User> users = userMapper.selectList(new QueryWrapper<User>()
                .eq("role", RoleConstant.USER).eq("status", 1));
        for (User user : users) {
            ExerciseGoal goal = goalMapper.selectOne(new QueryWrapper<ExerciseGoal>()
                    .eq("user_id", user.getId()));
            if (goal == null) {
                continue;
            }
            List<CheckRecord> weekRecords = checkRecordMapper.selectList(new QueryWrapper<CheckRecord>()
                    .eq("user_id", user.getId())
                    .ge("check_date", weekStart)
                    .le("check_date", today));
            long days = weekRecords.stream().map(CheckRecord::getCheckDate).distinct().count();
            long duration = weekRecords.stream().mapToLong(r -> r.getDuration() == null ? 0 : r.getDuration()).sum();
            long calories = weekRecords.stream().mapToLong(r -> r.getCalories() == null ? 0 : r.getCalories()).sum();

            boolean notMet = days < goal.getWeekDays()
                    || duration < goal.getWeekDuration()
                    || calories < goal.getWeekCalories();
            if (notMet) {
                insertNotice(user.getId(), "本周目标未达标",
                        "本周运动目标尚未达成，下周继续加油！", "REMIND");
            }
        }
        log.info("【周目标未达标提醒】执行完毕");
    }

    /**
     * 写入提醒，同一天相同标题去重
     */
    private void insertNotice(Long userId, String title, String content, String type) {
        Long exists = noticeMapper.selectCount(new QueryWrapper<Notice>()
                .eq("user_id", userId)
                .eq("title", title)
                .ge("create_time", LocalDate.now().atStartOfDay()));
        if (exists != null && exists > 0) {
            return;
        }
        Notice notice = new Notice();
        notice.setUserId(userId);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setType(type);
        notice.setIsRead(0);
        noticeMapper.insert(notice);
    }
}
