package com.jiankang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiankang.entity.CheckRecord;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 打卡记录 Mapper
 */
public interface CheckRecordMapper extends BaseMapper<CheckRecord> {

    @Select("SELECT COUNT(*) FROM check_record")
    Long countAll();

    @Select("SELECT COALESCE(SUM(duration),0) FROM check_record")
    Long sumAllDuration();

    @Select("SELECT COALESCE(SUM(calories),0) FROM check_record")
    Long sumAllCalories();

    /**
     * 用户运动排行（按累计时长倒序，取前 10）
     */
    @Select("SELECT u.id AS userId, u.nickname AS nickname, u.username AS username, " +
            "COUNT(c.id) AS checkCount, COALESCE(SUM(c.duration),0) AS totalDuration, " +
            "COALESCE(SUM(c.calories),0) AS totalCalories " +
            "FROM sys_user u LEFT JOIN check_record c ON u.id = c.user_id " +
            "WHERE u.role = 'USER' AND u.status = 1 " +
            "GROUP BY u.id, u.nickname, u.username " +
            "ORDER BY totalDuration DESC LIMIT 10")
    List<Map<String, Object>> topUsers();
}
