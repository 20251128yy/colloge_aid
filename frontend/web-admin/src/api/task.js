import request from '../utils/request'

// 获取任务列表
export const getTasks = (params) => {
  return request.get('/admin/tasks', { params })
}

// 获取任务详情
export const getTaskById = (id) => {
  return request.get(`/admin/tasks/${id}`)
}

// 更新任务状态
export const updateTaskStatus = (id, status) => {
  return request.put(`/admin/tasks/${id}/status`, { status })
}

// 删除任务
export const deleteTask = (id) => {
  return request.delete(`/admin/tasks/${id}`)
}

// 获取任务统计数据
export const getTaskStatistics = () => {
  return request.get('/admin/tasks/statistics')
}

// 导出任务数据
export const exportTasks = (params) => {
  return request.get('/admin/tasks/export', { params, responseType: 'blob' })
}