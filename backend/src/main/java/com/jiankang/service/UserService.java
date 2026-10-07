package com.jiankang.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankang.entity.User;

/**
 * 用户管理服务（管理员）
 */
public interface UserService {

    Page<User> page(Integer page, Integer size, String keyword);

    void updateStatus(Long id, Integer status);

    void resetPassword(Long id);

    void delete(Long id);
}
