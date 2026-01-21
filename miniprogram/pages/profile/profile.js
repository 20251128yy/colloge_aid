// miniprogram/pages/profile/profile.js
import { getCurrentUserInfo, switchRole, getPointsInfo } from '../../api/user';

Page({
  data: {
    userInfo: {},
    currentRole: 0, // 当前角色：0-需求方，1-派送方
    pointsInfo: {
      availablePoints: 0,
      frozenPoints: 0
    }
  },

  onLoad() {
    // 页面加载时获取用户信息和积分
    this.loadUserInfo();
    this.loadPointsInfo();
  },

  onShow() {
    // 页面显示时重新加载用户信息
    this.loadUserInfo();
    this.loadPointsInfo();
  },

  // 加载用户信息
  async loadUserInfo() {
    try {
      wx.showLoading({ title: '加载中...' });

      // 调用获取当前用户信息API（禁用自动loading）
      const res = await getCurrentUserInfo(false);

      if (res.code === 200) {
        this.setData({
          userInfo: res.data,
          currentRole: res.data.currentRole || 0
        });
      } else {
        wx.showToast({ title: res.msg, icon: 'none' });
      }
    } catch (error) {
      console.error('加载用户信息失败:', error);
      wx.showToast({ title: error.msg || '加载失败，请重试', icon: 'none' });
    } finally {
      wx.hideLoading();
    }
  },

  // 加载积分信息
  async loadPointsInfo() {
    try {
      // 调用获取积分信息API（禁用自动loading）
      const res = await getPointsInfo(false);

      if (res.code === 200) {
        this.setData({
          pointsInfo: res.data
        });
      } else {
        console.log('加载积分信息失败:', res.msg);
      }
    } catch (error) {
      console.error('加载积分信息失败:', error);
    }
  },

  // 切换角色
  async onSwitchRole() {
    const { currentRole } = this.data;
    const newRole = currentRole === 0 ? 1 : 0;

    wx.showModal({
      title: '切换角色',
      content: `确定要切换到${newRole === 0 ? '需求方' : '派送方'}角色吗？`,
      success: async (res) => {
        if (res.confirm) {
          try {
            wx.showLoading({ title: '切换中...' });

            // 调用切换角色API（禁用自动loading）
            const result = await switchRole(newRole, false);

            if (result.code === 200) {
              wx.showToast({ title: '角色切换成功' });
              this.setData({
                currentRole: newRole
              });
              // 重新加载用户信息
              this.loadUserInfo();
            } else {
              wx.showToast({ title: result.msg, icon: 'none' });
            }
          } catch (error) {
            console.error('角色切换失败:', error);
            wx.showToast({ title: error.msg || '切换失败，请重试', icon: 'none' });
          } finally {
            wx.hideLoading();
          }
        }
      }
    });
  },

  // 查看我的发布
  onViewMyTasks() {
    // TODO: 跳转到我的发布页面
    wx.showToast({ title: '功能开发中', icon: 'none' });
  },

  // 查看我的接单
  onViewAcceptedTasks() {
    // TODO: 跳转到我的接单页面
    wx.showToast({ title: '功能开发中', icon: 'none' });
  },

  // 查看积分记录
  onViewPointRecords() {
    // TODO: 跳转到积分记录页面
    wx.showToast({ title: '功能开发中', icon: 'none' });
  },

  // 查看区块链记录
  onViewBlockchain() {
    // TODO: 跳转到区块链记录页面
    wx.showToast({ title: '功能开发中', icon: 'none' });
  },

  // 设置
  onSettings() {
    // TODO: 跳转到设置页面
    wx.showToast({ title: '功能开发中', icon: 'none' });
  },

  // 退出登录
  onLogout() {
    wx.showModal({
      title: '退出登录',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 调用全局的退出登录方法
          const app = getApp();
          app.logout();
        }
      }
    });
  }
});
