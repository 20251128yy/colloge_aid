// miniprogram/api/task.js
import request from '../utils/request';
// const request = require('../utils/request');

/**
 * 获取待接单任务列表
 * @param {number} sortBy - 排序方式（0-积分最高，1-时间最近）
 * @returns {Promise} - 返回Promise对象
 */
// export const getPendingTasks = (sortBy = 0) => {
//   return request.get('/task/pending', { sortBy });
// };
export const getPendingTasks = () => {
  return request({
    url: '/task/list', // 后端接口路径
    method: 'GET', // 指定GET方法
    loading: true // 自动显示loading
  });
};
/**
 * 创建任务
 * @param {object} data - 任务信息
 * @returns {Promise} - 返回Promise对象
 */
const createTask = (data) => {
  return request.post('/task/create', data);
};

/**
 * 获取任务详情
 * @param {number} taskId - 任务ID
 * @returns {Promise} - 返回Promise对象
 */
const getTaskById = (taskId) => {
  return request.get(`/task/${taskId}`);
};

/**
 * 接单
 * @param {number} taskId - 任务ID
 * @returns {Promise} - 返回Promise对象
 */
const acceptTask = (taskId) => {
  return request.post(`/task/${taskId}/accept`);
};

/**
 * 完成任务
 * @param {number} taskId - 任务ID
 * @returns {Promise} - 返回Promise对象
 */
const completeTask = (taskId) => {
  return request.post(`/task/${taskId}/complete`);
};

/**
 * 取消任务
 * @param {number} taskId - 任务ID
 * @param {string} reason - 取消原因
 * @returns {Promise} - 返回Promise对象
 */
const cancelTask = (taskId, reason) => {
  return request.post(`/task/${taskId}/cancel`, { reason });
};

/**
 * 发放积分
 * @param {number} taskId - 任务ID
 * @returns {Promise} - 返回Promise对象
 */
const issuePoints = (taskId) => {
  return request.post(`/task/${taskId}/issuePoints`);
};

/**
 * 获取我的发布任务列表
 * @returns {Promise} - 返回Promise对象
 */
const getMyTasks = () => {
  return request.get('/task/my');
};

/**
 * 获取我的接单任务列表
 * @returns {Promise} - 返回Promise对象
 */
const getMyAcceptedTasks = () => {
  return request.get('/task/my-accepted');
};

module.exports = {
  getPendingTasks,
  createTask,
  getTaskById,
  acceptTask,
  completeTask,
  cancelTask,
  issuePoints,
  getMyTasks,
  getMyAcceptedTasks
};
