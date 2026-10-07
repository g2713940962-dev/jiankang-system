package com.jiankang.controller;

import com.jiankang.common.Result;
import com.jiankang.service.StatsService;
import com.jiankang.vo.DashboardVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 统计接口（用户端：个人仪表盘）
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
}
