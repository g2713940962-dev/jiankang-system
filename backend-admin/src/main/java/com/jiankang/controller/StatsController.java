package com.jiankang.controller;

import com.jiankang.common.Result;
import com.jiankang.common.annotation.RequireRole;
import com.jiankang.common.constant.RoleConstant;
import com.jiankang.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 统计接口（管理端：全站统计）
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Resource
    private StatsService statsService;

    @GetMapping("/overview")
    @RequireRole(RoleConstant.ADMIN)
    public Result<Map<String, Object>> overview() {
        return Result.success(statsService.overview());
    }
}
