// miniprogram/pages/task-detail/task-detail.js
import { getTaskById, acceptTask, completeTask, cancelTask, issuePoints } from '../../api/task';
import { getHistoryByTaskId } from '../../api/blockchain';

Page({
  data: {
    taskId: '',
    taskInfo: {},
    isDeliverer: false, // 当前用户是否为派送方角色
    currentUserId: '', // 当前登录用户ID
    blockchainHistory: ''
  },

  onLoad(options) {
    // 获取任务ID
    const { taskId } = options;
    this.setData({
      taskId: taskId
    });

    // 加载任务详情
    this.loadTaskDetail();

    // 加载区块链积分流转记录
    this.loadBlockchainHistory();

    // 获取当前用户信息
    this.getCurrentUserInfo();
  },

  // 加载任务详情
  async loadTaskDetail() {
    const { taskId } = this.data;

    try {
      wx.showLoading({ title: '加载中...' });

      // 调用获取任务详情API
      const res = await getTaskById(taskId);

      if (res.success) {
        this.setData({
          taskInfo: res.data
        });
      } else {
        wx.showToast({ title: res.message, icon: 'none' });
      }
    } catch (error) {
      console.error('加载任务详情失败:', error);
      wx.showToast({ title: '加载失败，请重试', icon: 'none' });
    } finally {
      wx.hideLoading();
    }
  },

  // 加载区块链积分流转记录
  async loadBlockchainHistory() {
    const { taskId } = this.data;

    try {
      // 调用获取区块链历史记录API
      const res = await getHistoryByTaskId(taskId);

      if (res.success) {
        this.setData({
          blockchainHistory: res.data
        });
      } else {
        console.log('区块链记录加载失败:', res.message);
      }
    } catch (error) {
      console.error('加载区块链记录失败:', error);
    }
  },

  // 获取当前用户信息
  getCurrentUserInfo() {
    // 从全局数据或本地存储获取当前用户信息
    const app = getApp();
    // TODO: 实现获取当前用户信息的逻辑
    this.setData({
      isDeliverer: false, // 默认不是派送方
      currentUserId: 1 // 模拟用户ID
    });
  },

  // 接单
  async onAcceptTask() {
    const { taskId, currentUserId } = this.data;

    try {
      wx.showLoading({ title: '处理中...' });

      // 调用接单API
      const res = await acceptTask(taskId, currentUserId);

      if (res.success) {
        wx.showToast({ title: '接单成功' });
        // 更新任务状态
        this.setData({
          'taskInfo.taskStatus': 1,
          'taskInfo.delivererId': currentUserId
        });
      } else {
        wx.showToast({ title: res.message, icon: 'none' });
      }
    } catch (error) {
      console.error('接单失败:', error);
      wx.showToast({ title: '接单失败，请重试', icon: 'none' });
    } finally {
      wx.hideLoading();
    }
  },

  // 完成任务
  async onCompleteTask() {
    const { taskId } = this.data;

    try {
      wx.showLoading({ title: '处理中...' });

      // 调用完成任务API
      const res = await completeTask(taskId);

      if (res.success) {
        wx.showToast({ title: '任务完成' });
        // 更新任务状态
        this.setData({
          'taskInfo.taskStatus': 2
        });
      } else {
        wx.showToast({ title: res.message, icon: 'none' });
      }
    } catch (error) {
      console.error('完成任务失败:', error);
      wx.showToast({ title: '操作失败，请重试', icon: 'none' });
    } finally {
      wx.hideLoading();
    }
  },

  // 取消任务
  async onCancelTask() {
    const { taskId } = this.data;

    wx.showModal({
      title: '确认取消',
      content: '确定要取消该任务吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            wx.showLoading({ title: '处理中...' });

            // 调用取消任务API
            const result = await cancelTask(taskId, '用户取消');

            if (result.success) {
              wx.showToast({ title: '任务已取消' });
              // 更新任务状态
              this.setData({
                'taskInfo.taskStatus': 4
              });
            } else {
              wx.showToast({ title: result.message, icon: 'none' });
            }
          } catch (error) {
            console.error('取消任务失败:', error);
            wx.showToast({ title: '操作失败，请重试', icon: 'none' });
          } finally {
            wx.hideLoading();
          }
        }
      }
    });
  },

  // 确认完成并发放积分
  async onConfirmTask() {
    const { taskId } = this.data;

    wx.showModal({
      title: '确认完成',
      content: '确定要确认任务完成并发放积分吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            wx.showLoading({ title: '处理中...' });

            // 调用发放积分API
            const result = await issuePoints(taskId);

            if (result.success) {
              wx.showToast({ title: '积分已发放' });
              // 更新任务状态
              this.setData({
                'taskInfo.taskStatus': 3
              });
              // 重新加载区块链记录
              this.loadBlockchainHistory();
            } else {
              wx.showToast({ title: result.message, icon: 'none' });
            }
          } catch (error) {
            console.error('发放积分失败:', error);
            wx.showToast({ title: '操作失败，请重试', icon: 'none' });
          } finally {
            wx.hideLoading();
          }
        }
      }
    });
  }
});
