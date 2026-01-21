<template>
  <div class="dashboard-container">
    <h2>仪表盘</h2>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ totalUsers }}</div>
            <div class="stat-label">总用户数</div>
          </div>
          <el-icon class="stat-icon user-icon"><User /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ pendingTasks }}</div>
            <div class="stat-label">待处理任务</div>
          </div>
          <el-icon class="stat-icon task-icon"><DocumentChecked /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ completedTasks }}</div>
            <div class="stat-label">已完成任务</div>
          </div>
          <el-icon class="stat-icon complete-icon"><CircleCheck /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ totalPoints }}</div>
            <div class="stat-label">总积分</div>
          </div>
          <el-icon class="stat-icon point-icon"><Money /></el-icon>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>用户增长趋势</span>
            </div>
          </template>
          <div class="chart-container">
            <!-- 这里可以集成ECharts或其他图表库 -->
            <div class="chart-placeholder">用户增长趋势图表</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>任务完成情况</span>
            </div>
          </template>
          <div class="chart-container">
            <!-- 这里可以集成ECharts或其他图表库 -->
            <div class="chart-placeholder">任务完成情况图表</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近待审核用户 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>最近待审核用户</span>
        </div>
      </template>
      <el-table :data="pendingUsers" stripe style="width: 100%">
        <el-table-column prop="id" label="用户ID" width="80" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column prop="auditStatus" label="状态" width="100">
          <template #default="scope">
            <el-tag type="warning">待审核</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleApprove(scope.row)">通过</el-button>
            <el-button type="danger" size="small" @click="handleReject(scope.row)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User, DocumentChecked, CircleCheck, Money } from '@element-plus/icons-vue'
import { getUsers, updateUserAuditStatus } from '../api/user'
import { getPlatformStatistics } from '../api/statistics'

// 统计数据
const totalUsers = ref(0)
const pendingTasks = ref(0)
const completedTasks = ref(0)
const totalPoints = ref(0)
const pendingUsers = ref([])

// 获取统计数据
const fetchStatistics = async () => {
  try {
    // 获取平台统计数据
    const statsResponse = await getPlatformStatistics()
    totalUsers.value = statsResponse.totalUsers || 0
    pendingTasks.value = statsResponse.pendingTasks || 0
    completedTasks.value = statsResponse.completedTasks || 0
    totalPoints.value = statsResponse.totalPoints || 0
    
    // 获取待审核用户数据
    const usersResponse = await getUsers({ auditStatus: 0, page: 1, pageSize: 10 })
    pendingUsers.value = usersResponse.records || []
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败，请重试')
  }
}

// 审核通过
const handleApprove = async (user) => {
  try {
    await updateUserAuditStatus(user.id, 1)
    ElMessage.success(`用户 ${user.name} 审核通过`)
    pendingUsers.value = pendingUsers.value.filter(u => u.id !== user.id)
  } catch (error) {
    console.error('审核用户失败:', error)
    ElMessage.error('审核用户失败，请重试')
  }
}

// 审核拒绝
const handleReject = async (user) => {
  try {
    await updateUserAuditStatus(user.id, 2)
    ElMessage.success(`用户 ${user.name} 审核拒绝`)
    pendingUsers.value = pendingUsers.value.filter(u => u.id !== user.id)
  } catch (error) {
    console.error('审核用户失败:', error)
    ElMessage.error('审核用户失败，请重试')
  }
}

onMounted(() => {
  fetchStatistics()
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.stat-card {
  position: relative;
  overflow: hidden;
}

.stat-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100px;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

.stat-icon {
  position: absolute;
  right: 20px;
  top: 20px;
  font-size: 32px;
  opacity: 0.2;
}

.user-icon {
  color: #409eff;
}

.task-icon {
  color: #67c23a;
}

.complete-icon {
  color: #e6a23c;
}

.point-icon {
  color: #f56c6c;
}

.chart-container {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart-placeholder {
  font-size: 18px;
  color: #909399;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>