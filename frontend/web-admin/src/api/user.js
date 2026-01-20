// Web管理端用户API模块

import { get, post, put, del } from '../utils/request';

/**
 * 获取用户列表
 * @param {Object} params - 查询参数
 * @param {string} [params.keyword] - 关键词（手机号、邮箱、姓名）
 * @param {number} [params.auditStatus] - 审核状态
 * @param {number} [params.role] - 角色
 * @param {number} [params.page] - 页码
 * @param {number} [params.size] - 每页数量
 * @returns {Promise<Object>} - 用户列表
 */
export const getUserList = (params = {}) => {
  return get('/admin/users', params);
};

/**
 * 用户审核
 * @param {number} userId - 用户ID
 * @param {number} auditStatus - 审核状态，0=待审核，1=审核通过，2=审核拒绝
 * @param {string} reason - 审核理由
 * @returns {Promise<boolean>} - 审核结果
 */
export const auditUser = (userId, auditStatus, reason) => {
  return post('/admin/user/audit', { userId, auditStatus, reason });
};

/**
 * 获取用户详情
 * @param {number} userId - 用户ID
 * @returns {Promise<Object>} - 用户详情
 */
export const getUserDetail = (userId) => {
  return get(`/admin/users/${userId}`);
};

/**
 * 禁用/启用用户
 * @param {number} userId - 用户ID
 * @param {boolean} disabled - 是否禁用
 * @returns {Promise<boolean>} - 操作结果
 */
export const toggleUserStatus = (userId, disabled) => {
  return put(`/admin/users/${userId}/status`, { disabled });
};

/**
 * 调整用户积分
 * @param {number} userId - 用户ID
 * @param {number} amount - 调整的积分数量，正数为增加，负数为减少
 * @param {string} reason - 调整理由
 * @returns {Promise<boolean>} - 操作结果
 */
export const adjustUserPoints = (userId, amount, reason) => {
  return put(`/admin/users/${userId}/points`, { amount, reason });
};