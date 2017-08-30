import Vue from 'vue'
import FastClick from 'fastclick'

import { ToastPlugin, LoadingPlugin, AlertPlugin, ConfirmPlugin, WechatPlugin } from 'vux'
Vue.use(ToastPlugin)
Vue.use(LoadingPlugin)
Vue.use(AlertPlugin)
Vue.use(ConfirmPlugin)
Vue.use(WechatPlugin)
// 点击延时
FastClick.attach(document.body)
