<template>
  <div class="page-container">
    <div class="card" style="max-width: 720px">
      <div class="chart-title">运动目标设置</div>
      <el-alert
        title="设置每周 / 每月的运动目标，系统会结合打卡数据自动统计达成进度，并在未达标时通过定时任务提醒你。"
        type="info"
        :closable="false"
        style="margin-bottom: 20px"
      />
      <el-form :model="form" label-width="160px" v-loading="loading">
        <el-divider content-position="left">每周目标</el-divider>
        <el-form-item label="每周运动天数">
          <el-input-number v-model="form.weekDays" :min="0" :max="7" />
          <span class="unit">天</span>
        </el-form-item>
        <el-form-item label="每周运动时长">
          <el-input-number v-model="form.weekDuration" :min="0" :step="10" />
          <span class="unit">分钟</span>
        </el-form-item>
        <el-form-item label="每周消耗卡路里">
          <el-input-number v-model="form.weekCalories" :min="0" :step="50" />
          <span class="unit">千卡</span>
        </el-form-item>

        <el-divider content-position="left">每月目标</el-divider>
        <el-form-item label="每月运动天数">
          <el-input-number v-model="form.monthDays" :min="0" :max="31" />
          <span class="unit">天</span>
        </el-form-item>
        <el-form-item label="每月运动时长">
          <el-input-number v-model="form.monthDuration" :min="0" :step="50" />
          <span class="unit">分钟</span>
        </el-form-item>
        <el-form-item label="每月消耗卡路里">
          <el-input-number v-model="form.monthCalories" :min="0" :step="100" />
          <span class="unit">千卡</span>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="saving" @click="onSave">保存目标</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getGoal, saveGoal } from '../api/goal'

const loading = ref(false)
const saving = ref(false)

const form = reactive({
  weekDays: 0,
  weekDuration: 0,
  weekCalories: 0,
  monthDays: 0,
  monthDuration: 0,
  monthCalories: 0
})

const load = async () => {
  loading.value = true
  try {
    const data = await getGoal()
    Object.assign(form, {
      weekDays: data.weekDays ?? 0,
      weekDuration: data.weekDuration ?? 0,
      weekCalories: data.weekCalories ?? 0,
      monthDays: data.monthDays ?? 0,
      monthDuration: data.monthDuration ?? 0,
      monthCalories: data.monthCalories ?? 0
    })
  } catch (e) {
    // 已统一处理
  } finally {
    loading.value = false
  }
}

const onSave = async () => {
  saving.value = true
  try {
    await saveGoal(form)
    ElMessage.success('目标保存成功')
  } catch (e) {
    // 已统一处理
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.chart-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
}
.unit {
  margin-left: 8px;
  color: #999;
}
</style>
