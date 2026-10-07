<template>
  <div class="page-container">
    <!-- 统计卡片 -->
    <el-row :gutter="16">
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #3a8ee6, #6aa9f0)">
          <div class="label">累计打卡（次）</div>
          <div class="value">{{ dashboard.summary?.totalCount ?? 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #34c77b, #6ad6a0)">
          <div class="label">累计运动时长（分钟）</div>
          <div class="value">{{ dashboard.summary?.totalDuration ?? 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #ff9f43, #ffbe76)">
          <div class="label">累计消耗卡路里（千卡）</div>
          <div class="value">{{ dashboard.summary?.totalCalories ?? 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #a55eea, #c489f0)">
          <div class="label">本周运动天数</div>
          <div class="value">{{ dashboard.summary?.weekDays ?? 0 }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="14">
        <div class="card">
          <div class="chart-title">近 7 天打卡趋势</div>
          <div ref="trendRef" class="chart" style="height: 320px"></div>
        </div>
      </el-col>
      <el-col :span="10">
        <div class="card">
          <div class="chart-title">运动类型分布</div>
          <div ref="typeRef" class="chart" style="height: 320px"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 目标达成 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="12">
        <div class="card">
          <div class="chart-title">本周目标达成情况</div>
          <div class="goal-row">
            <div class="goal-row-head"><span>运动天数</span><span>{{ dashboard.week?.daysActual ?? 0 }} / {{ dashboard.week?.daysTarget ?? 0 }} 天</span></div>
            <el-progress :percentage="pct(dashboard.week?.daysActual, dashboard.week?.daysTarget)" :stroke-width="12" />
          </div>
          <div class="goal-row">
            <div class="goal-row-head"><span>运动时长</span><span>{{ dashboard.week?.durationActual ?? 0 }} / {{ dashboard.week?.durationTarget ?? 0 }} 分钟</span></div>
            <el-progress :percentage="pct(dashboard.week?.durationActual, dashboard.week?.durationTarget)" :stroke-width="12" />
          </div>
          <div class="goal-row">
            <div class="goal-row-head"><span>消耗卡路里</span><span>{{ dashboard.week?.caloriesActual ?? 0 }} / {{ dashboard.week?.caloriesTarget ?? 0 }} 千卡</span></div>
            <el-progress :percentage="pct(dashboard.week?.caloriesActual, dashboard.week?.caloriesTarget)" :stroke-width="12" />
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card">
          <div class="chart-title">本月目标达成情况</div>
          <div class="goal-row">
            <div class="goal-row-head"><span>运动天数</span><span>{{ dashboard.month?.daysActual ?? 0 }} / {{ dashboard.month?.daysTarget ?? 0 }} 天</span></div>
            <el-progress :percentage="pct(dashboard.month?.daysActual, dashboard.month?.daysTarget)" :stroke-width="12" />
          </div>
          <div class="goal-row">
            <div class="goal-row-head"><span>运动时长</span><span>{{ dashboard.month?.durationActual ?? 0 }} / {{ dashboard.month?.durationTarget ?? 0 }} 分钟</span></div>
            <el-progress :percentage="pct(dashboard.month?.durationActual, dashboard.month?.durationTarget)" :stroke-width="12" />
          </div>
          <div class="goal-row">
            <div class="goal-row-head"><span>消耗卡路里</span><span>{{ dashboard.month?.caloriesActual ?? 0 }} / {{ dashboard.month?.caloriesTarget ?? 0 }} 千卡</span></div>
            <el-progress :percentage="pct(dashboard.month?.caloriesActual, dashboard.month?.caloriesTarget)" :stroke-width="12" />
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount, reactive, ref, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getDashboard } from '../api/stats'

const dashboard = reactive({ summary: {}, trend: [], types: [], week: {}, month: {} })
const trendRef = ref()
const typeRef = ref()
let trendChart = null
let typeChart = null

const pct = (actual, target) => {
  if (!target || target <= 0) return 0
  return Math.min(100, Math.round((actual / target) * 100))
}

const load = async () => {
  try {
    const data = await getDashboard()
    Object.assign(dashboard, data)
    await nextTick()
    renderTrend()
    renderType()
  } catch (e) {
    // 错误提示已在拦截器统一处理
  }
}

const renderTrend = () => {
  if (!trendRef.value) return
  if (!trendChart) trendChart = echarts.init(trendRef.value)
  const dates = (dashboard.trend || []).map((t) => t.date.slice(5))
  const counts = (dashboard.trend || []).map((t) => t.count)
  const durations = (dashboard.trend || []).map((t) => t.duration)
  trendChart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['打卡次数', '运动时长(分钟)'] },
    grid: { left: 40, right: 50, top: 40, bottom: 30 },
    xAxis: { type: 'category', data: dates },
    yAxis: [
      { type: 'value', name: '次数', minInterval: 1 },
      { type: 'value', name: '分钟' }
    ],
    series: [
      { name: '打卡次数', type: 'bar', data: counts, itemStyle: { color: '#3a8ee6' } },
      { name: '运动时长(分钟)', type: 'line', yAxisIndex: 1, smooth: true, data: durations, itemStyle: { color: '#34c77b' } }
    ]
  })
}

const renderType = () => {
  if (!typeRef.value) return
  if (!typeChart) typeChart = echarts.init(typeRef.value)
  const data = (dashboard.types || []).map((t) => ({ name: t.type, value: t.count }))
  typeChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [
      {
        type: 'pie',
        radius: ['40%', '65%'],
        center: ['60%', '50%'],
        avoidLabelOverlap: true,
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        label: { show: false },
        data
      }
    ]
  })
}

const onResize = () => {
  trendChart && trendChart.resize()
  typeChart && typeChart.resize()
}

onMounted(() => {
  load()
  window.addEventListener('resize', onResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', onResize)
  trendChart && trendChart.dispose()
  typeChart && typeChart.dispose()
})
</script>

<style scoped>
.chart-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
}
.chart {
  width: 100%;
}
.goal-row {
  margin-bottom: 14px;
}
.goal-row-head {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
}
</style>
