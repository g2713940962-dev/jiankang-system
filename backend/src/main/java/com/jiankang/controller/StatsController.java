package com.jiankang.controller;

import com.jiankang.common.Result;
import com.jiankang.common.annotation.RequireRole;
import com.jiankang.common.constant.RoleConstant;
import com.jiankang.service.StatsService;
import com.jiankang.vo.DashboardVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 统计接口（数据可视化）
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Resource
    private StatsService statsService;

    @GetMapping("/dashboard")
    public Result<DashboardVO> dashboard() {
        return Result.success(statsService.dashboard());
    }

    @GetMapping("/overview")
    @RequireRole(RoleConstant.ADMIN)
    public Result<Map<String, Object>> overview() {
        return Result.success(statsService.overview());
    }
}
