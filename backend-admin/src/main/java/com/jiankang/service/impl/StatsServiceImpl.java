package com.jiankang.service.impl;

import com.jiankang.mapper.CheckRecordMapper;
import com.jiankang.mapper.UserMapper;
import com.jiankang.service.StatsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统计服务实现（管理端：全站统计）
 */
@Service
public class StatsServiceImpl implements StatsService {

    @Resource
    private CheckRecordMapper checkRecordMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Map<String, Object> overview() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("userCount", userMapper.selectCount(null));
        map.put("adminCount", userMapper.countByRole("ADMIN"));
        map.put("checkCount", checkRecordMapper.countAll());
        map.put("totalDuration", checkRecordMapper.sumAllDuration());
        map.put("totalCalories", checkRecordMapper.sumAllCalories());
        map.put("topUsers", checkRecordMapper.topUsers());
        return map;
    }
}
