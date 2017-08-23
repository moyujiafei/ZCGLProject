<template>
  <div>
  </div>
</template>

<script>
import config from '@/api/config'
import api from '@/api'
import common from '@/components/common'
import { mapGetters } from 'vuex'
export default {
  components: {
  },
  mounted () {
    this.doDiapatch()
  },
  watch: {
    '$route': 'doDiapatch'
  },
  methods: {
    doDiapatch () {
      let _this = this
      let params = _this.$route.query
      if (params.goTest === '1') {
        _this.$router.replace({name: 'Test'})
        return false
      }
      if (params.fromMsg === '1') {
        _this.fromMsgAct(params)// 通过消息进入
      } else {
        if (params.doOauth === '1') {
          _this.$router.replace({name: 'Oauth', query: {appId: params.appId, token: params.token}})
        } else {
          _this.toOAuth()
        }
      }
    },
    fromMsgAct (params) {
      if (!config.token) {
        this.$vux.alert.show({content: '请先进入一次应用首页，完成网页授权'})
        return false
      }
      let action = params.action
      if (action) {
        if (action === 'rwxq') {
          // 任务详情
          this.$router.replace({name: 'Rwxq', query: {rwid: params.rwid}})
        } else if (action === 'userxq') {
          // 用户详情
          this.$router.replace({name: 'Userxq', query: {appId: params.appId, userId: params.userId}})
        } else if (action === 'zcxq') {
          // 资产详情
          this.$router.replace({name: 'Zcxq', query: {zcid: params.zcid}})
        }
      }
    },
    toOAuth () {
      // 网页授权
      let _this = this
      let appId = _this.$route.query.appId
      let errorMsg = '应用标识不能为空'
      if (!appId) {
        _this.$store.dispatch('hideMainTabbar')
        _this.$vux.alert.show({
          title: '错误',
          content: errorMsg
        })
        return false
      }
      api.post('/wx/oauth/checkToken.do', {appId: appId}).then(function (res) {
        if (res) {
          if (res === errorMsg) {
            _this.$vux.alert.show({
              title: '网页授权失败',
              content: res
            })
          } else if (res === '用户不存在,或未分配权限') {
            _this.$vux.alert.show({
              title: '网页授权失败',
              content: res
            })
          } else {
            // 授权成功
            let currTagName = ''
            if (_this.contains(res, common.tagNames.hqglryz)) {
              currTagName = common.tagNames.hqglryz
            } else if (_this.contains(res, common.tagNames.syrz)) {
              currTagName = common.tagNames.syrz
            } else if (_this.contains(res, common.tagNames.wxz)) {
              currTagName = common.tagNames.wxz
            } else if (_this.contains(res, common.tagNames.xjz)) {
              currTagName = common.tagNames.xjz
            } else if (_this.contains(res, common.tagNames.bmzcglyz)) {
              currTagName = common.tagNames.bmzcglyz
            } else {
              _this.$vux.alert.show({content: '无效的标签名'})
              return false
            }
            _this.$router.replace({name: 'NaviZc', query: {tagName: currTagName}})
          }
        } else {
          // window.location.href = config.api + '/wx/oauth/toOAuth.do?appId=' + appId
          window.location.href = config.api + '/wx/oauth/toOAuthTest.do?appId=' + appId + '&testToken=' + _this.testToken
        }
      })
    },
    contains (arr, obj) {
      let i = arr.length
      while (i--) {
        if (arr[i] === obj) {
          return true
        }
      }
      return false
    }
  },
  data () {
    return {
    }
  },
  computed: {
    ...mapGetters([
      'testToken'
    ])
  }
}
</script>

<style scoped>
</style>
