// 微信小程序用户API模块

import { get, post, put } from '../utils/request';

/**
 * 用户登录
 * @param {Object} data - 登录数据
 * @param {string} data.phone - 手机号
 * @param {string} data.password - 密码
 * @returns {Promise<Object>} - 登录结果，包含token
 */
export const login = (data) => {
  return post('/user/login', data);
};

/**
 * 用户注册
 * @param {Object} data - 注册数据
 * @param {string} data.phone - 手机号
 * @param {string} data.email - 邮箱
 * @param {string} data.password - 密码
 * @param {string} data.name - 姓名
 * @returns {Promise<Object>} - 注册结果，包含用户信息
 */
export const register = (data) => {
  return post('/user/register', data);
};

/**
 * 获取用户信息
 * @returns {Promise<Object>} - 用户信息
 */
export const getUserInfo = () => {
  return get('/user/info');
};

/**
 * 切换用户角色
 * @param {number} role - 角色ID，1=需求方，2=派送方
 * @returns {Promise<Object>} - 更新后的用户信息
 */
export const switchRole = (role) => {
  return post('/user/switchRole', { role });
};

/**
 * 更新用户信息
 * @param {Object} data - 用户信息
 * @returns {Promise<Object>} - 更新后的用户信息
 */
export const updateUserInfo = (data) => {
  return put('/user/profile', data);
};

/**
 * 修改密码
 * @param {Object} data - 密码修改数据
 * @param {string} data.oldPassword - 旧密码
 * @param {string} data.newPassword - 新密码
 * @returns {Promise<Object>} - 修改结果
 */
export const changePassword = (data) => {
  return put('/user/password', data);
};