<template>
  <div>
    <div>
      <x-header :left-options="{showBack: true}">申请维修资产</x-header>
    </div>
    <x-button type="primary" @click.native="scan">确认</x-button>



    <flexbox class="foot">
      <flexbox-item>
        <x-button type="primary" @click.native="getNetworkType">确认</x-button>
      </flexbox-item>
      <flexbox-item>
        <x-button type="warn" @click.native="cancel">取消</x-button>
      </flexbox-item>
    </flexbox>


    <group title="申请原因：">
      <x-textarea :max="100" v-model="textValue" ></x-textarea>
    </group>


  </div>
</template>



<script>
  import { Grid, GridItem, GroupTitle, XTextarea, Group, XHeader, Flexbox, FlexboxItem, XButton } from 'vux'
  import api from '@/api/index'
  export default {
    data () {
      return {
        isVoice: true,
        textValue: '',
        networkType: '',
        config: {}
      }
    },
    computed: {
      wx: function () {
        let wx = this.$wechat
        this.config['debug'] = false
        this.config['jsApiList'] = [
          'scanQRCode'
        ]
        wx.config(this.config)
        wx.ready(function () {
        })
        return wx
      }
    },
    components: {
      Grid,
      GridItem,
      GroupTitle,
      XHeader,
      Flexbox,
      FlexboxItem,
      XButton,
      Group,
      XTextarea
    },
    methods: {
      cancel () {
        this.$router.go(-1)
      },
      scan () {
        let _this = this
        this.wx.scanQRCode({
          desc: 'scanQRCode desc',
          needResult: 1, // 默认为0，扫描结果由企业微信处理，1则直接返回扫描结果，
          scanType: ['qrCode', 'barCode'], // 可以指定扫二维码还是一维码，默认二者都有
          success: function (res) {
            // 回调
            var json = {}
            try {
              json = JSON.parse(res.resultStr)
              json = json.zcdm
              if (json == null || json === '') {
                json = res.resultStr
              }
            } catch (error) {
              json = res.resultStr
            }
            if (json.length > 35) {
              _this.$vux.alert.show({content: '非法信息'})
              return
            }
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
    },
    mounted: function () {
      console.log('加载')
      api.post('/wx/common/getWXConfig.do', {
        url: location.href,
        appId: this.$route.query.appId
      }).then((response) => {
        this.config = response
      })
        .catch((response) => {
          console.log(response)
        })
    }
  }
</script>


<style scoped>
  .foot{position:fixed;left:0;height:42px;line-height:42px;width:100%;}
  .foot{bottom:0px;}
</style>
