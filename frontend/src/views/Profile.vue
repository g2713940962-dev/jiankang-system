<template>
  <div class="page-container">
    <el-row :gutter="16">
      <el-col :span="12">
        <div class="card">
          <div class="chart-title">基本信息</div>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
            <el-descriptions-item label="昵称">{{ user.nickname || '-' }}</el-descriptions-item>
            <el-descriptions-item label="角色">
              <el-tag :type="user.role === 'ADMIN' ? 'warning' : 'primary'">
                {{ user.role === 'ADMIN' ? '管理员' : '普通用户' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="手机号">{{ user.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="注册时间">{{ user.createTime || '-' }}</el-descriptions-item>
          </el-descriptions>
          <el-button type="primary" style="margin-top: 16px" @click="openEdit">编辑资料</el-button>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card">
          <div class="chart-title">修改密码</div>
          <el-form ref="pwdRef" :model="pwdForm" :rules="pwdRules" label-width="90px">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="pwdForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="pwdForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="pwdSaving" @click="onChangePwd">确认修改</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>

    <!-- 编辑资料对话框 -->
    <el-dialog v-model="editVisible" title="编辑资料" width="420px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="昵称">
          <el-input v-model="editForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.phone" placeholder="请输入手机号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" :loading="editSaving" @click="onSaveProfile">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getInfo, updateProfile, updatePassword } from '../api/auth'
import { useUserStore } from '../store'

const store = useUserStore()
const user = reactive({})
const pwdRef = ref()
const editVisible = ref(false)
const editSaving = ref(false)
const pwdSaving = ref(false)

const editForm = reactive({ nickname: '', phone: '' })
const pwdForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })

const validateConfirm = (rule, value, callback) => {
  if (value !== pwdForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度需在 6-20 位之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirm, trigger: 'blur' }
  ]
}

const load = async () => {
  try {
    const data = await getInfo()
    Object.assign(user, data)
    store.setUser(data)
  } catch (e) {
    // 已统一处理
  }
}

const openEdit = () => {
  editForm.nickname = user.nickname || ''
  editForm.phone = user.phone || ''
  editVisible.value = true
}

const onSaveProfile = async () => {
  editSaving.value = true
  try {
    const data = await updateProfile(editForm)
    Object.assign(user, data)
    store.setUser(data)
    ElMessage.success('保存成功')
    editVisible.value = false
  } catch (e) {
    // 已统一处理
  } finally {
    editSaving.value = false
  }
}

const onChangePwd = () => {
  pwdRef.value.validate(async (valid) => {
    if (!valid) return
    pwdSaving.value = true
    try {
      await updatePassword({ oldPassword: pwdForm.oldPassword, newPassword: pwdForm.newPassword })
      ElMessage.success('密码修改成功')
      pwdForm.oldPassword = ''
      pwdForm.newPassword = ''
      pwdForm.confirmPassword = ''
    } catch (e) {
      // 已统一处理
    } finally {
      pwdSaving.value = false
    }
  })
}

onMounted(load)
</script>

<style scoped>
.chart-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
}
</style>
