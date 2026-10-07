package com.jiankang.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankang.common.Result;
import com.jiankang.entity.Notice;
import com.jiankang.service.NoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息接口
 */
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @GetMapping("/page")
    public Result<Page<Notice>> page(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(noticeService.page(page, size));
    }

    @GetMapping("/unread")
    public Result<Long> unreadCount() {
        return Result.success(noticeService.unreadCount());
    }

    @PutMapping("/{id}/read")
    public Result<Void> read(@PathVariable Long id) {
        noticeService.read(id);
        return Result.success();
    }

    @PutMapping("/read-all")
    public Result<Void> readAll() {
        noticeService.readAll();
        return Result.success();
    }
}
