package com.jiankang.service;

import com.jiankang.vo.DashboardVO;

import java.util.Map;

/**
 * 统计服务（数据可视化）
 */
public interface StatsService {

    DashboardVO dashboard();

    Map<String, Object> overview();
}
