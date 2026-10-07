package com.jiankang.service;

import com.jiankang.dto.LoginDTO;
import com.jiankang.dto.RegisterDTO;
import com.jiankang.dto.UpdatePasswordDTO;
import com.jiankang.dto.UpdateProfileDTO;
import com.jiankang.vo.LoginVO;
import com.jiankang.vo.UserVO;

/**
 * 认证服务
 */
public interface AuthService {

    LoginVO login(LoginDTO dto);

    UserVO register(RegisterDTO dto);

    UserVO info();

    void updatePassword(UpdatePasswordDTO dto);

    UserVO updateProfile(UpdateProfileDTO dto);
}
