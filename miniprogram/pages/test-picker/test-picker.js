// miniprogram/pages/test-picker/test-picker.js
Page({
  data: {
    selectedTime: '',
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

  // 处理时间选择器变化
  onTimeChange(e) {
    console.log('时间选择变化:', e.detail.value);
    this.setData({
      selectedTime: e.detail.value
    });
  }
});