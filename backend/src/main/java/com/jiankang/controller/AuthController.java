package com.jiankang.controller;

import com.jiankang.common.Result;
import com.jiankang.dto.LoginDTO;
import com.jiankang.dto.RegisterDTO;
import com.jiankang.dto.UpdatePasswordDTO;
import com.jiankang.dto.UpdateProfileDTO;
import com.jiankang.service.AuthService;
import com.jiankang.vo.LoginVO;
import com.jiankang.vo.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 认证接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterDTO dto) {
        return Result.success(authService.register(dto));
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }

    @GetMapping("/info")
    public Result<UserVO> info() {
        return Result.success(authService.info());
    }

    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto) {
        authService.updatePassword(dto);
        return Result.success();
    }

    @PutMapping("/profile")
    public Result<UserVO> updateProfile(@RequestBody UpdateProfileDTO dto) {
        return Result.success(authService.updateProfile(dto));
    }
}
