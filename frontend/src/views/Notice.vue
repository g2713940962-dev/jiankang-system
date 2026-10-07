<template>
  <div class="page-container">
    <div class="card">
      <div class="toolbar">
        <div class="chart-title" style="margin: 0">消息提醒</div>
        <div style="flex: 1"></div>
        <el-button type="primary" plain @click="onReadAll">全部已读</el-button>
      </div>

      <el-table :data="list" v-loading="loading" border stripe>
        <el-table-column prop="title" label="标题" width="200">
          <template #default="{ row }">
            <span :style="{ fontWeight: row.isRead === 0 ? 600 : 400 }">{{ row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="内容" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === 'REMIND' ? 'warning' : 'info'">
              {{ row.type === 'REMIND' ? '提醒' : '系统' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="时间" width="170" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag v-if="row.isRead === 1" type="success" size="small">已读</el-tag>
            <el-tag v-else type="danger" size="small">未读</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button v-if="row.isRead === 0" size="small" link type="primary" @click="onRead(row)">标记已读</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        style="margin-top: 16px; justify-content: flex-end"
        background
        layout="total, prev, pager, next"
        :total="total"
        :current-page="query.page"
        :page-size="query.size"
        @current-change="(p) => { query.page = p; load() }"
      />
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { pageNotice, readNotice, readAllNotice } from '../api/notice'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ page: 1, size: 10 })

const load = async () => {
  loading.value = true
  try {
    const data = await pageNotice(query)
    list.value = data.records || []
    total.value = data.total || 0
  } catch (e) {
    // 已统一处理
  } finally {
    loading.value = false
  }
}

const onRead = async (row) => {
  await readNotice(row.id)
  ElMessage.success('已标记为已读')
  load()
}

const onReadAll = async () => {
  await readAllNotice()
  ElMessage.success('已全部标记为已读')
  load()
}

onMounted(load)
</script>

<style scoped>
.chart-title {
  font-size: 16px;
  font-weight: 600;
}
</style>
