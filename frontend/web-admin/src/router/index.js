import { createRouter, createWebHistory } from 'vue-router'
import Login from '../pages/Login.vue'
import Layout from '../components/Layout.vue'
import Dashboard from '../pages/Dashboard.vue'
import UserManagement from '../pages/UserManagement.vue'
import TaskManagement from '../pages/TaskManagement.vue'
import Statistics from '../pages/Statistics.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: Dashboard
      },
      {
        path: '/users',
        name: 'UserManagement',
        component: UserManagement
      },
      {
        path: '/tasks',
        name: 'TaskManagement',
        component: TaskManagement
      },
      {
        path: '/statistics',
        name: 'Statistics',
        component: Statistics
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('adminToken')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router