package com.jiankang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiankang.common.util.UserContext;
import com.jiankang.dto.GoalDTO;
import com.jiankang.entity.ExerciseGoal;
import com.jiankang.mapper.ExerciseGoalMapper;
import com.jiankang.service.GoalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 运动目标服务实现
 */
@Service
public class GoalServiceImpl implements GoalService {

    @Resource
    private ExerciseGoalMapper goalMapper;

    @Override
    public ExerciseGoal get() {
        ExerciseGoal goal = goalMapper.selectOne(
                new QueryWrapper<ExerciseGoal>().eq("user_id", UserContext.getUserId()));
        if (goal == null) {
            return defaultGoal();
        }
        return goal;
    }

    @Override
    public ExerciseGoal save(GoalDTO dto) {
        ExerciseGoal goal = goalMapper.selectOne(
                new QueryWrapper<ExerciseGoal>().eq("user_id", UserContext.getUserId()));
        if (goal == null) {
            goal = new ExerciseGoal();
            goal.setUserId(UserContext.getUserId());
            apply(goal, dto);
            goalMapper.insert(goal);
        } else {
            apply(goal, dto);
            goalMapper.updateById(goal);
        }
        return goal;
    }

    private void apply(ExerciseGoal goal, GoalDTO dto) {
        goal.setWeekDays(dto.getWeekDays());
        goal.setWeekDuration(dto.getWeekDuration());
        goal.setWeekCalories(dto.getWeekCalories());
        goal.setMonthDays(dto.getMonthDays());
        goal.setMonthDuration(dto.getMonthDuration());
        goal.setMonthCalories(dto.getMonthCalories());
    }

    private ExerciseGoal defaultGoal() {
        ExerciseGoal goal = new ExerciseGoal();
        goal.setUserId(UserContext.getUserId());
        goal.setWeekDays(0);
        goal.setWeekDuration(0);
        goal.setWeekCalories(0);
        goal.setMonthDays(0);
        goal.setMonthDuration(0);
        goal.setMonthCalories(0);
        return goal;
    }
}
