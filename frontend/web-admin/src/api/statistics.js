// Web管理端数据统计API模块

import { get } from '../utils/request';

/**
 * 获取平台统计数据
 * @returns {Promise<Object>} - 统计数据
 */
export const getStatistics = () => {
  return get('/admin/statistics');
};

/**
 * 获取用户统计数据
 * @param {Object} params - 查询参数
 * @param {string} [params.startDate] - 开始日期
 * @param {string} [params.endDate] - 结束日期
 * @returns {Promise<Object>} - 用户统计数据
 */
export const getUserStatistics = (params = {}) => {
  return get('/admin/statistics/users', params);
};

/**
 * 获取任务统计数据
 * @param {Object} params - 查询参数
 * @param {string} [params.startDate] - 开始日期
 * @param {string} [params.endDate] - 结束日期
 * @returns {Promise<Object>} - 任务统计数据
 */
export const getTaskStatistics = (params = {}) => {
  return get('/admin/statistics/tasks', params);
};

/**
 * 获取积分交易统计数据
 * @param {Object} params - 查询参数
 * @param {string} [params.startDate] - 开始日期
 * @param {string} [params.endDate] - 结束日期
 * @returns {Promise<Object>} - 积分交易统计数据
 */
export const getPointStatistics = (params = {}) => {
  return get('/admin/statistics/points', params);
};

/**
 * 获取用户积分排行
 * @param {Object} params - 查询参数
 * @param {number} [params.size] - 排行数量
 * @returns {Promise<Array>} - 用户积分排行
 */
export const getPointRanking = (params = {}) => {
  return get('/admin/statistics/points/ranking', params);
};