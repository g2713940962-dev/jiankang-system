<template>
  <div class="page-container">
    <div class="card">
      <div class="toolbar">
        <el-input v-model="query.keyword" placeholder="按用户名/昵称搜索" clearable style="width: 240px" @keyup.enter="onSearch" />
        <el-button type="primary" :icon="Search" @click="onSearch">查询</el-button>
      </div>

      <el-table :data="list" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="username" label="用户名" width="140" />
        <el-table-column prop="nickname" label="昵称" width="140" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.role === 'ADMIN' ? 'warning' : 'primary'">
              {{ row.role === 'ADMIN' ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="170" />
        <el-table-column label="操作" width="210" fixed="right">
          <template #default="{ row }">
            <el-button size="small" link :type="row.status === 1 ? 'warning' : 'success'" @click="onToggleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button size="small" link type="primary" @click="onResetPwd(row)">重置密码</el-button>
            <el-button size="small" link type="danger" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        style="margin-top: 16px; justify-content: flex-end"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        :current-page="query.page"
        :page-size="query.size"
        :page-sizes="[10, 20, 50]"
        @current-change="(p) => { query.page = p; load() }"
        @size-change="(s) => { query.size = s; query.page = 1; load() }"
      />
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { pageUser, updateUserStatus, resetUserPassword, deleteUser } from '../../api/user'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ page: 1, size: 10, keyword: '' })

const load = async () => {
  loading.value = true
  try {
    const data = await pageUser(query)
    list.value = data.records || []
    total.value = data.total || 0
  } catch (e) {
    // 已统一处理
  } finally {
    loading.value = false
  }
}

const onSearch = () => {
  query.page = 1
  load()
}

const onToggleStatus = (row) => {
  const target = row.status === 1 ? 0 : 1
  const text = target === 1 ? '启用' : '禁用'
  ElMessageBox.confirm(`确定${text}用户「${row.username}」吗？`, '提示', { type: 'warning' }).then(async () => {
    await updateUserStatus(row.id, target)
    ElMessage.success(`${text}成功`)
    load()
  }).catch(() => {})
}

const onResetPwd = (row) => {
  ElMessageBox.confirm(`确定将用户「${row.username}」的密码重置为 123456 吗？`, '提示', { type: 'warning' }).then(async () => {
    await resetUserPassword(row.id)
    ElMessage.success('密码已重置为 123456')
  }).catch(() => {})
}

const onDelete = (row) => {
  ElMessageBox.confirm(`确定删除用户「${row.username}」吗？此操作不可恢复。`, '提示', { type: 'warning' }).then(async () => {
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    load()
  }).catch(() => {})
}

onMounted(load)
</script>
