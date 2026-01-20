// miniprogram/app.js
App({
  onLaunch() {
    // 初始化时检查登录状态
    this.checkLoginStatus();
  },

  onShow() {
    // 应用启动或从后台进入前台时执行
  },

  onHide() {
    // 应用从前台进入后台时执行
  },

  // 检查登录状态
  checkLoginStatus() {
    const token = wx.getStorageSync('token');
    const isLoggedIn = wx.getStorageSync('isLoggedIn');

    this.globalData.isLoggedIn = !!isLoggedIn;
    this.globalData.token = token;

    // 如果未登录，跳转到登录页
    if (!this.globalData.isLoggedIn) {
      wx.redirectTo({ url: '/pages/login/login' });
    }
  },

  // 登录成功后更新全局状态
  loginSuccess(token) {
    this.globalData.isLoggedIn = true;
    this.globalData.token = token;
    
    wx.setStorageSync('token', token);
    wx.setStorageSync('isLoggedIn', true);
  },

  // 退出登录
  logout() {
    this.globalData.isLoggedIn = false;
    this.globalData.token = '';
    
    wx.removeStorageSync('token');
    wx.removeStorageSync('isLoggedIn');
    
    wx.redirectTo({ url: '/pages/login/login' });
  },

  globalData: {
    isLoggedIn: false,
    token: '',
    userInfo: null
  }
});
