package com.jiankang.service;

import com.jiankang.dto.GoalDTO;
import com.jiankang.entity.ExerciseGoal;

/**
 * 运动目标服务
 */
public interface GoalService {

    ExerciseGoal get();

    ExerciseGoal save(GoalDTO dto);
}
