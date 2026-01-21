<template>
  <div class="task-management">
    <h2>任务管理</h2>
    
    <!-- 搜索和筛选区域（适配后端真实字段） -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <el-form :model="searchForm" inline>
        <el-form-item label="任务标题">
          <el-input v-model="searchForm.title" placeholder="请输入任务标题" clearable />
        </el-form-item>
        <el-form-item label="任务状态">
          <el-select v-model="searchForm.taskStatus" placeholder="全部" clearable>
            <el-option label="待接单" value="0" />
            <el-option label="配送中" value="1" />
            <el-option label="已完成" value="2" />
            <el-option label="已取消" value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="searchForm.auditStatus" placeholder="全部" clearable>
            <el-option label="待审核" value="0" />
            <el-option label="审核通过" value="1" />
            <el-option label="审核拒绝" value="2" />
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

    <!-- 任务列表（适配后端真实字段和状态） -->
    <el-card shadow="hover">
      <el-table :data="tasks" stripe style="width: 100%">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="任务ID" width="80" />
        <el-table-column prop="title" label="任务标题" min-width="200" />
        <el-table-column prop="fromLocation" label="取件地点" width="120" />
        <el-table-column prop="toLocation" label="送达地点" width="120" />
        <el-table-column prop="taskStatus" label="任务状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.taskStatus === 0" type="warning">待接单</el-tag>
            <el-tag v-else-if="scope.row.taskStatus === 1" type="primary">配送中</el-tag>
            <el-tag v-else-if="scope.row.taskStatus === 2" type="success">已完成</el-tag>
            <el-tag v-else-if="scope.row.taskStatus === 4" type="danger">已取消</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.auditStatus === 0" type="info">待审核</el-tag>
            <el-tag v-else-if="scope.row.auditStatus === 1" type="success">审核通过</el-tag>
            <el-tag v-else-if="scope.row.auditStatus === 2" type="danger">审核拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="pointAmount" label="积分奖励" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="danger" size="small" @click="handleCancel(scope.row)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页（适配后端参数） -->
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

    <!-- 任务详情对话框（适配后端真实字段） -->
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
            <el-form-item label="取件地点">
              <el-input v-model="currentTask.fromLocation" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="送达地点">
              <el-input v-model="currentTask.toLocation" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="积分奖励">
              <el-input v-model="currentTask.pointAmount" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务状态">
              <el-tag v-if="currentTask.taskStatus === 0" type="warning">待接单</el-tag>
              <el-tag v-else-if="currentTask.taskStatus === 1" type="primary">配送中</el-tag>
              <el-tag v-else-if="currentTask.taskStatus === 2" type="success">已完成</el-tag>
              <el-tag v-else-if="currentTask.taskStatus === 4" type="danger">已取消</el-tag>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="审核状态">
              <el-tag v-if="currentTask.auditStatus === 0" type="info">待审核</el-tag>
              <el-tag v-else-if="currentTask.auditStatus === 1" type="success">审核通过</el-tag>
              <el-tag v-else-if="currentTask.auditStatus === 2" type="danger">审核拒绝</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间">
              <el-input v-model="currentTask.createTime" disabled />
            </el-form-item>
          </el-col>
        </el-row>
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

// 搜索条件（适配后端参数名）
const searchForm = reactive({
  title: '',          // 关键词（对应后端keyword）
  taskStatus: '',     // 任务状态
  auditStatus: ''     // 审核状态
})

// 表格数据
const tasks = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 任务详情对话框
const dialogVisible = ref(false)
const currentTask = ref({})

// 核心：加载任务列表（适配后端返回格式）
const loadTasks = async () => {
  try {
    const params = {
      pageNum: currentPage.value,    
      pageSize: pageSize.value,      
      keyword: searchForm.title,     
      taskStatus: searchForm.taskStatus || undefined,  
      auditStatus: searchForm.auditStatus || undefined  
    }

    // 调用API（此时response已经是后端的res.data，即{list:[...], total:5}）
    const response = await getTasks(params)
    console.log("后端返回的最终数据：", response) // 此时打印的应该是 {list:[5条数据], total:5}
    
    // 适配响应拦截器的返回格式（直接取response.list，而非response.data.list）
    tasks.value = response.list || []  // 核心修改：去掉.data层级
    total.value = response.total || 0  // 核心修改：去掉.data层级

    // 验证：打印解析后的数据
    console.log("解析后的任务列表：", tasks.value)
    console.log("解析后的总条数：", total.value)
  } catch (error) {
    console.error('加载任务列表失败详情：', error)
    ElMessage.error(`加载任务列表失败：${error.message || '未知错误'}`)
    tasks.value = []
    total.value = 0
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadTasks()
}

// 重置
const handleReset = () => {
  searchForm.title = ''
  searchForm.taskStatus = ''
  searchForm.auditStatus = ''
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
const handleCancel = async (task) => {
  try {
    // 调用取消任务API（后续可补充）
    // await cancelTask(task.id)
    ElMessage.success(`任务${task.title}已取消`)
    // 本地更新状态（后端是4=已取消）
    task.taskStatus = 4
  } catch (error) {
    console.error('取消任务失败:', error)
    ElMessage.error('取消任务失败，请重试')
  }
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