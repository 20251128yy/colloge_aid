// Web管理端API请求封装

// 后端API基础URL
const API_BASE_URL = 'http://localhost:8080/api';

// 统一请求方法
export const request = async (url, method, data = {}, options = {}) => {
  // 获取本地存储的token
  const token = localStorage.getItem('token');

  // 设置请求头
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers
  };

  // 如果有token，添加到请求头
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  try {
    const response = await fetch(`${API_BASE_URL}${url}`, {
      method,
      headers,
      body: method !== 'GET' ? JSON.stringify(data) : undefined,
      ...options
    });

    const responseData = await response.json();

    if (response.ok) {
      const { code, msg, data } = responseData;
      if (code === 200) {
        return data;
      } else if (code === 401) {
        // 未授权，跳转到登录页
        localStorage.removeItem('token');
        localStorage.removeItem('userInfo');
        window.location.href = '/login';
        throw new Error('未授权，请重新登录');
      } else {
        // 其他错误
        throw new Error(msg || '请求失败');
      }
    } else {
      throw new Error(`网络错误: ${response.status}`);
    }
  } catch (error) {
    console.error('API请求失败:', error);
    throw error;
  }
};

// GET请求
export const get = (url, params = {}, options = {}) => {
  // 处理查询参数
  const queryString = Object.keys(params)
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
    .join('&');
  const fullUrl = queryString ? `${url}?${queryString}` : url;
  return request(fullUrl, 'GET', {}, options);
};

// POST请求
export const post = (url, data = {}, options = {}) => {
  return request(url, 'POST', data, options);
};

// PUT请求
export const put = (url, data = {}, options = {}) => {
  return request(url, 'PUT', data, options);
};

// DELETE请求
export const del = (url, data = {}, options = {}) => {
  return request(url, 'DELETE', data, options);
};