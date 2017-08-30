import Vue from 'vue'
import App from './App'
import router from './router'
import './utils/main'
// mintUI
// import 'mint-ui/lib/style.css'
// import { InfiniteScroll } from 'mint-ui'
// Vue.use(InfiniteScroll)
// 过滤器
import filters from './filters'
Object.keys(filters).forEach((key) => Vue.filter(key, filters[key]))

require('es6-promise').polyfill()
// vuex
import { sync } from 'vuex-router-sync'
import Vuex from 'vuex'
import navi from './store/modules/navi'
import zcxq from './store/modules/zcxq'
import login from './store/modules/login'
Vue.use(Vuex)
const store = new Vuex.Store({
  modules: {}
})
store.registerModule('vux', {
  state: {
    isLoading: false,
    direction: 'forward'
  },
  mutations: {
    // 加载动画
    updateLoadingStatus (state, payload) {
      state.isLoading = payload.isLoading
    },
    // 切换动画
    updateDirection (state, payload) {
      state.direction = payload.direction
    }
  },
  actions: {
  },
  getters: {
    getDirection (state) {
      return state.direction
    }
  },
  modules: {
    // 允许将单一的 Store 拆分为多个 Store 的同时保存在单一的状态树中
    navi,
    zcxq,
    login
  }
})
Vue.use(store)
sync(store, router)

const history = window.sessionStorage
history.clear()
var historyCount = history.getItem('count') * 1 || 0
history.setItem('/', 0)
// 使用 router.beforeEach 注册一个全局的 before 钩子：
router.beforeEach((to, from, next) => {
  if (!window.localStorage.token) {
    store.commit('hideMainTabbar')
  }
  // window.scrollTo(0, 0)
  store.commit('updateLoadingStatus', {isLoading: true})
  const toIndex = history.getItem(to.path)
  const fromIndex = history.getItem(from.path)

  if (toIndex) {
    if (!fromIndex || parseInt(toIndex, 10) > parseInt(fromIndex, 10) || toIndex === '0' && fromIndex === '0') {
      store.commit('updateDirection', {direction: 'reverse'})
    } else {
      store.commit('updateDirection', {direction: 'forward'})
    }
  } else {
    ++historyCount
    history.setItem('count', historyCount)
    if (to.path !== '/' && to.path !== history.setItem(to.path, historyCount)) {
      store.commit('updateDirection', {direction: 'reverse'})
    }
  }

  if (/\/http/.test(to.path)) {
    var url = to.path.split('http')[1]
    window.location.href = `http${url}`
  } else {
    next()
  }
})
router.afterEach(function (to) {
  // setTimeout(() => {
  //   store.commit('updateLoadingStatus', {isLoading: false})
  // }, 200)
})

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
