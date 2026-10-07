package com.jiankang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 健康打卡与运动达标管理系统 启动类
 */
@SpringBootApplication
@MapperScan("com.jiankang.mapper")
@EnableScheduling
public class JiankangApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiankangApplication.class, args);
    }
}
