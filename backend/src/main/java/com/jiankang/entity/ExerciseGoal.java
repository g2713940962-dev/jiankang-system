package com.jiankang.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 运动目标实体（每周 / 每月目标各三项）
 */
@Data
@TableName("exercise_goal")
public class ExerciseGoal implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户 id（一个用户一条目标） */
    private Long userId;

    /** 每周运动天数目标 */
    private Integer weekDays;

    /** 每周运动时长目标（分钟） */
    private Integer weekDuration;

    /** 每周消耗卡路里目标（千卡） */
    private Integer weekCalories;

    /** 每月运动天数目标 */
    private Integer monthDays;

    /** 每月运动时长目标（分钟） */
    private Integer monthDuration;

    /** 每月消耗卡路里目标（千卡） */
    private Integer monthCalories;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
