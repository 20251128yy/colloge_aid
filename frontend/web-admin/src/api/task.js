// import request from '../utils/request'

// // 获取任务列表
// export const getTasks = (params) => {
//   return request.get('/admin/tasks', { params })
// }

// // 获取任务详情
// export const getTaskById = (id) => {
//   return request.get(`/admin/tasks/${id}`)
// }

// // 更新任务状态
// export const updateTaskStatus = (id, status) => {
//   return request.put(`/admin/tasks/${id}/status`, { status })
// }

// // 删除任务
// export const deleteTask = (id) => {
//   return request.delete(`/admin/tasks/${id}`)
// }

// // 获取任务统计数据
// export const getTaskStatistics = () => {
//   return request.get('/admin/tasks/statistics')
// }

// // 导出任务数据
// export const exportTasks = (params) => {
//   return request.get('/admin/tasks/export', { params, responseType: 'blob' })
// }
import request from '../utils/request'

// 获取任务列表（修复路径）
export const getTasks = (params) => {
  return request.get('/task/admin/list', { params }) // ✅ 正确路径
}

// 获取任务详情（修复路径）
export const getTaskById = (id) => {
  return request.get(`/task/${id}`) // ✅ 对应后端 /task/{taskId}
}

// 更新任务状态（新增：后端原有接口是 /task/{taskId}/accept 等，需适配）
export const updateTaskStatus = (id, status) => {
  // 根据状态调用不同的后端接口
  let url = ''
  if (status === 1) {
    url = `/task/${id}/accept` // 接单（配送中）
  } else if (status === 2) {
    url = `/task/${id}/complete` // 完成
  } else if (status === 4) {
    url = `/task/${id}/cancel?reason=管理员操作` // 取消
  }
  return request.post(url)
}

// 删除任务（如果后端没有删除接口，可先注释，或新增）
export const deleteTask = (id) => {
  return request.delete(`/task/${id}`) // 需后端新增删除接口
}

// 获取任务统计数据（需后端新增 /task/statistics 接口）
export const getTaskStatistics = () => {
  return request.get('/task/statistics')
}

// 导出任务数据（需后端新增 /task/export 接口）
export const exportTasks = (params) => {
  return request.get('/task/export', { params, responseType: 'blob' })
}