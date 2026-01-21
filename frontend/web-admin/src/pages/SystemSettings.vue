<template>
  <div class="system-settings-container">
    <h2>系统设置</h2>
    
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <template #header>
        <div class="card-header">
          <span>基础配置</span>
        </div>
      </template>
      <el-form :model="basicSettings" label-width="120px">
        <el-form-item label="系统名称">
          <el-input v-model="basicSettings.systemName" placeholder="请输入系统名称" />
        </el-form-item>
        <el-form-item label="系统版本">
          <el-input v-model="basicSettings.systemVersion" placeholder="请输入系统版本" />
        </el-form-item>
        <el-form-item label="联系邮箱">
          <el-input v-model="basicSettings.contactEmail" placeholder="请输入联系邮箱" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="basicSettings.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="系统状态">
          <el-switch v-model="basicSettings.systemStatus" active-text="启用" inactive-text="禁用" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveBasicSettings">保存配置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <template #header>
        <div class="card-header">
          <span>积分配置</span>
        </div>
      </template>
      <el-form :model="pointSettings" label-width="120px">
        <el-form-item label="注册送积分">
          <el-input-number v-model="pointSettings.registerPoints" :min="0" :max="1000" placeholder="请输入注册送积分" />
        </el-form-item>
        <el-form-item label="完成任务积分">
          <el-input-number v-model="pointSettings.taskCompletePoints" :min="0" :max="1000" placeholder="请输入完成任务积分" />
        </el-form-item>
        <el-form-item label="任务超时扣分">
          <el-input-number v-model="pointSettings.taskTimeoutPoints" :min="0" :max="1000" placeholder="请输入任务超时扣分" />
        </el-form-item>
        <el-form-item label="积分兑换比例">
          <el-input-number v-model="pointSettings.pointExchangeRate" :min="0" :max="100" :step="0.1" placeholder="请输入积分兑换比例" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="savePointSettings">保存配置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>安全配置</span>
        </div>
      </template>
      <el-form :model="securitySettings" label-width="120px">
        <el-form-item label="登录失败次数限制">
          <el-input-number v-model="securitySettings.loginFailLimit" :min="0" :max="10" placeholder="请输入登录失败次数限制" />
        </el-form-item>
        <el-form-item label="密码复杂度要求">
          <el-select v-model="securitySettings.passwordComplexity" placeholder="请选择密码复杂度要求">
            <el-option label="低" value="low" />
            <el-option label="中" value="medium" />
            <el-option label="高" value="high" />
          </el-select>
        </el-form-item>
        <el-form-item label="Token过期时间（小时）">
          <el-input-number v-model="securitySettings.tokenExpireHours" :min="1" :max="720" placeholder="请输入Token过期时间" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveSecuritySettings">保存配置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 基础配置
const basicSettings = ref({
  systemName: '校园互助平台',
  systemVersion: '1.0.0',
  contactEmail: 'support@campus-help.com',
  contactPhone: '12345678901',
  systemStatus: true
})

// 积分配置
const pointSettings = ref({
  registerPoints: 100,
  taskCompletePoints: 10,
  taskTimeoutPoints: 5,
  pointExchangeRate: 10
})

// 安全配置
const securitySettings = ref({
  loginFailLimit: 5,
  passwordComplexity: 'medium',
  tokenExpireHours: 24
})

// 保存基础配置
const saveBasicSettings = async () => {
  try {
    // 这里可以调用API保存配置
    console.log('保存基础配置:', basicSettings.value)
    ElMessage.success('基础配置保存成功')
  } catch (error) {
    console.error('保存基础配置失败:', error)
    ElMessage.error('保存基础配置失败，请重试')
  }
}

// 保存积分配置
const savePointSettings = async () => {
  try {
    // 这里可以调用API保存配置
    console.log('保存积分配置:', pointSettings.value)
    ElMessage.success('积分配置保存成功')
  } catch (error) {
    console.error('保存积分配置失败:', error)
    ElMessage.error('保存积分配置失败，请重试')
  }
}

// 保存安全配置
const saveSecuritySettings = async () => {
  try {
    // 这里可以调用API保存配置
    console.log('保存安全配置:', securitySettings.value)
    ElMessage.success('安全配置保存成功')
  } catch (error) {
    console.error('保存安全配置失败:', error)
    ElMessage.error('保存安全配置失败，请重试')
  }
}

onMounted(() => {
  // 这里可以调用API获取配置
  console.log('加载系统设置')
})
</script>

<style scoped>
.system-settings-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-form-item {
  margin-bottom: 20px;
}
</style>