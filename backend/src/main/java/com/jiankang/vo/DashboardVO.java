package com.jiankang.vo;

import lombok.Data;

import java.util.List;

/**
 * 个人仪表盘数据（数据可视化）
 */
@Data
public class DashboardVO {

    /** 汇总统计 */
    private Summary summary;

    /** 近 7 天打卡趋势 */
    private List<TrendItem> trend;

    /** 运动类型分布 */
    private List<TypeItem> types;

    /** 本周目标达成情况 */
    private GoalProgress week;

    /** 本月目标达成情况 */
    private GoalProgress month;

    @Data
    public static class Summary {
        private Long totalCount;
        private Long totalDuration;
        private Long totalCalories;
        private Long todayCount;
        private Long todayDuration;
        private Long todayCalories;
        private Long weekDays;
    }

    @Data
    public static class TrendItem {
        private String date;
        private Long count;
        private Long duration;
        private Long calories;
    }

    @Data
    public static class TypeItem {
        private String type;
        private Long count;
    }

    @Data
    public static class GoalProgress {
        private Long daysActual;
        private Integer daysTarget;
        private Long durationActual;
        private Integer durationTarget;
        private Long caloriesActual;
        private Integer caloriesTarget;
    }
}
