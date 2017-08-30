import Vue from 'vue'
import Router from 'vue-router'
import Welcome from '@/components/Welcome'
import Rwxq from '@/components/xq/rwxq/Rwxq'
import Zcxq from '@/components/xq/zcxq/Zcxq'
import Userxq from '@/components/xq/userxq/Userxq'
import Test from '@/components/Test'
import TestEnter from '@/components/TestEnter'
// import test from '@/components/zc/wxxj/test'
// 资产登记
import Zcdb from '@/components/zc/zcdj/Zcdb'
import Zccxdb from '@/components/zc/zcdj/Zccxdb'
import Zcjjgh from '@/components/zc/zcdj/Zcjjgh'
import zcdjTest from '@/components/zc/zcdj/zcdjTest'
// 故障维修
import Sqbfzc from '@/components/zc/gzwx/Sqbfzc'
import Sqxzzc from '@/components/zc/gzwx/Sqxzzc'
import Sqzcwx from '@/components/zc/gzwx/Sqzcwx'
import gzwxTest from '@/components/zc/gzwx/gzwxTest'
import Wcwx from '@/components/zc/gzwx/Wcwx'
// 日常巡检
import XjSqbfzc from '@/components/zc/rcxj/Sqbfzc'
import XjSqwxzc from '@/components/zc/rcxj/Sqwxzc'
import XjSqxzzc from '@/components/zc/rcxj/Sqxzzc'
import XjWcrcxj from '@/components/zc/rcxj/Wcrcxj'
import rcxjTest from '@/components/zc/rcxj/rcxjTest'

import JssdkTest from '@/components/zc/JssdkTest'
// 我的
import MySetting from '@/components/zc/my/MySetting'
import MyAbout from '@/components/zc/my/MyAbout'
import MyMsg from '@/components/zc/my/MyMsg'
import MyUpdate from '@/components/zc/my/MyUpdate'
import MyConcat from '@/components/zc/my/MyConcat'
import MyAction from '@/components/zc/my/MyAction'
//  资产运维
import agreeBFSQ from '@/components/zc/zcyw/agreeBFSQ'
import agreeBFZC from '@/components/zc/zcyw/agreeBFZC'
import agreeXZSQ from '@/components/zc/zcyw/agreeXZSQ'
import refuseBFSQ from '@/components/zc/zcyw/refuseBFSQ'
import refuseWXSQ from '@/components/zc/zcyw/refuseWXSQ'
import refuseXZSQ from '@/components/zc/zcyw/refuseXZSQ'
import ZcywTest from '@/components/zc/zcyw/ZcywTest'
//  资产分配（部门资产管理员组）
import Sqgh from '@/components/zc/zcfp/Sqgh'
import Zccxfp from '@/components/zc/zcfp/Zccxfp'
import Zcfp from '@/components/zc/zcfp/Zcfp'
import Jjzcsj from '@/components/zc/zcfp/Jjzcsj'
import ZcfpTest from '@/components/zc/zcfp/ZcfpTest'
//  资产使用(使用人组)
import Jjlyzc from '@/components/zc/syzc/Jjlyzc'
import Sjzc from '@/components/zc/syzc/Sjzc'
import SyrSqwxzc from '@/components/zc/syzc/Sqwxzc'
import SyrSqxzzc from '@/components/zc/syzc/Sqxzzc'
import SyrSqbfzc from '@/components/zc/syzc/Sqbfzc'
// 登录验证
import Oauth from '@/components/oauth/Oauth'
// 主导航
import NaviZc from '@/components/navi/zc'
import NaviGz from '@/components/navi/gz'
import NaviMy from '@/components/navi/my'
import ZcxqNav from '@/components/navi/zclist/ZcxqNav'
import Zcdj from '@/components/zc/Zcdj'

Vue.use(Router)

