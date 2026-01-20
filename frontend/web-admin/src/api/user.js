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

// 管理员登录（适配后端/user/login，统一登录入口）
export const adminLogin = (data) => {
  // 复用用户登录接口，后端会验证是否为管理员
  return request.post('/user/login', data).then(res => {
    // 登录成功后验证是否为管理员（前端二次校验）
    if (res.code === 200) {
      // 解析Token中的identityType（需在JwtUtil前端实现解析）
      // const identityType = JwtUtil.getIdentityTypeFromToken(res.data);
      // if (identityType !== 2) {
      //   return Promise.reject({ msg: '非管理员账号，无法登录管理后台' });
      // }
    }
    return res;
  })
}