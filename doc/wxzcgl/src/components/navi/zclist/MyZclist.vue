<template>
  <div>
    <div>
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
        <tbody>
          <tr>
            <td class="qrcodeTd"><div class="qrcodeBtn" @click="scanQrcode"></div></td>
            <td>
              <search placeholder="资产代码" :auto-fixed="false" v-model="searchParam" @on-submit="doSearch" ref="search"></search>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-if="doSearchFlag">
      <Pending v-show="doSearchFlag" :tag-name="tagName" :tab-index="1" :do-search-index="doSearchIndex" :search-param="searchParam"></Pending>
    </div>
    <div v-show="!doSearchFlag">
      <panel :footer="footer" :list="zcListData" :type="type" @on-click-footer="loadZcListData" @on-click-item="toZcdetail" ></panel>
    </div>
  </div>
</template>
<script>
  import api from '@/api'
  import { Panel, Search } from 'vux'
  import common from '@/components/common'
  import Pending from '@/components/navi/gz_detail/Pending.vue'
  export default {
    components: {
      Panel,
      Search,
      Pending
    },
    props: ['tagName'],
    data () {
      return {
        rows: common.pageRowNo,
        page: 1,
        type: '1',
        zcListData: [],
        wxConfig: {},
        footer: {
          title: '查看更多'
        },
        searchParam: '',
        doSearchFlag: false,
        doSearchIndex: 1
      }
    },
    watch: {
      searchParam: function () {
        if (!this.searchParam) {
          this.doSearchFlag = false
        }
      }
    },
    mounted () {
      this.getWxConfig()
      this.loadZcListData()
    },
    methods: {
      doSearch () {
        this.doSearchFlag = true
        this.doSearchIndex++
      },
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
        let wxObj = _this.$wechat
        wxObj.config(_this.wxConfig)
        wxObj.scanQRCode({
          desc: '扫描设备二维码',
          needResult: 1, // 默认为0，扫描结果由企业微信处理，1则直接返回扫描结果，
          scanType: ['qrCode', 'barCode'], // 可以指定扫二维码还是一维码，默认二者都有
          success: function (res) {
            let reslt = common.scanCodeInfo(res)
            if (reslt) {
              _this.searchParam = reslt
              _this.doSearch()
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
      },
      loadZcListData () {
        let _this = this
        let param = {
          rows: _this.rows,
          tagName: _this.tagName,
          page: _this.page
        }
        if (_this.searchParam && _this.searchParam.length > 0) {
          param['zcdm'] = _this.searchParam
        }
        let url = '/wx/zcgl/getMyZCList.do'
        if (url) {
          api.post(url, param).then(function (res) {
            if (res) {
              // 添加资产列表
              let zcList = res.zcList
              _this.zcListData.push.apply(_this.zcListData, zcList)
              _this.page++
              // 设置统计数量
              if (res.countNo) {
                _this.$store.dispatch('setGzCount', res.countNo.toString())
              }
              console.log(_this.page)// 此处的log不要删除
            } else {
              _this.$vux.toast.text('没有更多数据', 'bottom')
            }
          })
        }
      },
      toZcdetail (item) {
        this.$store.dispatch('zcxqShowBack')
        this.$router.push({name: 'ZcxqNav', query: {zcid: item.zcid}})
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
