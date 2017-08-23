<template>
  <div>
    <!-- <div>
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
        <tbody>
          <tr>
            <td class="qrcodeTd"><div class="qrcodeBtn" @click="scanQrcode"></div></td>
            <td>
              <search placeholder="资产代码" :auto-fixed="false" v-model="searchParam" @on-submit="onSubmit" ref="search"></search>
            </td>
          </tr>
        </tbody>
      </table>
    </div> -->
    <div>
      <div>
        <tab v-model="tabIndex">
          <tab-item class="vux-center" :selected="tabIndex === index" v-for="(item, index) in tabListLv1" :key="index" @on-item-click="onItemClick">{{item}}</tab-item>
        </tab>
      </div>
      <div v-for="(item, index) in tabListLv1" :key="index">
        <transition :name="'vux-pop-' + (index === 0 ? 'out' : 'in')">
          <Pending v-show="tabIndex === index" :tag-name="tagName" :tab-index="index"  ></Pending>
        </transition>
      </div>
    </div>
  </div>
</template>
<script>
  import api from '@/api'
  import Pending from '@/components/navi/gz_detail/Pending.vue'
  import { Tab, TabItem } from 'vux'
  export default {
    components: {
      Tab,
      TabItem,
      Pending
    },
    props: ['tagName'],
    data () {
      return {
        tabListLv1: ['待处理', '全部'],
        wxConfig: {},
        tabIndex: 0,
        direction: 'forward'
      }
    },
    mounted () {
      // this.getWxConfig()
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
      scanQrcode () {
        // 扫描设备二维码
        let _this = this
        let wxObj = this.$wechat
        wxObj.config(this.wxConfig)
        if (wxObj) {
          wxObj.scanQRCode({
            desc: '扫描设备二维码',
            needResult: 1, // 默认为0，扫描结果由企业微信处理，1则直接返回扫描结果，
            scanType: ['qrCode', 'barCode'], // 可以指定扫二维码还是一维码，默认二者都有
            success: function (res) {
              if (res.resultStr) {
                _this.searchParam = res.resultStr
              }
            },
            error: function (res) {
            }
          })
        }
      },
      onItemClick (index) {
        if (index !== this.tabIndex) {
          this.tabIndex = index
        }
      }
    }
  }
</script>
<style scoped>
.qrcodeTd {
  text-align: center;
  width: 40px;
  background-color: #efeff4;
}
.qrcodeBtn {
  width: 32px;
  height: 32px;
  margin: 0 auto;
  background-image: url('../../../assets/qrcode.png');
}
.qrcodeBtn:before{
  background-image: url('../../../assets/qrcode.png');
}
.qrcodeBtn:active{
  background-image: url('../../../assets/qrcode_on.png');
}
.flex-demo {
  text-align: center;
  border: 1px solid #eee;
}
</style>
