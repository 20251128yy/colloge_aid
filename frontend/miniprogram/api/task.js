// 微信小程序任务API模块

import { get, post, put } from '../utils/request';

/**
 * 获取任务列表
 * @param {Object} params - 查询参数
 * @param {number} [params.sortBy] - 排序方式，0=默认，1=距离，2=奖励，3=发布时间
 * @param {number} [params.taskStatus] - 任务状态，0=待接单，1=已接单，2=已完成
 * @param {number} [params.page] - 页码
 * @param {number} [params.size] - 每页数量
 * @returns {Promise<Array>} - 任务列表
 */
export const getTaskList = (params = {}) => {
  return get('/task/list', params);
};

/**
 * 创建任务
 * @param {Object} data - 任务数据
 * @param {string} data.title - 任务标题
 * @param {string} data.description - 任务描述
 * @param {string} data.fromLocation - 起点位置
 * @param {string} data.toLocation - 终点位置
 * @param {number} data.itemType - 物品类型
 * @param {number} data.pointAmount - 奖励积分
 * @param {string} data.expectedCompletionTime - 预计完成时间
 * @returns {Promise<Object>} - 创建的任务
 */
export const createTask = (data) => {
  return post('/task/create', data);
};

/**
 * 获取任务详情
 * @param {number} taskId - 任务ID
 * @returns {Promise<Object>} - 任务详情
 */
export const getTaskDetail = (taskId) => {
  return get(`/task/${taskId}`);
};

/**
 * 接单
 * @param {number} taskId - 任务ID
 * @returns {Promise<Object>} - 任务详情
 */
export const acceptTask = (taskId) => {
  return post(`/task/${taskId}/accept`);
};

/**
 * 完成任务
 * @param {number} taskId - 任务ID
 * @returns {Promise<Object>} - 任务详情
 */
export const completeTask = (taskId) => {
  return post(`/task/${taskId}/complete`);
};

/**
 * 取消任务
 * @param {number} taskId - 任务ID
 * @param {string} reason - 取消原因
 * @returns {Promise<Object>} - 任务详情
 */
export const cancelTask = (taskId, reason) => {
  return post(`/task/${taskId}/cancel`, { reason });
};

/**
 * 获取我发布的任务列表
 * @returns {Promise<Array>} - 任务列表
 */
export const getMyPublishedTasks = () => {
  return get('/task/my/published');
};

/**
 * 获取我接单的任务列表
 * @returns {Promise<Array>} - 任务列表
 */
export const getMyAcceptedTasks = () => {
  return get('/task/my/accepted');
};

/**
 * 搜索任务
 * @param {string} keyword - 搜索关键词
 * @returns {Promise<Array>} - 任务列表
 */
export const searchTasks = (keyword) => {
  return get('/task/search', { keyword });
};