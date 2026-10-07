package com.jiankang.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankang.entity.Notice;

/**
 * 消息服务
 */
public interface NoticeService {

    Page<Notice> page(Integer page, Integer size);

    Long unreadCount();

    void read(Long id);

    void readAll();
}
