package com.jiankang.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiankang.common.constant.RoleConstant;
import com.jiankang.entity.CheckRecord;
import com.jiankang.entity.ExerciseGoal;
import com.jiankang.entity.User;
import com.jiankang.mapper.CheckRecordMapper;
import com.jiankang.mapper.ExerciseGoalMapper;
import com.jiankang.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Random;

/**
 * 数据初始化：首次启动时自动创建管理员、演示用户及示例数据
 * <p>
 * 默认账号：admin / 123456（管理员）、user / 123456（普通用户）
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CheckRecordMapper checkRecordMapper;

    @Resource
    private ExerciseGoalMapper goalMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initAdmin();
        initDemoUser();
    }

    private void initAdmin() {
        Long count = userMapper.selectCount(new QueryWrapper<User>().eq("username", "admin"));
        if (count != null && count > 0) {
            return;
        }
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setNickname("系统管理员");
        admin.setRole(RoleConstant.ADMIN);
        admin.setStatus(1);
        userMapper.insert(admin);
        log.info("已初始化管理员账号：admin / 123456");
    }

    private void initDemoUser() {
        Long count = userMapper.selectCount(new QueryWrapper<User>().eq("username", "user"));
        if (count != null && count > 0) {
            return;
        }
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setNickname("运动达人");
        user.setRole(RoleConstant.USER);
        user.setStatus(1);
        userMapper.insert(user);
        seedCheckRecords(user.getId());
        seedGoal(user.getId());
        log.info("已初始化演示用户：user / 123456，并生成示例打卡数据");
    }

    /**
     * 生成近 10 天示例打卡数据，便于演示数据可视化
     */
    private void seedCheckRecords(Long userId) {
        String[] types = {"晨跑", "健身", "球类", "骑行", "瑜伽"};
        Random random = new Random(42);
        LocalDate today = LocalDate.now();
        for (int i = 9; i >= 0; i--) {
            int count = random.nextInt(2) + 1;
            for (int j = 0; j < count; j++) {
                CheckRecord record = new CheckRecord();
                record.setUserId(userId);
                record.setCheckDate(today.minusDays(i));
                record.setCheckType(types[random.nextInt(types.length)]);
                record.setDuration(random.nextInt(40) + 30);
                record.setCalories(random.nextInt(300) + 200);
                record.setStepCount(random.nextInt(8000) + 3000);
                record.setRemark("示例打卡数据");
                checkRecordMapper.insert(record);
            }
        }
    }

    private void seedGoal(Long userId) {
        ExerciseGoal goal = new ExerciseGoal();
        goal.setUserId(userId);
        goal.setWeekDays(5);
        goal.setWeekDuration(180);
        goal.setWeekCalories(1200);
        goal.setMonthDays(20);
        goal.setMonthDuration(800);
        goal.setMonthCalories(5000);
        goalMapper.insert(goal);
    }
}
