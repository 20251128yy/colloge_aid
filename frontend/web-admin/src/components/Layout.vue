<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="200px" class="aside">
      <div class="logo">
        <h3>校园互助平台</h3>
      </div>
      <el-menu
        default-active="/"
        class="el-menu-vertical-demo"
        :router="true"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="/">
          <el-icon><Management /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/tasks">
          <el-icon><DocumentChecked /></el-icon>
          <span>任务管理</span>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据统计</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区域 -->
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <div class="user-info">
          <span>管理员</span>
          <el-button type="text" @click="handleLogout" class="logout-btn">
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </el-button>
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Management, User, DocumentChecked, DataAnalysis, SwitchButton } from '@element-plus/icons-vue'

const router = useRouter()

const handleLogout = () => {
  localStorage.removeItem('adminToken')
  localStorage.removeItem('isAdminLoggedIn')
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.aside {
  background-color: #545c64;
  border-right: 1px solid #ddd;
}

.logo {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #666;
}

.logo h3 {
  color: #fff;
  margin: 0;
  font-size: 18px;
}

.header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  background-color: #fff;
  border-bottom: 1px solid #ddd;
  padding: 0 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.logout-btn {
  color: #606266;
}

.main {
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
}
</style>