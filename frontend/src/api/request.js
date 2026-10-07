import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store'
import router from '../router'

/**
 * Axios 实例：统一封装请求头 token、响应解包、错误提示
 */
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器：携带 token
request.interceptors.request.use(
  (config) => {
    const store = useUserStore()
    if (store.token) {
      config.headers.Authorization = 'Bearer ' + store.token
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器：统一解包 Result，成功直接返回 data
request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 200) {
      return res.data
    }
    if (res.code === 401) {
      handleUnauthorized()
    } else {
      ElMessage.error(res.message || '请求失败')
    }
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  (error) => {
    ElMessage.error(error.message || '网络异常，请稍后重试')
    return Promise.reject(error)
  }
)

function handleUnauthorized() {
  const store = useUserStore()
  store.logout()
  ElMessage.error('登录已过期，请重新登录')
  router.push('/login')
}

export default request
