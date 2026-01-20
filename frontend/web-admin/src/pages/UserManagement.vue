<template>
  <div class="user-management">
    <h2>用户管理</h2>
    
    <!-- 搜索和筛选区域 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <el-form :model="searchForm" inline>
        <el-form-item label="搜索">
          <el-input v-model="searchForm.keyword" placeholder="姓名/手机号/学号" clearable />
        </el-form-item>
        <el-form-item label="用户状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable>
            <el-option label="启用" value="1" />
            <el-option label="禁用" value="0" />
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
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 用户列表 -->
    <el-card shadow="hover">
      <el-table :data="users" stripe style="width: 100%">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="用户ID" width="80" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column prop="pointBalance" label="积分余额" width="100" />
        <el-table-column prop="auditStatus" label="审核状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.auditStatus === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="scope.row.auditStatus === 1" type="success">审核通过</el-tag>
            <el-tag v-else-if="scope.row.auditStatus === 2" type="danger">审核拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="用户状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="warning" size="small" @click="handleUpdateStatus(scope.row)">状态</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 用户详情对话框 -->
    <el-dialog v-model="dialogVisible" title="用户详情" width="50%">
      <el-form :model="currentUser" label-position="top">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户ID">
              <el-input v-model="currentUser.id" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名">
              <el-input v-model="currentUser.name" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号">
              <el-input v-model="currentUser.phone" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="currentUser.email" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号">
              <el-input v-model="currentUser.studentId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="积分余额">
              <el-input v-model="currentUser.pointBalance" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="审核状态">
              <el-tag v-if="currentUser.auditStatus === 0" type="warning">待审核</el-tag>
              <el-tag v-else-if="currentUser.auditStatus === 1" type="success">审核通过</el-tag>
              <el-tag v-else-if="currentUser.auditStatus === 2" type="danger">审核拒绝</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户状态">
              <el-tag v-if="currentUser.status === 1" type="success">启用</el-tag>
              <el-tag v-else type="danger">禁用</el-tag>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="注册时间">
          <el-input v-model="currentUser.createTime" disabled />
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
import { getUsers } from '../api/user'

// 搜索条件
const searchForm = reactive({
  keyword: '',
  status: '',
  auditStatus: ''
})

// 表格数据
const users = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 用户详情对话框
const dialogVisible = ref(false)
const currentUser = ref({})

// 模拟用户数据
const mockUsers = []
for (let i = 1; i <= 50; i++) {
  mockUsers.push({
    id: i,
    name: `用户${i}`,
    phone: `138001380${i.toString().padStart(2, '0')}`,
    email: `user${i}@example.com`,
    studentId: `2023${i.toString().padStart(4, '0')}`,
    pointBalance: Math.floor(Math.random() * 1000),
    auditStatus: Math.floor(Math.random() * 3),
    status: Math.random() > 0.2 ? 1 : 0,
    createTime: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toLocaleString('zh-CN')
  })
}

// 加载用户列表
const loadUsers = () => {
  // 模拟API请求
  users.value = mockUsers
    .filter(user => {
      // 按关键词过滤
      const keyword = searchForm.keyword.toLowerCase()
      if (keyword && !user.name.toLowerCase().includes(keyword) && 
          !user.phone.includes(keyword) && !user.studentId.includes(keyword)) {
        return false
      }
      // 按状态过滤
      if (searchForm.status !== '' && user.status !== parseInt(searchForm.status)) {
        return false
      }
      // 按审核状态过滤
      if (searchForm.auditStatus !== '' && user.auditStatus !== parseInt(searchForm.auditStatus)) {
        return false
      }
      return true
    })
    .slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
  
  total.value = mockUsers.length
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadUsers()
}

// 重置
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.status = ''
  searchForm.auditStatus = ''
  currentPage.value = 1
  loadUsers()
}

// 查看用户详情
const handleView = (user) => {
  currentUser.value = { ...user }
  dialogVisible.value = true
}

// 更新用户状态
const handleUpdateStatus = (user) => {
  const newStatus = user.status === 1 ? 0 : 1
  ElMessage.success(`用户${user.name}状态已更新为${newStatus === 1 ? '启用' : '禁用'}`)
  user.status = newStatus
}

// 删除用户
const handleDelete = (user) => {
  ElMessage.success(`用户${user.name}已删除`)
  users.value = users.value.filter(u => u.id !== user.id)
  total.value--
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  loadUsers()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadUsers()
}

// 初始化加载数据
loadUsers()
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>