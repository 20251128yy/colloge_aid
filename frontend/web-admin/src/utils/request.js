// import axios from 'axios'

// // 页面加载时检查并清除错误格式的token
// const checkAndClearInvalidToken = () => {
//   let token = localStorage.getItem('adminToken')
//   if (token && typeof token !== 'string') {
//     console.error('发现错误格式的token，正在清除:', token)
//     localStorage.removeItem('adminToken')
//     localStorage.removeItem('isAdminLoggedIn')
//     localStorage.removeItem('userInfo')
//   }
// }

// // 执行token检查
// checkAndClearInvalidToken()

// // 创建axios实例
// const request = axios.create({
//   baseURL: 'http://localhost:8080/api',
//   timeout: 10000
// })

// // 请求拦截器
// request.interceptors.request.use(
//   config => {
//     let token = localStorage.getItem('adminToken')
//     console.log('请求拦截器 - 获取到的token:', token)
    
//     // 确保token是字符串
//     if (token && typeof token === 'string' && token.trim() !== '') {
//       config.headers.Authorization = `Bearer ${token.trim()}`
//       console.log('请求拦截器 - 添加的Authorization头:', config.headers.Authorization)
//     } else if (token) {
//       // 如果token存在但不是字符串，清除它
//       console.error('Token格式错误，期望字符串但得到:', token)
//       localStorage.removeItem('adminToken')
//       localStorage.removeItem('isAdminLoggedIn')
//     }
    
//     return config
//   },
//   error => {
//     return Promise.reject(error)
//   }
// )

// // 响应拦截器
// // request.interceptors.response.use(
// //   response => {
// //     const res = response.data
// //     if (res.success) {
// //       return res.data
// //     } else {
// //       return Promise.reject(new Error(res.message || '请求失败'))
// //     }
// //   },
// //   error => {
// //     if (error.response && error.response.status === 401) {
// //       localStorage.removeItem('adminToken')
// //       window.location.href = '/login'
// //     }
// //     return Promise.reject(error)
// //   }
// // )
// // 响应拦截器
// request.interceptors.response.use(
//   response => {
//     const res = response.data
//     console.log('响应数据:', res)
    
//     // 检查code字段
//     if (res.code === 200 || res.code === undefined) {
//       // 有data字段返回data，没有返回整个res
//       return res.data !== undefined ? res.data : res
//     } else {
//       // 后端返回错误码
//       return Promise.reject(new Error(res.msg || res.message || '请求失败'))
//     }
//   },
//   error => {
//     if (error.response && error.response.status === 401) {
//       localStorage.removeItem('adminToken')
//       window.location.href = '/login'
//     }
//     return Promise.reject(error)
//   }
// )
// export default request

import axios from 'axios'

// 页面加载时检查并清除错误格式的token（修复核心逻辑）
const checkAndClearInvalidToken = () => {
  const token = localStorage.getItem('adminToken')
  // 定义异常Token的特征，包含常见的错误格式
  const invalidTokenPatterns = [
    '[object Object]', 
    'undefined', 
    'null', 
    ''
  ]
  
  // 检查Token是否为异常值
  if (token && invalidTokenPatterns.includes(token.trim())) {
    console.error('发现错误格式的token，正在清除:', token)
    localStorage.removeItem('adminToken')
    localStorage.removeItem('isAdminLoggedIn')
    localStorage.removeItem('userInfo')
  }
}

// 执行token检查
checkAndClearInvalidToken()

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
})

// 请求拦截器（优化Token处理逻辑）
request.interceptors.request.use(
  config => {
    let token = localStorage.getItem('adminToken') || ''
    console.log('请求拦截器 - 获取到的token:', token)
    
    // 清理Token前后空格
    token = token.trim()
    
    // 仅当Token是有效字符串时才添加请求头
    const invalidTokenPatterns = ['[object Object]', 'undefined', 'null', '']
    if (token && !invalidTokenPatterns.includes(token)) {
      config.headers.Authorization = `Bearer ${token}`
      console.log('请求拦截器 - 添加的Authorization头:', config.headers.Authorization)
    } else if (token) {
      // 如果是异常Token，直接清除并提示
      console.error('Token格式错误，期望有效字符串但得到:', token)
      localStorage.removeItem('adminToken')
      localStorage.removeItem('isAdminLoggedIn')
    }
    
    return config
  },
  error => {
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器（增强错误处理和提示）
request.interceptors.response.use(
  response => {
    const res = response.data
    console.log('响应数据:', res)
    
    // 统一处理后端返回格式
    if (res.code === 200 || res.code === undefined) {
      return res.data !== undefined ? res.data : res
    } else {
      // 针对Token相关错误给出明确提示
      const errorMsg = res.msg || res.message || '请求失败'
      if (errorMsg.includes('Token格式错误') || errorMsg.includes('权限不足')) {
        console.error('Token验证失败:', errorMsg)
        // 清除无效Token并跳转登录页
        localStorage.removeItem('adminToken')
        localStorage.removeItem('isAdminLoggedIn')
        // 延迟跳转，避免影响错误提示
        setTimeout(() => {
          window.location.href = '/login'
        }, 1000)
      }
      return Promise.reject(new Error(errorMsg))
    }
  },
  error => {
    console.error('响应拦截器错误:', error)
    // 处理401/403等权限错误
    if (error.response) {
      const { status } = error.response
      if (status === 401 || status === 403) {
        console.error('Token无效或过期，跳转登录页')
        localStorage.removeItem('adminToken')
        localStorage.removeItem('isAdminLoggedIn')
        setTimeout(() => {
          window.location.href = '/login'
        }, 1000)
      }
    }
    // 统一错误提示
    const errorMsg = error.message || '网络请求失败'
    return Promise.reject(new Error(errorMsg))
  }
)

export default request