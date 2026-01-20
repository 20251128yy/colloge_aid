// Web管理端任务API模块

import { get, post, put, del } from '../utils/request';

/**
 * 获取任务列表
 * @param {Object} params - 查询参数
 * @param {string} [params.keyword] - 关键词
 * @param {number} [params.taskStatus] - 任务状态
 * @param {number} [params.auditStatus] - 审核状态
 * @param {number} [params.page] - 页码
 * @param {number} [params.size] - 每页数量
 * @returns {Promise<Object>} - 任务列表
 */
export const getTaskList = (params = {}) => {
  return get('/admin/tasks', params);
};

/**
 * 任务审核/下架
 * @param {number} taskId - 任务ID
 * @param {number} auditStatus - 审核状态
 * @param {string} reason - 审核理由
 * @returns {Promise<boolean>} - 审核结果
 */
export const auditTask = (taskId, auditStatus, reason) => {
  return post('/admin/task/audit', { taskId, auditStatus, reason });
};

/**
 * 获取任务详情
 * @param {number} taskId - 任务ID
 * @returns {Promise<Object>} - 任务详情
 */
export const getTaskDetail = (taskId) => {
  return get(`/admin/tasks/${taskId}`);
};

/**
 * 强制取消任务
 * @param {number} taskId - 任务ID
 * @param {string} reason - 取消理由
 * @returns {Promise<boolean>} - 操作结果
 */
export const forceCancelTask = (taskId, reason) => {
  return put(`/admin/tasks/${taskId}/cancel`, { reason });
};

/**
 * 处理任务投诉
 * @param {number} taskId - 任务ID
 * @param {number} action - 处理方式
 * @param {string} remark - 处理备注
 * @returns {Promise<boolean>} - 操作结果
 */
export const handleTaskComplaint = (taskId, action, remark) => {
  return put(`/admin/tasks/${taskId}/complaint`, { action, remark });
};