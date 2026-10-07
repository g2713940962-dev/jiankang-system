package com.jiankang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiankang.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户 Mapper
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT COUNT(*) FROM sys_user WHERE role = #{role}")
    Long countByRole(@Param("role") String role);
}
