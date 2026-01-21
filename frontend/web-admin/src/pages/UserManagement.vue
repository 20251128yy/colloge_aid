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
        <el-form-item label="用户角色">
          <el-select v-model="searchForm.role" placeholder="全部" clearable>
            <el-option label="需求方" value="1" />
            <el-option label="派送方" value="2" />
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
        <el-table-column prop="currentRole" label="用户角色" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.currentRole === 1" type="info">需求方</el-tag>
            <el-tag v-else-if="scope.row.currentRole === 2" type="primary">派送方</el-tag>
          </template>
        </el-table-column>
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
        <el-table-column label="操作" width="250">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="warning" size="small" @click="handleUpdateStatus(scope.row)">状态</el-button>
            <el-button type="info" size="small" @click="handleSwitchRole(scope.row)">
              {{ scope.row.currentRole === 1 ? '切换为派送方' : '切换为需求方' }}
            </el-button>
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
import { getUsers, updateUserStatus, updateUserRole } from '../api/user'

// 搜索条件
const searchForm = reactive({
  keyword: '',
  status: '',
  auditStatus: '',
  role: ''
})

// 表格数据
const users = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 用户详情对话框
const dialogVisible = ref(false)
const currentUser = ref({})

// 加载用户列表
const loadUsers = async () => {
  try {
    // 构建请求参数
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchForm.keyword,
      status: searchForm.status || undefined,
      auditStatus: searchForm.auditStatus || undefined,
      role: searchForm.role || undefined
    }

    // 调用真实API
    const response = await getUsers(params)
    users.value = response.records || []
    total.value = response.total || 0
  } catch (error) {
    console.error('加载用户列表失败:', error)
    ElMessage.error('加载用户列表失败，请重试')
  }
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
  searchForm.role = ''
  currentPage.value = 1
  loadUsers()
}

// 查看用户详情
const handleView = (user) => {
  currentUser.value = { ...user }
  dialogVisible.value = true
}

// 更新用户状态
const handleUpdateStatus = async (user) => {
  const newStatus = user.status === 1 ? 0 : 1
  try {
    await updateUserStatus(user.id, newStatus)
    ElMessage.success(`用户${user.name}状态已更新为${newStatus === 1 ? '启用' : '禁用'}`)
    user.status = newStatus
  } catch (error) {
    console.error('更新用户状态失败:', error)
    ElMessage.error('更新用户状态失败，请重试')
  }
}

// 删除用户
const handleDelete = async (user) => {
  try {
    // 这里需要添加删除用户的API调用
    // await deleteUser(user.id)
    ElMessage.success(`用户${user.name}已删除`)
    // 重新加载用户列表
    loadUsers()
  } catch (error) {
    console.error('删除用户失败:', error)
    ElMessage.error('删除用户失败，请重试')
  }
}

// 切换用户角色
const handleSwitchRole = async (user) => {
  const newRole = user.currentRole === 1 ? 2 : 1
  try {
    await updateUserRole(user.id, newRole)
    ElMessage.success(`用户${user.name}已切换为${newRole === 1 ? '需求方' : '派送方'}`)
    user.currentRole = newRole
  } catch (error) {
    console.error('切换用户角色失败:', error)
    ElMessage.error('切换用户角色失败，请重试')
  }
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