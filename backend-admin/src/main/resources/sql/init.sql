-- =====================================================
-- 健康打卡与运动达标管理系统 - 数据库初始化脚本
-- 说明：只需执行本脚本一次，即可创建数据库和表结构。
--       管理员 / 演示用户 / 示例数据由后端首次启动时自动生成。
-- =====================================================

CREATE DATABASE IF NOT EXISTS `jiankang` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `jiankang`;

DROP TABLE IF EXISTS `notice`;
DROP TABLE IF EXISTS `exercise_goal`;
DROP TABLE IF EXISTS `check_record`;
DROP TABLE IF EXISTS `sys_user`;

-- 用户表
CREATE TABLE `sys_user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(50)  NOT NULL COMMENT '用户名（登录账号）',
    `password`    VARCHAR(100) NOT NULL COMMENT '密码（BCrypt 加密）',
    `nickname`    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `role`        VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '角色：ADMIN/USER',
    `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';

-- 打卡记录表
CREATE TABLE `check_record` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     BIGINT       NOT NULL COMMENT '用户 id',
    `check_date`  DATE         NOT NULL COMMENT '打卡日期',
    `check_type`  VARCHAR(20)  NOT NULL COMMENT '运动类型：晨跑/健身/球类/骑行/瑜伽/其他',
    `duration`    INT          NOT NULL DEFAULT 0 COMMENT '运动时长（分钟）',
    `calories`    INT          DEFAULT 0 COMMENT '消耗卡路里（千卡）',
    `step_count`  INT          DEFAULT 0 COMMENT '步数',
    `remark`      VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_date` (`user_id`, `check_date`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '打卡记录表';

-- 运动目标表（每周 / 每月目标各三项）
CREATE TABLE `exercise_goal` (
    `id`             BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`        BIGINT   NOT NULL COMMENT '用户 id（一个用户一条目标）',
    `week_days`      INT      DEFAULT 0 COMMENT '每周运动天数目标',
    `week_duration`  INT      DEFAULT 0 COMMENT '每周运动时长目标（分钟）',
    `week_calories`  INT      DEFAULT 0 COMMENT '每周消耗卡路里目标（千卡）',
    `month_days`     INT      DEFAULT 0 COMMENT '每月运动天数目标',
    `month_duration` INT      DEFAULT 0 COMMENT '每月运动时长目标（分钟）',
    `month_calories` INT      DEFAULT 0 COMMENT '每月消耗卡路里目标（千卡）',
    `create_time`    DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time`    DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '运动目标表';

-- 站内消息 / 提醒表
CREATE TABLE `notice` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     BIGINT       NOT NULL COMMENT '接收用户 id',
    `title`       VARCHAR(100) NOT NULL COMMENT '标题',
    `content`     VARCHAR(500) DEFAULT NULL COMMENT '内容',
    `type`        VARCHAR(20)  NOT NULL DEFAULT 'SYSTEM' COMMENT '类型：REMIND提醒/SYSTEM系统',
    `is_read`     TINYINT      NOT NULL DEFAULT 0 COMMENT '是否已读：0未读 1已读',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_read` (`user_id`, `is_read`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '站内消息表';
