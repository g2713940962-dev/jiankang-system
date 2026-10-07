<template>
  <div class="page-container">
    <!-- 统计卡片 -->
    <el-row :gutter="16">
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #3a8ee6, #6aa9f0)">
          <div class="label">用户总数</div>
          <div class="value">{{ overview.userCount ?? 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #ff9f43, #ffbe76)">
          <div class="label">管理员数量</div>
          <div class="value">{{ overview.adminCount ?? 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #34c77b, #6ad6a0)">
          <div class="label">打卡总数（次）</div>
          <div class="value">{{ overview.checkCount ?? 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #a55eea, #c489f0)">
          <div class="label">累计时长（分钟）</div>
          <div class="value">{{ overview.totalDuration ?? 0 }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #e74c3c, #f1948a)">
          <div class="label">累计卡路里（千卡）</div>
          <div class="value">{{ overview.totalCalories ?? 0 }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 用户运动排行 -->
    <div class="card" style="margin-top: 16px">
      <div class="card-title">用户运动排行（按累计时长 Top 10）</div>
      <el-table :data="overview.topUsers || []" v-loading="loading" border stripe>
        <el-table-column type="index" label="排名" width="70" />
        <el-table-column prop="nickname" label="用户昵称" />
        <el-table-column prop="username" label="用户名" width="140" />
        <el-table-column prop="checkCount" label="打卡次数" width="110" />
        <el-table-column prop="totalDuration" label="累计时长(分钟)" width="140" />
        <el-table-column prop="totalCalories" label="累计卡路里(千卡)" width="150" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { getOverview } from '../api/stats'

const overview = reactive({})
const loading = ref(false)

const load = async () => {
  loading.value = true
  try {
    Object.assign(overview, await getOverview())
  } catch (e) {
    // 错误提示已在拦截器统一处理
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.card-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
}
</style>
