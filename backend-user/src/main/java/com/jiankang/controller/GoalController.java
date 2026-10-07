package com.jiankang.controller;

import com.jiankang.common.Result;
import com.jiankang.dto.GoalDTO;
import com.jiankang.entity.ExerciseGoal;
import com.jiankang.service.GoalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 运动目标接口
 */
@RestController
@RequestMapping("/api/goal")
public class GoalController {

    @Resource
    private GoalService goalService;

    @GetMapping
    public Result<ExerciseGoal> get() {
        return Result.success(goalService.get());
    }

    @PostMapping
    public Result<ExerciseGoal> save(@Valid @RequestBody GoalDTO dto) {
        return Result.success(goalService.save(dto));
    }
}
