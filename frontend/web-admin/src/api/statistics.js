import request from '../utils/request'

// 获取平台统计数据
export const getPlatformStatistics = () => {
  return request.get('/admin/statistics/platform')
}

// 获取用户统计数据
export const getUserStatistics = () => {
  return request.get('/admin/statistics/users')
}

// 获取任务统计数据
export const getTaskStatistics = () => {
  return request.get('/admin/statistics/tasks')
}

// 获取积分统计数据
export const getPointsStatistics = () => {
  return request.get('/admin/statistics/points')
}

// 获取每日统计数据
export const getDailyStatistics = (params) => {
  return request.get('/admin/statistics/daily', { params })
}

// 获取每周统计数据
export const getWeeklyStatistics = (params) => {
  return request.get('/admin/statistics/weekly', { params })
}

// 获取每月统计数据
export const getMonthlyStatistics = (params) => {
  return request.get('/admin/statistics/monthly', { params })
}