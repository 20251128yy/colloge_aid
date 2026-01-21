import request from '../utils/request'

// 获取用户列表（适配后端参数）
export const getUsers = (params) => {
  return request.get('/admin/users', { params })
}

// 获取用户详情
export const getUserById = (id) => {
  return request.get(`/admin/users/${id}`)
}

// 更新用户审核状态（修正参数传递方式）
export const updateUserAuditStatus = (id, status) => {
  return request.put(`/admin/users/${id}/audit`, {}, {
    params: { auditStatus: status } // 后端@RequestParam需要放在params里
  })
}

// 更新用户角色（修正参数传递）
export const updateUserRole = (id, role) => {
  return request.put(`/admin/users/${id}/role`, {}, {
    params: { role: role }
  })
}

// 更新用户状态（修正参数传递）
export const updateUserStatus = (id, status) => {
  return request.put(`/admin/users/${id}/status`, {}, {
    params: { status: status }
  })
}

// 获取用户积分记录
export const getUserPointsHistory = (id, params) => {
  return request.get(`/admin/users/${id}/points`, { params })
}

// 管理员登录 - 调用 /admin/login
// export const adminLogin = (data) => {
//   return request.post('/admin/login', {
//     account: data.username || '',
//     password: data.password || ''
//   })
//   .then(res => {
//     console.log('管理员登录响应:', res)
//     if (res.code !== 200) {
//       throw new Error(res.msg || '登录失败')
//     }
//     return res
//   })
//   .catch(error => {
//     console.error('管理员登录错误:', error)
//     throw error
//   })
// }
// 管理员登录
export const adminLogin = (data) => {
  console.log('管理员登录请求:', data)
  
  return request.post('/admin/login', {
    account: data.username || '',
    password: data.password || ''
  })
  .then(res => {
    console.log('管理员登录响应:', res)
    
    // 检查响应格式
    let token, user
    if (res && res.token && res.user) {
      // 直接从res中获取
      token = res.token
      user = res.user
    } else if (res && res.data && res.data.token && res.data.user) {
      // 从res.data中获取
      token = res.data.token
      user = res.data.user
    } else {
      console.error('登录响应缺少必要字段', res)
      throw new Error('登录响应格式错误')
    }
    
    // 确保token是字符串
    if (typeof token !== 'string') {
      console.error('Token格式错误，期望字符串但得到:', token)
      throw new Error('Token格式错误')
    }
    
    // 保存到localStorage
    localStorage.setItem('adminToken', token)
    localStorage.setItem('userInfo', JSON.stringify(user))
    
    console.log('登录成功，Token:', token)
    console.log('用户信息:', user)
    
    return { token, user }
  })
  .catch(error => {
    console.error('管理员登录过程错误:', error)
    // 清除可能存在的旧token
    localStorage.removeItem('adminToken')
    localStorage.removeItem('userInfo')
    throw error
  })
}