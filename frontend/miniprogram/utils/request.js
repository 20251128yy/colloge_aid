// 微信小程序API请求封装

// 后端API基础URL
const API_BASE_URL = 'http://localhost:8080/api';

// 统一请求方法
export const request = (url, method, data = {}, options = {}) => {
  // 获取本地存储的token
  const token = wx.getStorageSync('token');

  // 设置请求头
  const header = {
    'Content-Type': 'application/json',
    ...options.header
  };

  // 如果有token，添加到请求头
  if (token) {
    header['Authorization'] = `Bearer ${token}`;
  }

  return new Promise((resolve, reject) => {
    wx.request({
      url: `${API_BASE_URL}${url}`,
      method,
      data,
      header,
      success: (res) => {
        // 处理响应
        if (res.statusCode === 200) {
          const { code, msg, data } = res.data;
          if (code === 200) {
            resolve(data);
          } else if (code === 401) {
            // 未授权，跳转到登录页
            wx.removeStorageSync('token');
            wx.removeStorageSync('userInfo');
            wx.navigateTo({ url: '/pages/login/login' });
            reject(new Error('未授权，请重新登录'));
          } else {
            // 其他错误，显示错误信息
            wx.showToast({
              title: msg || '请求失败',
              icon: 'none',
              duration: 2000
            });
            reject(new Error(msg || '请求失败'));
          }
        } else {
          wx.showToast({
            title: '网络错误',
            icon: 'none',
            duration: 2000
          });
          reject(new Error(`网络错误: ${res.statusCode}`));
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '请求失败',
          icon: 'none',
          duration: 2000
        });
        reject(err);
      }
    });
  });
};

// GET请求
export const get = (url, data = {}, options = {}) => {
  return request(url, 'GET', data, options);
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