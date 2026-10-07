package com.jiankang.service;

import com.jiankang.dto.LoginDTO;
import com.jiankang.vo.LoginVO;
import com.jiankang.vo.UserVO;

/**
 * 认证服务（管理端）
 */
public interface AuthService {

    LoginVO login(LoginDTO dto);

    UserVO info();
}
