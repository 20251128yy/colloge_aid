// miniprogram/pages/login/login.js
import { login, register } from '../../api/user';

Page({
  data: {
    account: '', // 手机号或邮箱
    password: ''
  },

  // 输入框内容变化处理
  onInputChange(e) {
    const { type } = e.currentTarget.dataset;
    this.setData({
      [type]: e.detail.value
    });
  },

  // 登录
  async onLogin() {
    const { account, password } = this.data;

    try {
      wx.showLoading({ title: '登录中...' });

      // 调用登录API
      const res = await login({ account, password });

      if (res.success) {
        // 保存登录状态到本地存储
        wx.setStorageSync('token', res.data);
        wx.setStorageSync('isLoggedIn', true);

        wx.showToast({ title: '登录成功' });

        // 跳转到首页
        setTimeout(() => {
          wx.switchTab({ url: '/pages/index/index' });
        }, 1500);
      } else {
        wx.showToast({ title: res.message, icon: 'none' });
      }
    } catch (error) {
      console.error('登录失败:', error);
      wx.showToast({ title: '登录失败，请重试', icon: 'none' });
    } finally {
      wx.hideLoading();
    }
  },

  // 注册
  async onRegister() {
    const { account, password } = this.data;

    // 简单验证输入格式
    if (!this.validateInput(account, password)) {
      return;
    }

    try {
      wx.showLoading({ title: '注册中...' });

      // 调用注册API
      const res = await register({ 
        phone: this.isPhone(account) ? account : '',
        email: this.isEmail(account) ? account : '',
        password: password,
        nickName: '新用户', // 默认昵称
        role: 0 // 默认角色：需求方
      });

      if (res.success) {
        wx.showToast({ title: '注册成功' });
        wx.showToast({ title: '请登录', icon: 'none' });
      } else {
        wx.showToast({ title: res.message, icon: 'none' });
      }
    } catch (error) {
      console.error('注册失败:', error);
      wx.showToast({ title: '注册失败，请重试', icon: 'none' });
    } finally {
      wx.hideLoading();
    }
  },

  // 验证输入格式
  validateInput(account, password) {
    if (!account) {
      wx.showToast({ title: '请输入手机号或邮箱', icon: 'none' });
      return false;
    }

    if (!password) {
      wx.showToast({ title: '请输入密码', icon: 'none' });
      return false;
    }

    if (password.length < 6) {
      wx.showToast({ title: '密码长度不能少于6位', icon: 'none' });
      return false;
    }

    if (this.isPhone(account) && !/^1[3-9]\d{9}$/.test(account)) {
      wx.showToast({ title: '手机号格式不正确', icon: 'none' });
      return false;
    }

    if (this.isEmail(account) && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(account)) {
      wx.showToast({ title: '邮箱格式不正确', icon: 'none' });
      return false;
    }

    return true;
  },

  // 判断是否为手机号
  isPhone(str) {
    return /^1[3-9]\d{9}$/.test(str);
  },

  // 判断是否为邮箱
  isEmail(str) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(str);
  }
});
