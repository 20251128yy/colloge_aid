// miniprogram/pages/create-task/create-task.js
import { createTask } from '../../api/task';

Page({
  data: {
    title: '',
    description: '',
    pointAmount: '',
    location: '',
    contactPhone: '',
    expectedCompletionTime: '',
    expectedCompletionTimeText: '请选择期望完成时间',
    startTime: '',
    endTime: ''
  },

  onLoad() {
    // 初始化时间选择器的起止时间
    const now = new Date();
    const startTime = now.toISOString().slice(0, 16);
    const endTime = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000).toISOString().slice(0, 16);
    
    this.setData({
      startTime: startTime,
      endTime: endTime
    });
  },

  // 处理表单输入变化
  onInputChange(e) {
    const { type } = e.currentTarget.dataset;
    this.setData({
      [type]: e.detail.value
    });
  },

  // 处理时间选择器变化
  onTimeChange(e) {
    const time = e.detail.value;
    // 格式化时间，将 T 替换为空格，添加秒数
    const formattedTime = time.replace('T', ' ') + ':00';
    this.setData({
      expectedCompletionTime: formattedTime,
      expectedCompletionTimeText: time
    });
  },

  // 表单提交
  async onSubmit(e) {
    const { title, description, pointAmount, location, contactPhone, expectedCompletionTime } = this.data;

    // 表单验证
    if (!title) {
      wx.showToast({ title: '请输入任务标题', icon: 'none' });
      return;
    }

    if (!description) {
      wx.showToast({ title: '请输入任务描述', icon: 'none' });
      return;
    }

    if (!pointAmount || pointAmount <= 0) {
      wx.showToast({ title: '请输入有效的积分奖励', icon: 'none' });
      return;
    }

    if (!location) {
      wx.showToast({ title: '请输入任务地点', icon: 'none' });
      return;
    }

    if (!contactPhone) {
      wx.showToast({ title: '请输入联系电话', icon: 'none' });
      return;
    }

    if (!expectedCompletionTime) {
      wx.showToast({ title: '请选择期望完成时间', icon: 'none' });
      return;
    }

    try {
      wx.showLoading({ title: '发布中...', mask: true });

      // 调用创建任务API
      const res = await createTask({
        title: title,
        description: description,
        pointAmount: parseInt(pointAmount),
        location: location,
        contactPhone: contactPhone,
        expectedCompletionTime: expectedCompletionTime
      });

      // 适配后端响应格式
      if (res.code === 200) {
        wx.showToast({ title: '任务发布成功', icon: 'success' });
        
        // 延迟跳转到任务列表页面
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({ title: res.msg || '发布失败，请重试', icon: 'none' });
      }
    } catch (error) {
      console.error('创建任务失败:', error);
      wx.showToast({ title: error.msg || '发布失败，请重试', icon: 'none' });
    } finally {
      wx.hideLoading();
    }
  }
});