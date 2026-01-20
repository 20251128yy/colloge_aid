// miniprogram/utils/request.js
/**
 * 小程序请求封装（适配后端Spring Boot接口）
 * 核心：端口改为8080，强制JSON格式，自动携带Token
 */
export default function request(options) {
  // 基础路径：后端实际运行端口是8080，不是3001！
  const baseUrl = 'http://localhost:8080/api';
  
  // 显示loading（如果需要）
  if (options.loading !== false) {
    wx.showLoading({ title: '加载中...', mask: true });
  }

  return new Promise((resolve, reject) => {
    wx.request({
      // 拼接完整接口路径
      url: baseUrl + options.url,
      // 请求方法（默认GET）
      method: options.method || 'GET',
      // 请求数据（默认空）
      data: options.data || {},
      // 请求头：强制JSON格式，携带Token
      header: {
        'Content-Type': 'application/json',
        // 登录后自动携带Token（管理员/普通用户通用）
        'Authorization': wx.getStorageSync('token') ? `Bearer ${wx.getStorageSync('token')}` : ''
      },
      // 成功响应
      success: (res) => {
        // 关闭loading
        wx.hideLoading();
        
        // 后端统一返回 {code, msg, data}
        const { code, msg, data } = res.data;
        
        // 成功：code=200
        if (code === 200) {
          resolve({ code, msg, data });
        } 
        // 业务错误：400/403等，直接reject让前端捕获
        else {
          reject({ code, msg, data });
        }
      },
      // 失败（网络错误/端口错误等）
      fail: (err) => {
        wx.hideLoading();
        // 统一封装错误信息
        reject({
          code: err.statusCode || 500,
          msg: '网络错误，请检查后端是否启动或端口是否正确',
          data: null
        });
        console.error('请求失败:', err);
      }
    });
  });
}