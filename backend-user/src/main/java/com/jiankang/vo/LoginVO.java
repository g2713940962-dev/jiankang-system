package com.jiankang.vo;

import lombok.Data;

/**
 * 登录返回结果
 */
@Data
public class LoginVO {

    private String token;

    private UserVO user;
}
