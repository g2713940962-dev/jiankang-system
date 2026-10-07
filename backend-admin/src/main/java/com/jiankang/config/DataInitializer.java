package com.jiankang.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiankang.common.constant.RoleConstant;
import com.jiankang.entity.User;
import com.jiankang.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 数据初始化：首次启动时自动创建管理员账号
 * <p>
 * 默认账号：admin / 123456（管理员）
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initAdmin();
    }

    private void initAdmin() {
        Long count = userMapper.selectCount(new QueryWrapper<User>().eq("username", "admin"));
        if (count != null && count > 0) {
            return;
        }
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setNickname("系统管理员");
        admin.setRole(RoleConstant.ADMIN);
        admin.setStatus(1);
        userMapper.insert(admin);
        log.info("已初始化管理员账号：admin / 123456");
    }
}
