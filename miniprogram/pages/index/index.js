// miniprogram/pages/index/index.js
import { getPendingTasks } from '../../api/task';

Page({
  data: {
    taskList: [],
    searchKeyword: '',
    sortBy: 0, // 0: 积分最高, 1: 时间最近
    hasMore: true,
    page: 1,
    pageSize: 10
  },

  onLoad() {
    // 页面加载时获取任务列表
    this.loadTasks();
  },

  onShow() {
    // 页面显示时重新加载任务列表
    this.setData({
      taskList: [],
      page: 1,
      hasMore: true
    });
    this.loadTasks();
  },

  // 加载任务列表
  async loadTasks() {
    const { sortBy, page, pageSize, taskList } = this.data;

    try {
      wx.showLoading({ title: '加载中...' });

      // 调用获取待接单任务API（禁用request.js中的loading）
      const res = await getPendingTasks(sortBy, false);

      if (res.code === 200) {
        const newTasks = res.data || [];
        
        this.setData({
          taskList: page === 1 ? newTasks : [...taskList, ...newTasks],
          hasMore: newTasks.length >= pageSize,
          page: page + 1
        });
      } else {
        wx.showToast({ title: res.msg, icon: 'none' });
      }
    } catch (error) {
      console.error('加载任务失败:', error);
      wx.showToast({ title: error.msg || '加载失败，请重试', icon: 'none' });
    } finally {
      wx.hideLoading();
    }
  },

  // 搜索输入变化
  onSearchInput(e) {
    this.setData({
      searchKeyword: e.detail.value
    });
  },

  // 搜索任务
  onSearch() {
    const { searchKeyword } = this.data;
    // TODO: 实现搜索功能
    wx.showToast({ title: '搜索功能开发中', icon: 'none' });
  },

  // 排序方式变化
  onSortChange(e) {
    const sortBy = e.currentTarget.dataset.sort;
    this.setData({
      sortBy: sortBy,
      taskList: [],
      page: 1,
      hasMore: true
    });
    this.loadTasks();
  },

  // 任务卡片点击
  onTaskTap(e) {
    const taskId = e.currentTarget.dataset.taskId;
    wx.navigateTo({
      url: `/pages/task-detail/task-detail?taskId=${taskId}`
    });
  },

  // 加载更多任务
  onLoadMore() {
    if (this.data.hasMore) {
      this.loadTasks();
    }
  },

  // 创建任务
  onCreateTask() {
    // 跳转到创建任务页面
    wx.navigateTo({ url: '/pages/create-task/create-task' });
  }
});
