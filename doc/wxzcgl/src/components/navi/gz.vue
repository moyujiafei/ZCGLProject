<template>
  <div>
    <x-header :left-options="{showBack: showBack}" :right-options="{showMore: true}" @on-click-more="showMenus = true">我的工作</x-header>
    <div class="zclistPanel">
      <Zclist :tag-name="tagName"></Zclist>
    </div>
    <div v-transfer-dom>
      <actionsheet :menus="menus" v-model="showMenus" show-cancel @on-click-menu="doAction"></actionsheet>
    </div>
  </div>
</template>
<script>
import api from '@/api/index'
import { XHeader, Actionsheet, TransferDom } from 'vux'
import Zclist from './zclist/Zclist.vue'
import common from '@/components/common'
export default {
  directives: {
    TransferDom
  },
  components: {
    XHeader,
    Actionsheet,
    Zclist
  },
  computed: {
    tagName () {
      return this.$route.query.tagName
    }
  },
  data () {
    return {
      showBack: false,
      showMenus: false,
      menus: {
        menu_zcdj: '资产登记'
      },
      wxConfig: {}
    }
  },
  mounted () {
    this.getWxConfig()
  },
  methods: {
    getWxConfig () {
      let _this = this
      api.post('/wx/common/getWXConfig.do', {url: document.URL}).then(function (res) {
        if (res) {
          _this.wxConfig = res
          _this.wxConfig['debug'] = false
          _this.wxConfig['jsApiList'] = ['scanQRCode']
        }
      })
    },
    doAction (key) {
      let _this = this
      if (key === 'menu_zcdj') {
        let wxObj = _this.$wechat
        wxObj.config(_this.wxConfig)
        _this.scanQRCode(wxObj)
      }
    },
    scanQRCode (wxObj) {
      let _this = this
      wxObj.scanQRCode({
        desc: '扫描设备二维码',
        needResult: 1, // 默认为0，扫描结果由企业微信处理，1则直接返回扫描结果，
        scanType: ['qrCode', 'barCode'], // 可以指定扫二维码还是一维码，默认二者都有
        success: function (res) {
          let json = common.scanCodeInfo(res)
          if (json) {
            api.post('/wx/zczt/isExist.do', {
              dm: json
            }).then((response) => {
              if (response == null) {
                _this.$router.push({name: 'Zcdj', query: {zcdm: json}})
              } else {
                _this.$vux.alert.show({
                  title: '提示',
                  content: '资产已登记！'
                })
                _this.$store.dispatch('zcxqShowBack')
                _this.$router.push({name: 'ZcxqNav', query: {zcid: response.id}})
              }
            })
              .catch((response) => {
                console.log(response)
              })
          } else {
            _this.$vux.alert.show({content: '非法信息'})
          }
        },
        error: function (res) {
          if (res.errMsg.indexOf('function_not_exist') > 0) {
            _this.$vux.alert.show({
              title: '提示',
              content: '版本过低请升级！'
            })
          }
        }
      })
    }
  }
}
</script>
<style scoped>
.zclistPanel{
  margin-bottom: 53px;
}
</style>
