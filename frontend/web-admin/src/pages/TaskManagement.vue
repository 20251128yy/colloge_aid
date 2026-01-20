<template>
  <div class="task-management">
    <h2>任务管理</h2>
    
    <!-- 搜索和筛选区域 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <el-form :model="searchForm" inline>
        <el-form-item label="任务标题">
          <el-input v-model="searchForm.title" placeholder="请输入任务标题" clearable />
        </el-form-item>
        <el-form-item label="任务类型">
          <el-select v-model="searchForm.type" placeholder="全部" clearable>
            <el-option label="学习任务" value="study" />
            <el-option label="运动任务" value="exercise" />
            <el-option label="生活任务" value="life" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable>
            <el-option label="待完成" value="pending" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 任务列表 -->
    <el-card shadow="hover">
      <el-table :data="tasks" stripe style="width: 100%">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="任务ID" width="80" />
        <el-table-column prop="title" label="任务标题" min-width="200" />
        <el-table-column prop="type" label="任务类型" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.type === 'study'" type="primary">学习任务</el-tag>
            <el-tag v-else-if="scope.row.type === 'exercise'" type="success">运动任务</el-tag>
            <el-tag v-else-if="scope.row.type === 'life'" type="warning">生活任务</el-tag>
            <el-tag v-else type="info">其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="发布人" />
        <el-table-column prop="participants" label="参与人数" width="100" />
        <el-table-column prop="pointReward" label="积分奖励" width="100" />
        <el-table-column prop="status" label="任务状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 'pending'" type="warning">待完成</el-tag>
            <el-tag v-else-if="scope.row.status === 'completed'" type="success">已完成</el-tag>
            <el-tag v-else-if="scope.row.status === 'cancelled'" type="danger">已取消</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deadline" label="截止时间" width="180" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="danger" size="small" @click="handleCancel(scope.row)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </el-card>

    <!-- 任务详情对话框 -->
    <el-dialog v-model="dialogVisible" title="任务详情" width="50%">
      <el-form :model="currentTask" label-position="top">
        <el-form-item label="任务ID">
          <el-input v-model="currentTask.id" disabled />
        </el-form-item>
        <el-form-item label="任务标题">
          <el-input v-model="currentTask.title" disabled />
        </el-form-item>
        <el-form-item label="任务描述">
          <el-input v-model="currentTask.description" type="textarea" disabled :rows="3" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="任务类型">
              <el-tag v-if="currentTask.type === 'study'" type="primary">学习任务</el-tag>
              <el-tag v-else-if="currentTask.type === 'exercise'" type="success">运动任务</el-tag>
              <el-tag v-else-if="currentTask.type === 'life'" type="warning">生活任务</el-tag>
              <el-tag v-else type="info">其他</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="积分奖励">
              <el-input v-model="currentTask.pointReward" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="发布人">
              <el-input v-model="currentTask.creator" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参与人数">
              <el-input v-model="currentTask.participants" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="任务状态">
              <el-tag v-if="currentTask.status === 'pending'" type="warning">待完成</el-tag>
              <el-tag v-else-if="currentTask.status === 'completed'" type="success">已完成</el-tag>
              <el-tag v-else-if="currentTask.status === 'cancelled'" type="danger">已取消</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="截止时间">
              <el-input v-model="currentTask.deadline" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="创建时间">
          <el-input v-model="currentTask.createTime" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getTasks } from '../api/task'

// 搜索条件
const searchForm = reactive({
  title: '',
  type: '',
  status: ''
})

// 表格数据
const tasks = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 任务详情对话框
const dialogVisible = ref(false)
const currentTask = ref({})

// 模拟任务数据
const mockTasks = []
const types = ['study', 'exercise', 'life', 'other']
const typeLabels = ['学习任务', '运动任务', '生活任务', '其他']
const statuses = ['pending', 'completed', 'cancelled']

for (let i = 1; i <= 30; i++) {
  const typeIndex = Math.floor(Math.random() * types.length)
  mockTasks.push({
    id: i,
    title: `任务${i} - ${typeLabels[typeIndex]}`,
    description: `这是${typeLabels[typeIndex]}的详细描述，包含任务要求、完成标准等信息。`,
    type: types[typeIndex],
    creator: `用户${Math.floor(Math.random() * 100 + 1)}`,
    participants: Math.floor(Math.random() * 50),
    pointReward: Math.floor(Math.random() * 100 + 50),
    status: statuses[Math.floor(Math.random() * statuses.length)],
    deadline: new Date(Date.now() + Math.random() * 30 * 24 * 60 * 60 * 1000).toLocaleString('zh-CN'),
    createTime: new Date(Date.now() - Math.random() * 15 * 24 * 60 * 60 * 1000).toLocaleString('zh-CN')
  })
}

// 加载任务列表
const loadTasks = () => {
  // 模拟API请求
  tasks.value = mockTasks
    .filter(task => {
      // 按标题过滤
      const title = searchForm.title.toLowerCase()
      if (title && !task.title.toLowerCase().includes(title)) {
        return false
      }
      // 按类型过滤
      if (searchForm.type && task.type !== searchForm.type) {
        return false
      }
      // 按状态过滤
      if (searchForm.status && task.status !== searchForm.status) {
        return false
      }
      return true
    })
    .slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
  
  total.value = mockTasks.length
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadTasks()
}

// 重置
const handleReset = () => {
  searchForm.title = ''
  searchForm.type = ''
  searchForm.status = ''
  currentPage.value = 1
  loadTasks()
}

// 导出
const handleExport = () => {
  ElMessage.success('任务数据已导出')
}

// 查看任务详情
const handleView = (task) => {
  currentTask.value = { ...task }
  dialogVisible.value = true
}

// 取消任务
const handleCancel = (task) => {
  ElMessage.success(`任务${task.title}已取消`)
  task.status = 'cancelled'
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  loadTasks()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadTasks()
}

// 初始化加载数据
loadTasks()
</script>

<style scoped>
.task-management {
  padding: 20px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>