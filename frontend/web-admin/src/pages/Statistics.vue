<template>
  <div class="statistics-container">
    <h2>数据统计</h2>
    
    <!-- 时间筛选器 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <el-form :model="filterForm" inline>
        <el-form-item label="统计周期">
          <el-select v-model="filterForm.period" placeholder="请选择统计周期">
            <el-option label="今日" value="today" />
            <el-option label="本周" value="week" />
            <el-option label="本月" value="month" />
            <el-option label="全部" value="all" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 核心数据卡片 -->
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-content">
            <div class="stat-label">活跃用户</div>
            <div class="stat-value">{{ activeUsers }}</div>
            <div class="stat-change" :class="{ 'positive': userGrowth > 0, 'negative': userGrowth < 0 }">
              <el-icon v-if="userGrowth > 0"><ArrowUp /></el-icon>
              <el-icon v-else><ArrowDown /></el-icon>
              {{ Math.abs(userGrowth) }}%
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-content">
            <div class="stat-label">任务完成率</div>
            <div class="stat-value">{{ taskCompletionRate }}%</div>
            <div class="stat-change" :class="{ 'positive': completionRateChange > 0, 'negative': completionRateChange < 0 }">
              <el-icon v-if="completionRateChange > 0"><ArrowUp /></el-icon>
              <el-icon v-else><ArrowDown /></el-icon>
              {{ Math.abs(completionRateChange) }}%
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-content">
            <div class="stat-label">新增任务</div>
            <div class="stat-value">{{ newTasks }}</div>
            <div class="stat-change" :class="{ 'positive': taskGrowth > 0, 'negative': taskGrowth < 0 }">
              <el-icon v-if="taskGrowth > 0"><ArrowUp /></el-icon>
              <el-icon v-else><ArrowDown /></el-icon>
              {{ Math.abs(taskGrowth) }}%
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-content">
            <div class="stat-label">发放积分</div>
            <div class="stat-value">{{ totalPointsIssued }}</div>
            <div class="stat-change" :class="{ 'positive': pointsGrowth > 0, 'negative': pointsGrowth < 0 }">
              <el-icon v-if="pointsGrowth > 0"><ArrowUp /></el-icon>
              <el-icon v-else><ArrowDown /></el-icon>
              {{ Math.abs(pointsGrowth) }}%
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>用户活跃度趋势</span>
            </div>
          </template>
          <div class="chart-container">
            <div class="chart-placeholder">用户活跃度趋势图</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>任务类型分布</span>
            </div>
          </template>
          <div class="chart-container">
            <div class="chart-placeholder">任务类型分布饼图</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>积分发放趋势</span>
            </div>
          </template>
          <div class="chart-container">
            <div class="chart-placeholder">积分发放趋势图</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>用户行为分析</span>
            </div>
          </template>
          <div class="chart-container">
            <div class="chart-placeholder">用户行为分析图表</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import { getPlatformStatistics } from '../api/statistics'

// 筛选条件
const filterForm = reactive({
  period: 'month'
})

// 统计数据
const activeUsers = ref(128)
const taskCompletionRate = ref(85.6)
const newTasks = ref(45)
const totalPointsIssued = ref(8900)

// 增长率数据
const userGrowth = ref(12.5)
const completionRateChange = ref(-2.3)
const taskGrowth = ref(8.7)
const pointsGrowth = ref(15.2)

// 处理筛选
const handleFilter = () => {
  ElMessage.success(`已查询${filterForm.period}的数据`)
  // 模拟数据更新
  activeUsers.value = Math.floor(Math.random() * 200 + 50)
  taskCompletionRate.value = Number((Math.random() * 30 + 70).toFixed(1))
  newTasks.value = Math.floor(Math.random() * 60 + 20)
  totalPointsIssued.value = Math.floor(Math.random() * 10000 + 5000)
}
</script>

<style scoped>
.statistics-container {
  padding: 20px;
}

.stat-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
}

.stat-change {
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.stat-change.positive {
  color: #67c23a;
}

.stat-change.negative {
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
  font-weight: bold;
  color: #303133;
}
</style>