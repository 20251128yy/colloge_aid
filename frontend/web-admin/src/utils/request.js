import axios from 'axios'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('adminToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
// request.interceptors.response.use(
//   response => {
//     const res = response.data
//     if (res.success) {
//       return res.data
//     } else {
//       return Promise.reject(new Error(res.message || '请求失败'))
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
// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    console.log('响应数据:', res)
    
    // 检查code字段
    if (res.code === 200 || res.code === undefined) {
      // 有data字段返回data，没有返回整个res
      return res.data !== undefined ? res.data : res
    } else {
      // 后端返回错误码
      return Promise.reject(new Error(res.msg || res.message || '请求失败'))
    }
  },
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('adminToken')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)
export default request