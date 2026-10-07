package com.jiankang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiankang.common.util.UserContext;
import com.jiankang.entity.CheckRecord;
import com.jiankang.entity.ExerciseGoal;
import com.jiankang.mapper.CheckRecordMapper;
import com.jiankang.mapper.ExerciseGoalMapper;
import com.jiankang.service.StatsService;
import com.jiankang.vo.DashboardVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计服务实现（用户端：个人仪表盘）
 */
@Service
public class StatsServiceImpl implements StatsService {

    @Resource
    private CheckRecordMapper checkRecordMapper;

    @Resource
    private ExerciseGoalMapper goalMapper;

    @Override
    public DashboardVO dashboard() {
        Long userId = UserContext.getUserId();
        List<CheckRecord> records = checkRecordMapper.selectList(
                new QueryWrapper<CheckRecord>().eq("user_id", userId).orderByAsc("check_date"));

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate monthStart = today.withDayOfMonth(1);

        DashboardVO vo = new DashboardVO();
        vo.setSummary(buildSummary(records, today, weekStart));
        vo.setTrend(buildTrend(records, today));
        vo.setTypes(buildTypes(records));

        ExerciseGoal goal = goalMapper.selectOne(
                new QueryWrapper<ExerciseGoal>().eq("user_id", userId));
        vo.setWeek(buildProgress(records, weekStart, today, goal, true));
        vo.setMonth(buildProgress(records, monthStart, today, goal, false));
        return vo;
    }

    private DashboardVO.Summary buildSummary(List<CheckRecord> records, LocalDate today, LocalDate weekStart) {
        DashboardVO.Summary summary = new DashboardVO.Summary();
        summary.setTotalCount((long) records.size());
        summary.setTotalDuration(sumDuration(records));
        summary.setTotalCalories(sumCalories(records));

        List<CheckRecord> todayRecords = records.stream()
                .filter(r -> today.equals(r.getCheckDate()))
                .collect(Collectors.toList());
        summary.setTodayCount((long) todayRecords.size());
        summary.setTodayDuration(sumDuration(todayRecords));
        summary.setTodayCalories(sumCalories(todayRecords));

        long weekDays = records.stream()
                .filter(r -> !r.getCheckDate().isBefore(weekStart))
                .map(CheckRecord::getCheckDate)
                .distinct()
                .count();
        summary.setWeekDays(weekDays);
        return summary;
    }

    private List<DashboardVO.TrendItem> buildTrend(List<CheckRecord> records, LocalDate today) {
        List<DashboardVO.TrendItem> trend = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            List<CheckRecord> dayRecords = records.stream()
                    .filter(r -> date.equals(r.getCheckDate()))
                    .collect(Collectors.toList());
            DashboardVO.TrendItem item = new DashboardVO.TrendItem();
            item.setDate(date.toString());
            item.setCount((long) dayRecords.size());
            item.setDuration(sumDuration(dayRecords));
            item.setCalories(sumCalories(dayRecords));
            trend.add(item);
        }
        return trend;
    }

    private List<DashboardVO.TypeItem> buildTypes(List<CheckRecord> records) {
        Map<String, Long> typeCount = records.stream()
                .collect(Collectors.groupingBy(CheckRecord::getCheckType, Collectors.counting()));
        List<DashboardVO.TypeItem> types = new ArrayList<>();
        for (Map.Entry<String, Long> entry : typeCount.entrySet()) {
            DashboardVO.TypeItem item = new DashboardVO.TypeItem();
            item.setType(entry.getKey());
            item.setCount(entry.getValue());
            types.add(item);
        }
        return types;
    }

    private DashboardVO.GoalProgress buildProgress(List<CheckRecord> records, LocalDate start, LocalDate end,
                                                   ExerciseGoal goal, boolean isWeek) {
        List<CheckRecord> list = records.stream()
                .filter(r -> !r.getCheckDate().isBefore(start) && !r.getCheckDate().isAfter(end))
                .collect(Collectors.toList());

        DashboardVO.GoalProgress p = new DashboardVO.GoalProgress();
        p.setDaysActual(list.stream().map(CheckRecord::getCheckDate).distinct().count());
        p.setDurationActual(sumDuration(list));
        p.setCaloriesActual(sumCalories(list));

        if (goal == null) {
            p.setDaysTarget(0);
            p.setDurationTarget(0);
            p.setCaloriesTarget(0);
        } else if (isWeek) {
            p.setDaysTarget(goal.getWeekDays());
            p.setDurationTarget(goal.getWeekDuration());
            p.setCaloriesTarget(goal.getWeekCalories());
        } else {
            p.setDaysTarget(goal.getMonthDays());
            p.setDurationTarget(goal.getMonthDuration());
            p.setCaloriesTarget(goal.getMonthCalories());
        }
        return p;
    }

    private long sumDuration(List<CheckRecord> records) {
        return records.stream().mapToLong(r -> r.getDuration() == null ? 0 : r.getDuration()).sum();
    }

    private long sumCalories(List<CheckRecord> records) {
        return records.stream().mapToLong(r -> r.getCalories() == null ? 0 : r.getCalories()).sum();
    }
}