export default new Router({
  mode: 'history',
  scrollBehavior (to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { x: 0, y: 0 }
    }
  },
  routes: [
    { path: '*', component: Welcome },
    { name: 'Welcome', path: '/', component: Welcome },
    { name: 'Rwxq', path: '/xq/rwxq', component: Rwxq, meta: {keepAlive: true} },
    { name: 'Zcxq', path: '/xq/zcxq', component: Zcxq },
    { name: 'Userxq', path: '/xq/userxq', component: Userxq },

    { name: 'Test', path: '/test', component: Test },
    { name: 'TestEnter', path: '/testEnter', component: TestEnter },
    { name: 'Oauth', path: '/oauth/saveToken', component: Oauth },

    {
      name: 'NaviZc',
      path: '/zc',
      component: NaviZc,
      meta: {
        keepAlive: true
      }
    },
    {
      name: 'NaviGz',
      path: '/gz',
      component: NaviGz,
      meta: {
        keepAlive: true
      }
    },
    {
      name: 'NaviMy',
      path: '/my',
      component: NaviMy
    },
    { name: 'ZcxqNav', path: '/xq', component: ZcxqNav },
    // 资产调拨
    { name: 'Zcdb', path: '/wx/zcdj/allocateZC', component: Zcdb },
    { name: 'Zccxdb', path: '/wx/zcdj/reallocateZC', component: Zccxdb },
    { name: 'Zcjjgh', path: '/wx/zcdj/refuseRevertZC', component: Zcjjgh },
    // 日常巡检
    { name: 'XjSqbfzc', path: '/wx/rcxj/zcbfSQ', component: XjSqbfzc },
    { name: 'XjSqwxzc', path: '/wx/rcxj/zcwxSQ', component: XjSqwxzc },
    { name: 'XjSqxzzc', path: '/wx/rcxj/zcxzSQ', component: XjSqxzzc },
    { name: 'XjWcrcxj', path: '/wx/rcxj/finishRCXJ', component: XjWcrcxj },
    // 故障维修
    { name: 'Wcwx', path: '/wx/gzwx/finishGZWX', component: Wcwx },
    { name: 'Sqbfzc', path: '/wx/gzwx/zcbfSQ', component: Sqbfzc },
    { name: 'Sqxzzc', path: '/wx/gzwx/zcxzSQ', component: Sqxzzc },
    { name: 'Sqzcwx', path: '/wx/gzwx/resubmitWXSQ', component: Sqzcwx },
    // test页面
    { name: 'rcxjTest', path: '/wx/rcxj/Sqzcwx', component: rcxjTest },
    { name: 'JssdkTest', path: '/wx/rcxj/JssdkTest', component: JssdkTest },
    { name: 'zcdjTest', path: '/wx/zcdj/zcdjTest', component: zcdjTest },
    { name: 'gzwxTest', path: '/wx/zcsy/gzwxTest', component: gzwxTest },
    { name: 'ZcfpTest', path: '/wx/zcfp/ZcfpTest', component: ZcfpTest },
    { name: 'ZcywTest', path: '/wx/zcyw/ZcywTest', component: ZcywTest },
    // 我的
    { name: 'MySetting', path: '/wx/my/MySetting', component: MySetting },
    { name: 'MyAbout', path: '/wx/my/MyAbout', component: MyAbout },
    { name: 'MyMsg', path: '/wx/my/MyMsg', component: MyMsg },
    { name: 'MyUpdate', path: '/wx/my/MyUpdate', component: MyUpdate },
    { name: 'MyConcat', path: '/wx/my/MyConcat', component: MyConcat },
    { name: 'MyAction', path: '/wx/my/MyAction', component: MyAction },
    // 使用人
    { name: 'Jjlyzc', path: '/wx/zcsy/refuseLeadingZC', component: Jjlyzc },
    { name: 'Sjzc', path: '/wx/zcsy/sendbackZC', component: Sjzc },
    { name: 'SyrSqwxzc', path: '/wx/zcsy/zcwxSQ', component: SyrSqwxzc },
    { name: 'SyrSqbfzc', path: '/wx/zcsy/zcbfSQ', component: SyrSqbfzc },
    { name: 'SyrSqxzzc', path: '/wx/zcsy/zcxzSQ', component: SyrSqxzzc },
    // 资产分配
    { name: 'Sqgh', path: '/wx/zcfp/revertZC', component: Sqgh },
    { name: 'Zccxfp', path: '/wx/zcfp/reassignZC', component: Zccxfp },
    { name: 'Zcfp', path: '/wx/zcfp/assignZC', component: Zcfp },
    { name: 'Jjzcsj', path: '/wx/zcfp/refuseSendbackZC', component: Jjzcsj },
    // 资产运维
    { name: 'agreeBFSQ', path: '/wx/zcyw/agreeBFSQ', component: agreeBFSQ },
    { name: 'agreeBFZC', path: '/wx/zcyw/agreeBFZC', component: agreeBFZC },
    { name: 'agreeXZSQ', path: '/wx/zcyw/agreeXZSQ', component: agreeXZSQ },
    { name: 'refuseBFSQ', path: '/wx/zcyw/refuseBFSQ', component: refuseBFSQ },
    { name: 'refuseWXSQ', path: '/wx/zcyw/refuseWXSQ', component: refuseWXSQ },
    { name: 'refuseXZSQ', path: '/wx/zcyw/refuseXZSQ', component: refuseXZSQ },
    { name: 'Zcdj', path: '/wx/zc/Zcdj', component: Zcdj }
  ]
})
