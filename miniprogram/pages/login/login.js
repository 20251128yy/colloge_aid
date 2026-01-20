// miniprogram/pages/login/login.js
import { login, register } from '../../api/user';

Page({
  data: {
    account: '', // 手机号或邮箱
    password: '',
    name: '', // 姓名
    studentId: '' // 学号
  },

  onInputChange(e) {
    const { type } = e.currentTarget.dataset;
    this.setData({
      [type]: e.detail.value
    });
  },

  // 登录（适配code/msg格式）
  async onLogin() {
    const { account, password } = this.data;

    try {
      wx.showLoading({ title: '登录中...', mask: true });

      const res = await login({ account, password });

      // 适配后端 code=200 为成功
      if (res.code === 200) {
        wx.setStorageSync('token', res.data);
        wx.setStorageSync('isLoggedIn', true);
        wx.showToast({ title: '登录成功', icon: 'success' });

        setTimeout(() => {
          wx.switchTab({ url: '/pages/index/index' });
        }, 1500);
      }

    } catch (error) {
      console.error('登录失败:', error);
      wx.showToast({ 
        title: error.msg || '登录失败，请重试', 
        icon: 'none',
        duration: 2000
      });
    } finally {
      wx.hideLoading();
    }
  },

  // 注册（彻底解决配对警告+精准提示）
  async onRegister() {
    const { account, password, name, studentId } = this.data;

    // 前置关闭残留loading，避免重复
    wx.hideLoading();

    if (!this.validateInput(account, password, name, studentId)) {
      return;
    }

    try {
      wx.showLoading({ title: '注册中...', mask: true });

      // 构造参数：确保非手机号/邮箱时传 null 而非空字符串（避免后端校验空字符串）
      const registerParams = { 
        phone: this.isPhone(account) ? account : null,
        email: this.isEmail(account) ? account : null,
        password: password,
        name: name,
        studentId: studentId
      };

      const res = await register(registerParams);

      // 只有code=200才会走到这里
      wx.showToast({ title: '注册成功，请登录', icon: 'success', duration: 2000 });
      this.setData({ account: '', password: '', name: '', studentId: '' });

    } catch (error) {
      console.error('注册失败:', error);
      // 显示后端精准提示（手机号/邮箱/学号重复）
      wx.showToast({ 
        title: error.msg || '注册失败，请重试', 
        icon: 'none',
        duration: 2000
      });
    } finally {
      // 确保loading必关闭（核心解决配对警告）
      wx.hideLoading();
    }
  },

  // 验证输入格式（不变）
  validateInput(account, password, name, studentId) {
    if (!account) {
      wx.showToast({ title: '请输入手机号或邮箱', icon: 'none' });
      return false;
    }

    if (!name) {
      wx.showToast({ title: '请输入姓名', icon: 'none' });
      return false;
    }

    if (!studentId) {
      wx.showToast({ title: '请输入学号', icon: 'none' });
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

  isPhone(str) {
    return /^1[3-9]\d{9}$/.test(str);
  },

  isEmail(str) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(str);
  }
});