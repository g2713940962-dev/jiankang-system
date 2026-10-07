package com.jiankang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankang.common.ResultCode;
import com.jiankang.common.exception.BusinessException;
import com.jiankang.common.util.UserContext;
import com.jiankang.entity.Notice;
import com.jiankang.mapper.NoticeMapper;
import com.jiankang.service.NoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 消息服务实现
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    @Override
    public Page<Notice> page(Integer page, Integer size) {
        Page<Notice> p = new Page<>(page, size);
        QueryWrapper<Notice> qw = new QueryWrapper<>();
        qw.eq("user_id", UserContext.getUserId());
        qw.orderByAsc("is_read").orderByDesc("create_time");
        return noticeMapper.selectPage(p, qw);
    }

    @Override
    public Long unreadCount() {
        return noticeMapper.countUnread(UserContext.getUserId());
    }

    @Override
    public void read(Long id) {
        Notice notice = noticeMapper.selectById(id);
        if (notice == null || !notice.getUserId().equals(UserContext.getUserId())) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        notice.setIsRead(1);
        noticeMapper.updateById(notice);
    }

    @Override
    public void readAll() {
        Notice update = new Notice();
        update.setIsRead(1);
        noticeMapper.update(update, new QueryWrapper<Notice>()
                .eq("user_id", UserContext.getUserId())
                .eq("is_read", 0));
    }
}
