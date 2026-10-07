package com.jiankang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiankang.common.ResultCode;
import com.jiankang.common.constant.RoleConstant;
import com.jiankang.common.exception.BusinessException;
import com.jiankang.common.util.JwtUtil;
import com.jiankang.common.util.UserContext;
import com.jiankang.dto.LoginDTO;
import com.jiankang.entity.User;
import com.jiankang.mapper.UserMapper;
import com.jiankang.service.AuthService;
import com.jiankang.vo.LoginVO;
import com.jiankang.vo.UserVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 认证服务实现（管理端）
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoginVO login(LoginDTO dto) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", dto.getUsername()));
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(ResultCode.ACCOUNT_DISABLED);
        }
        // 管理端仅允许管理员账号登录
        if (!RoleConstant.ADMIN.equals(user.getRole())) {
            throw new BusinessException(ResultCode.NOT_ADMIN);
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUser(UserVO.from(user));
        return vo;
    }

    @Override
    public UserVO info() {
        return UserVO.from(getCurrentUser());
    }

    private User getCurrentUser() {
        User user = userMapper.selectById(UserContext.getUserId());
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return user;
    }
}
