// miniprogram/api/blockchain.js

const request = require('../utils/request');

/**
 * 获取指定任务的区块链积分流转记录
 * @param {number} taskId - 任务ID
 * @returns {Promise} - 返回Promise对象
 */
const getHistoryByTaskId = (taskId) => {
  return request.get('/blockchain/point/query', { taskId });
};

/**
 * 获取所有区块链积分流转记录
 * @returns {Promise} - 返回Promise对象
 */
const getAllHistory = () => {
  return request.get('/blockchain/point/query/all');
};

/**
 * 手动触发积分上链
 * @param {number} transactionId - 交易ID
 * @returns {Promise} - 返回Promise对象
 */
const uploadPointTransaction = (transactionId) => {
  return request.post('/blockchain/point/upload', { transactionId });
};

module.exports = {
  getHistoryByTaskId,
  getAllHistory,
  uploadPointTransaction
};
