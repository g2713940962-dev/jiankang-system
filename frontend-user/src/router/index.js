import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue'), meta: { title: '登录' } },
  { path: '/register', component: () => import('../views/Register.vue'), meta: { title: '注册' } },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '数据看板' } },
      { path: 'check', component: () => import('../views/CheckRecord.vue'), meta: { title: '打卡记录' } },
      { path: 'goal', component: () => import('../views/Goal.vue'), meta: { title: '目标设置' } },
      { path: 'notice', component: () => import('../views/Notice.vue'), meta: { title: '消息提醒' } },
      { path: 'profile', component: () => import('../views/Profile.vue'), meta: { title: '个人中心' } }
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/dashboard' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局路由守卫：登录校验 + 角色校验
router.beforeEach((to, from, next) => {
  const store = useUserStore()
  document.title = (to.meta.title ? to.meta.title + ' - ' : '') + '健康打卡与运动达标管理系统'
  if (to.path === '/login' || to.path === '/register') {
    next()
    return
  }
  if (!store.isLogin) {
    next('/login')
    return
  }
  next()
})

export default router
