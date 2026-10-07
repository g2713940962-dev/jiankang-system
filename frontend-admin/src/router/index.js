import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue'), meta: { title: '登录' } },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/overview',
    children: [
      { path: 'overview', component: () => import('../views/Dashboard.vue'), meta: { title: '数据概览' } },
      { path: 'users', component: () => import('../views/UserManage.vue'), meta: { title: '用户管理' } }
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/overview' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局路由守卫：登录校验 + 角色校验（仅管理员）
router.beforeEach((to, from, next) => {
  const store = useUserStore()
  document.title = (to.meta.title ? to.meta.title + ' - ' : '') + '健康打卡系统 - 管理端'
  if (to.path === '/login') {
    next()
    return
  }
  if (!store.isLogin || !store.isAdmin) {
    next('/login')
    return
  }
  next()
})

export default router
