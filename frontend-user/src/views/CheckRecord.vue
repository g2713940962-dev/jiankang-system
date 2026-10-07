<template>
  <div class="page-container">
    <div class="card">
      <!-- 查询工具栏 -->
      <div class="toolbar">
        <el-select v-model="query.checkType" placeholder="运动类型" clearable style="width: 140px">
          <el-option v-for="t in types" :key="t" :label="t" :value="t" />
        </el-select>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
          style="width: 260px"
        />
        <el-button type="primary" :icon="Search" @click="onSearch">查询</el-button>
        <el-button :icon="Refresh" @click="onReset">重置</el-button>
        <div style="flex: 1"></div>
        <el-button type="primary" :icon="Plus" @click="openAdd">新增打卡</el-button>
      </div>

      <!-- 今日打卡提示 -->
      <el-alert
        :title="`今日已打卡 ${todayList.length} 次`"
        type="success"
        :closable="false"
        style="margin-bottom: 12px"
      />

      <!-- 数据表格 -->
      <el-table :data="list" v-loading="loading" border stripe>
        <el-table-column prop="checkDate" label="日期" width="120" />
        <el-table-column prop="checkType" label="运动类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.checkType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="时长(分钟)" width="110" />
        <el-table-column prop="calories" label="卡路里(千卡)" width="120" />
        <el-table-column prop="stepCount" label="步数" width="100" />
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column prop="createTime" label="记录时间" width="170" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" link type="danger" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑打卡' : '新增打卡'" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="打卡日期" prop="checkDate">
          <el-date-picker v-model="form.checkDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="运动类型" prop="checkType">
          <el-select v-model="form.checkType" placeholder="请选择" style="width: 100%">
            <el-option v-for="t in types" :key="t" :label="t" :value="t" />
          </el-select>
        </el-form-item>
        <el-form-item label="运动时长" prop="duration">
          <el-input-number v-model="form.duration" :min="1" :max="1440" style="width: 100%" />
        </el-form-item>
        <el-form-item label="卡路里">
          <el-input-number v-model="form.calories" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="步数">
          <el-input-number v-model="form.stepCount" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="选填" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import { pageCheck, addCheck, updateCheck, deleteCheck, todayCheck } from '../api/check'

const types = ['晨跑', '健身', '球类', '骑行', '瑜伽', '其他']

// 获取本地时区的 yyyy-MM-dd（避免 toISOString 的 UTC 偏移）
const localToday = () => {
  const d = new Date()
  const offset = d.getTimezoneOffset()
  const local = new Date(d.getTime() - offset * 60000)
  return local.toISOString().slice(0, 10)
}

const list = ref([])
const total = ref(0)
const loading = ref(false)
const dateRange = ref([])
const todayList = ref([])
const dialogVisible = ref(false)
const saving = ref(false)
const formRef = ref()

const query = reactive({ page: 1, size: 10, checkType: '', startDate: '', endDate: '' })

const form = reactive({
  id: null,
  checkDate: '',
  checkType: '',
  duration: 30,
  calories: 0,
  stepCount: 0,
  remark: ''
})

const rules = {
  checkDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  checkType: [{ required: true, message: '请选择运动类型', trigger: 'change' }],
  duration: [{ required: true, message: '请输入运动时长', trigger: 'blur' }]
}

const load = async () => {
  loading.value = true
  try {
    const data = await pageCheck(query)
    list.value = data.records || []
    total.value = data.total || 0
  } catch (e) {
    // 已统一处理
  } finally {
    loading.value = false
  }
}

const loadToday = async () => {
  try {
    todayList.value = (await todayCheck()) || []
  } catch (e) {
    todayList.value = []
  }
}

const onSearch = () => {
  query.startDate = dateRange.value && dateRange.value[0] ? dateRange.value[0] : ''
  query.endDate = dateRange.value && dateRange.value[1] ? dateRange.value[1] : ''
  query.page = 1
  load()
}

const onReset = () => {
  query.checkType = ''
  query.startDate = ''
  query.endDate = ''
  dateRange.value = []
  query.page = 1
  load()
}

const openAdd = () => {
  Object.assign(form, { id: null, checkDate: localToday(), checkType: '', duration: 30, calories: 0, stepCount: 0, remark: '' })
  dialogVisible.value = true
}

const openEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    checkDate: row.checkDate,
    checkType: row.checkType,
    duration: row.duration,
    calories: row.calories,
    stepCount: row.stepCount,
    remark: row.remark
  })
  dialogVisible.value = true
}

const onSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    saving.value = true
    try {
      const payload = { ...form }
      delete payload.id
      if (form.id) {
        await updateCheck(form.id, payload)
        ElMessage.success('修改成功')
      } else {
        await addCheck(payload)
        ElMessage.success('打卡成功')
      }
      dialogVisible.value = false
      load()
      loadToday()
    } catch (e) {
      // 已统一处理
    } finally {
      saving.value = false
    }
  })
}

const onDelete = (row) => {
  ElMessageBox.confirm(`确定删除 ${row.checkDate} 的打卡记录吗？`, '提示', { type: 'warning' }).then(async () => {
    await deleteCheck(row.id)
    ElMessage.success('删除成功')
    load()
    loadToday()
  }).catch(() => {})
}

onMounted(() => {
  load()
  loadToday()
})
</script>
