import axios from 'axios'
import router from '@/router/routers'
import { Notification, MessageBox } from 'element-ui'
import store from '../store'
import { getToken } from '@/utils/auth'
import Config from '@/settings'
import Cookies from 'js-cookie'
import Settings from '../settings'
// 创建axios实例
const service = axios.create({
  baseURL: Settings.apiPrefix, // api 的 base_url
  timeout: Config.timeout // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
  config => {
    if (getToken()) {
      config.headers['Authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    config.headers['Content-Type'] = 'application/json'
    return config
  },
  error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    let responseData = response.data
    if (response.request.responseType !== 'blob') {
      responseData = responseData.data
    }
    return responseData
  },
  error => {
    // 兼容blob下载出错json提示
    if (error.response.data instanceof Blob && error.response.data.type.toLowerCase().indexOf('json') !== -1) {
      const reader = new FileReader()
      reader.readAsText(error.response.data, 'utf-8')
      reader.onload = function(e) {
        const errorMsg = JSON.parse(reader.result).message
        Notification.error({
          title: errorMsg,
          duration: 5000
        })
      }
    } else {
    let code = 0
    try {
      code = error.response.status
    } catch (e) {
      if (error.toString().indexOf('Error: timeout') !== -1) {
        Notification.error({
          title: '网络请求超时',
          duration: 5000
        })
        return Promise.reject(error)
      }
    }
    if (code) {
      if (code === 502) {
        router.push('/502')
      } else if (code === 401) {
        MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }
        ).then(() => {
          store.dispatch('LogOut').then(() => {
            Cookies.set('point', 401)
            location.href = '/'
          })
        })
      } else {
        const errorMsg = error.response.data.message
        if (errorMsg !== undefined) {
          if (code === 400) {
            Notification.warning({
              title: errorMsg,
              duration: 5000
            })
          } else {
            Notification.error({
              title: errorMsg,
              duration: 5000
            })
          }
        }
      }
    } else {
      Notification.error({
        title: '接口请求失败',
        duration: 5000
      })
    }
    }
    return Promise.reject(error)
  }
)

export function getBaseHeader() {
  return {
    'Authorization': getToken()
  }
}

export default service
