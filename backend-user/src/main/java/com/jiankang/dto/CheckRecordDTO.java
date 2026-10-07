package com.jiankang.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 新增/修改打卡记录请求参数
 */
@Data
public class CheckRecordDTO {

    @NotNull(message = "打卡日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkDate;

    @NotBlank(message = "运动类型不能为空")
    private String checkType;

    @NotNull(message = "运动时长不能为空")
    @Min(value = 1, message = "运动时长至少 1 分钟")
    private Integer duration;

    @Min(value = 0, message = "卡路里不能为负")
    private Integer calories;

    @Min(value = 0, message = "步数不能为负")
    private Integer stepCount;

    private String remark;
}
