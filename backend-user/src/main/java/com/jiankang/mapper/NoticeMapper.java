package com.jiankang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiankang.entity.Notice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 消息 Mapper
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    @Select("SELECT COUNT(*) FROM notice WHERE user_id = #{userId} AND is_read = 0")
    Long countUnread(@Param("userId") Long userId);
}
