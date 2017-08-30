import Vue from 'vue'
import axios from 'axios'
import qs from 'qs'
import config from './config'
require('es6-promise').polyfill()

import { ToastPlugin, dateFormat } from 'vux'
Vue.use(ToastPlugin)

axios.interceptors.request.use(config => {
  Vue.$vux.loading.show({
    text: 'Loading'
  })
  return config
}, error => {
  Vue.$vux.loading.hide()
  return Promise.reject(error)
})

axios.interceptors.response.use(response => {
  Vue.$vux.loading.hide()
  return response
}, error => {
  Vue.$vux.loading.hide()
  return Promise.reject(error)
})

function checkStatus (response) {
  // console.log(response)
  if (response.status === 200 || response.status === 304) {
    return response.data
  }
  return {
    data: {
      code: false,
      msg: response.statusText,
      data: response.statusText
    }
  }
}
function checkCode (res) {
  if (res.code === 500) {
    // Vue.$vux.toast.show({
    //   text: res.msg,
    //   type: 'warn'
    // })
  } else if (res.code === 200) {
    return res.data
  } else if (res.code === 300) {
    // Vue.$vux.toast.show({
    //   text: res.msg,
    //   type: 'warn'
    // })
    return res
  }
}

export default {
  post (url, data) {
    return axios({
      method: 'post',
      // url: config.api + url +'?token=otu_94d3ffdc5b5442263d8797e0d6138bb9',
      url: config.api + url + '?token=' + config.token,
      data: qs.stringify(data),
      timeout: config.timeout,
      headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
      }
    }).then(checkStatus).then(checkCode)
    // .then(checkStatus)
  },
  get (url, params) {
    return axios({
      method: 'get',
      url: config.api + url + '?token=' + config.token,
      params: qs.stringify(params),
      timeout: config.timeout,
      headers: {
        'X-Requested-With': 'XMLHttpRequest'
      }
    }).then(checkStatus).then(checkCode)
    // .then(checkStatus)
  },
  getDate (dateStr) {
    return dateFormat(new Date().setTime(dateStr), 'YYYY-MM-DD')
  },
  getFmtDate (dateStr, fmt = 'YYYY-MM-DD HH:mm:ss') {
    return dateFormat(new Date().setTime(dateStr), fmt)
  }
}
