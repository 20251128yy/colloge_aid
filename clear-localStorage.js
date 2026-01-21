// 模拟浏览器localStorage环境
const localStorage = {
  data: {},
  getItem(key) {
    return this.data[key] || null;
  },
  setItem(key, value) {
    this.data[key] = value;
  },
  removeItem(key) {
    delete this.data[key];
  },
  clear() {
    this.data = {};
  }
};

// 测试清除错误格式token的功能
function testClearInvalidToken() {
  console.log('=== 测试清除错误格式token ===');
  
  // 模拟存储错误格式的token
  localStorage.setItem('adminToken', { token: 'test-token' });
  localStorage.setItem('isAdminLoggedIn', true);
  localStorage.setItem('userInfo', JSON.stringify({ id: 1, name: 'Admin' }));
  
  console.log('测试前localStorage:', localStorage.data);
  
  // 模拟检查和清除错误格式token
  let token = localStorage.getItem('adminToken');
  console.log('获取到的token:', token);
  
  if (token && typeof token !== 'string') {
    console.error('Token格式错误，期望字符串但得到:', token);
    localStorage.removeItem('adminToken');
    localStorage.removeItem('isAdminLoggedIn');
    localStorage.removeItem('userInfo');
  }
  
  console.log('测试后localStorage:', localStorage.data);
  console.log('测试完成:', localStorage.getItem('adminToken') === null ? '成功清除错误格式token' : '清除失败');
}

// 执行测试
testClearInvalidToken();

console.log('\n=== 测试正常格式token ===');
// 测试正常格式token
localStorage.setItem('adminToken', 'valid-token-string');
localStorage.setItem('isAdminLoggedIn', true);

let token = localStorage.getItem('adminToken');
console.log('获取到的token:', token);
console.log('token类型:', typeof token);

if (token && typeof token === 'string' && token.trim() !== '') {
  console.log('Token格式正确，可以正常使用');
} else if (token) {
  console.error('Token格式错误');
  localStorage.removeItem('adminToken');
  localStorage.removeItem('isAdminLoggedIn');
}

console.log('测试后localStorage:', localStorage.data);
