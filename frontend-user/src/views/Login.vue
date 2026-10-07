<template>
  <div class="login-wrap">
    <div class="login-box">
      <h2 class="title">健康打卡与运动达标管理系统</h2>
      <p class="subtitle">登录账号</p>
      <el-form ref="formRef" :model="form" :rules="rules" size="large">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
            :prefix-icon="Lock"
            @keyup.enter="onSubmit"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" :loading="loading" @click="onSubmit">登 录</el-button>
        </el-form-item>
      </el-form>
      <div class="tips">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </div>
      <div class="demo">演示账号：user / 123456（普通用户）</div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '../api/auth'
import { useUserStore } from '../store'

const router = useRouter()
const store = useUserStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const onSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const data = await login(form)
      store.setLogin(data.token, data.user)
      ElMessage.success('登录成功')
      router.push('/dashboard')
    } catch (e) {
      // 错误提示已在拦截器统一处理
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-wrap {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #3a8ee6 0%, #6ac1b8 100%);
}
.login-box {
  width: 380px;
  padding: 40px 36px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
}
.title {
  font-size: 20px;
  text-align: center;
  color: #333;
}
.subtitle {
  text-align: center;
  color: #999;
  margin: 8px 0 24px;
}
.tips {
  text-align: center;
  font-size: 14px;
  color: #666;
}
.tips a {
  color: #3a8ee6;
  text-decoration: none;
}
.demo {
  margin-top: 12px;
  text-align: center;
  font-size: 12px;
  color: #bbb;
}
</style>
