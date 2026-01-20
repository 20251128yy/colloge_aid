// miniprogram/api/user.js
import request from '../utils/request';

/**
 * 用户登录接口
 * @param {Object} data - { account: 手机号/邮箱, password: 密码 }
 */
export const login = (data) => {
  return request({
    url: '/user/login', // 完整路径：http://localhost:8080/api/user/login
    method: 'POST',
    data: data,
    loading: false // 登录页自己控制loading，这里关闭
  });
};

/**
 * 用户注册接口
 * @param {Object} data - { phone/email, password, name, studentId }
 */
export const register = (data) => {
  return request({
    url: '/user/register', // 完整路径：http://localhost:8080/api/user/register
    method: 'POST',
    data: data,
    loading: false // 注册页自己控制loading
  });
};

/**
 * 管理员登录（复用用户登录接口，后端验证身份）
 * @param {Object} data - { account: 管理员手机号, password: 密码 }
 */
export const adminLogin = (data) => {
  return request({
    url: '/user/login', // 管理员和普通用户共用登录接口
    method: 'POST',
    data: data,
    loading: false
  });
};