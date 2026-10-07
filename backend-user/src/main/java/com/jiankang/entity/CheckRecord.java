package com.jiankang.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 打卡记录实体
 */
@Data
@TableName("check_record")
public class CheckRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户 id */
    private Long userId;

    /** 打卡日期 */
    private LocalDate checkDate;

    /** 运动类型：晨跑/健身/球类/骑行/瑜伽/其他 */
    private String checkType;

    /** 运动时长（分钟） */
    private Integer duration;

    /** 消耗卡路里（千卡） */
    private Integer calories;

    /** 步数 */
    private Integer stepCount;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
