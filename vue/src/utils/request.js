
import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import { getToken, removeToken } from '@/utils/auth'
import { base64encode } from '@/utils/code'
let oldCode = ''
// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 30 * 60 * 1000 // request timeout
})

// 传递唯一凭证的token
service.interceptors.request.use(
  config => {
    if (getToken()) {
      config.headers['AuthorizationUser'] = 'Bearer ' + getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(

  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom status
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  async response => {
    const res = response.data
    // 二进制数据则直接返回
    if (response.request.responseType === 'blob' || response.request.responseType === 'arraybuffer') {
      return res
    }

    // if the custom status is not 200, it is judged as an error.
    if (res.code !== 0) {
      if (res.code === 401 || res.msg === '未能获取对应StpLogic，type=user' || res.code === 402) {
        if (oldCode === res.code) return
        oldCode = res.code
        // 401-无登录权限  402-非系统有效用户
        const msg = res.code === 402 ? '非系统有效用户' : '登录状态已过期，您可以继续留在该页面，或者重新登录'
        MessageBox.confirm(msg, '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          removeToken()
          const redirectUrl = process.env.VUE_APP_REDIRECT_URL
          const webUrl = process.env.VUE_APP_WEB_URL
          const sysId = process.env.VUE_APP_SYS_ID
          // 重定向至单点登录
          const targetUrl = redirectUrl + '?casRedirectUrl=' + base64encode(webUrl) + '&sysId=' + sysId
          window.location.href = targetUrl
        }).catch(() => { })
      } else {
        Message({
          message: res.data.msg || 'Error',
          type: 'error'
        })
        return Promise.reject(new Error(res.msg || 'Error'))
      }
    } else {
      return res
    }
  },
  (error) => {
    const errorData = error?.response?.data
    Message({
      message: errorData.message,
      type: 'error',
      duration: 3 * 1000
    })
    return Promise.reject(error)
  })

export default service
