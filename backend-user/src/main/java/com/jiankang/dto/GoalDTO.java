package com.jiankang.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 运动目标设置请求参数
 */
@Data
public class GoalDTO {

    @NotNull(message = "每周运动天数目标不能为空")
    @Min(value = 0, message = "天数目标不能为负")
    @Max(value = 7, message = "每周天数目标不能超过 7")
    private Integer weekDays;

    @NotNull(message = "每周运动时长目标不能为空")
    @Min(value = 0, message = "时长目标不能为负")
    private Integer weekDuration;

    @NotNull(message = "每周卡路里目标不能为空")
    @Min(value = 0, message = "卡路里目标不能为负")
    private Integer weekCalories;

    @NotNull(message = "每月运动天数目标不能为空")
    @Min(value = 0, message = "天数目标不能为负")
    @Max(value = 31, message = "每月天数目标不能超过 31")
    private Integer monthDays;

    @NotNull(message = "每月运动时长目标不能为空")
    @Min(value = 0, message = "时长目标不能为负")
    private Integer monthDuration;

    @NotNull(message = "每月卡路里目标不能为空")
    @Min(value = 0, message = "卡路里目标不能为负")
    private Integer monthCalories;
}
