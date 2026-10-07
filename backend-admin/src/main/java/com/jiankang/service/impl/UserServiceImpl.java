package com.jiankang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankang.common.ResultCode;
import com.jiankang.common.exception.BusinessException;
import com.jiankang.common.util.UserContext;
import com.jiankang.entity.User;
import com.jiankang.mapper.UserMapper;
import com.jiankang.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 用户管理服务实现（管理员）
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Page<User> page(Integer page, Integer size, String keyword) {
        Page<User> p = new Page<>(page, size);
        QueryWrapper<User> qw = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            qw.and(w -> w.like("username", keyword).or().like("nickname", keyword));
        }
        qw.orderByDesc("id");
        return userMapper.selectPage(p, qw);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if (id.equals(UserContext.getUserId())) {
            throw new BusinessException("不能操作当前登录账号");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        user.setStatus(status);
        userMapper.updateById(user);
    }

    @Override
    public void resetPassword(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        user.setPassword(passwordEncoder.encode("123456"));
        userMapper.updateById(user);
    }

    @Override
    public void delete(Long id) {
        if (id.equals(UserContext.getUserId())) {
            throw new BusinessException("不能删除当前登录账号");
        }
        userMapper.deleteById(id);
    }
}
