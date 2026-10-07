package com.jiankang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiankang.common.ResultCode;
import com.jiankang.common.constant.RoleConstant;
import com.jiankang.common.exception.BusinessException;
import com.jiankang.common.util.JwtUtil;
import com.jiankang.common.util.UserContext;
import com.jiankang.dto.LoginDTO;
import com.jiankang.dto.RegisterDTO;
import com.jiankang.dto.UpdatePasswordDTO;
import com.jiankang.dto.UpdateProfileDTO;
import com.jiankang.entity.User;
import com.jiankang.mapper.UserMapper;
import com.jiankang.service.AuthService;
import com.jiankang.vo.LoginVO;
import com.jiankang.vo.UserVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 认证服务实现
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
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUser(UserVO.from(user));
        return vo;
    }

    @Override
    public UserVO register(RegisterDTO dto) {
        Long count = userMapper.selectCount(new QueryWrapper<User>().eq("username", dto.getUsername()));
        if (count != null && count > 0) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(StringUtils.hasText(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setRole(RoleConstant.USER);
        user.setStatus(1);
        userMapper.insert(user);
        return UserVO.from(user);
    }

    @Override
    public UserVO info() {
        return UserVO.from(getCurrentUser());
    }

    @Override
    public void updatePassword(UpdatePasswordDTO dto) {
        User user = getCurrentUser();
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.OLD_PASSWORD_ERROR);
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateById(user);
    }

    @Override
    public UserVO updateProfile(UpdateProfileDTO dto) {
        User user = getCurrentUser();
        if (StringUtils.hasText(dto.getNickname())) {
            user.setNickname(dto.getNickname());
        }
        user.setPhone(dto.getPhone());
        user.setAvatar(dto.getAvatar());
        userMapper.updateById(user);
        return UserVO.from(user);
    }

    private User getCurrentUser() {
        User user = userMapper.selectById(UserContext.getUserId());
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return user;
    }
}
