// 微信小程序区块链API模块

import { get, post } from '../utils/request';

/**
 * 手动触发积分上链
 * @param {number} transactionId - 交易ID
 * @returns {Promise<Object>} - 上链结果，包含交易哈希
 */
export const uploadPointTransaction = (transactionId) => {
  return post('/blockchain/point/upload', { transactionId });
};

/**
 * 查询链上积分流水
 * @param {number} userId - 用户ID
 * @returns {Promise<Array>} - 积分流水列表
 */
export const queryPointTransactions = (userId) => {
  return get('/blockchain/point/query', { userId });
};

/**
 * 获取积分余额
 * @returns {Promise<number>} - 积分余额
 */
export const getPointBalance = () => {
  return get('/user/points');
};

/**
 * 积分充值
 * @param {number} amount - 充值金额
 * @returns {Promise<Object>} - 充值结果
 */
export const rechargePoints = (amount) => {
  return post('/user/points/recharge', { amount });
};

/**
 * 积分兑换
 * @param {number} productId - 产品ID
 * @returns {Promise<Object>} - 兑换结果
 */
export const exchangePoints = (productId) => {
  return post('/user/points/exchange', { productId });
};