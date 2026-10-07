package com.jiankang.dto;

import lombok.Data;

/**
 * 更新个人资料请求参数
 */
@Data
public class UpdateProfileDTO {

    private String nickname;

    private String phone;

    private String avatar;
}
