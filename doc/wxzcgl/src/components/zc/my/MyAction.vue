<template>
  <div>
    <div>
      <x-header :left-options="{showBack: true}">我的操作</x-header>
    </div>
    <group>
      <div v-for="item in ztData">
        <table>
          <tbody>
          <tr>
            <td class="textTop dateCls">
              <h3 class="inlineStyle">{{item.jlsj | normalTime('DD')}}</h3><h5 class="inlineStyle ml">{{item.jlsj | normalTime('MM月')}}</h5>
            </td>
            <td width="5"> &nbsp;</td>
            <td>
              <table>
                <tr>
                  <td>
                    <img :src="item.smallImageUrl" alt="" height="80" width="80" @click="showImg(item)">
                  </td>
                  <td class="textTop">
                    <p>{{item.remark}}</p>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </group>
    <div v-transfer-dom>
      <popup v-model="show" @on-hide="stopPlay()">
        <div class="popup0">
          <group :title="voiceTile">
            <audio id="ztVoice" :src="voiceUrl" controls="controls">音频</audio>
          </group>
        </div>
      </popup>
    </div>
    <div>
      <x-button type="primary" @click.native="getMore">加载更多</x-button>
    </div>
  </div>
</template>
<script>
  import { TransferDom, XHeader, Popup, Group, XButton } from 'vux'
  import api from '@/api/index'
  export default {
    components: {
      Popup,
      Group,
      XHeader,
      XButton
    },
    directives: {
      TransferDom
    },
    computed: {
      wx: function () {
        let wx = this.$wechat
        this.config['debug'] = false
        this.config['jsApiList'] = [
          'playVoice',
          'pauseVoice',
          'stopVoice',
          'onVoicePlayEnd',
          'downloadVoice',
          'previewImage',
          'downloadImage'
        ]
        wx.config(this.config)
        return wx
      }
    },
    methods: {
      getMore () {
        let _this = this
        api.post('/wx/action/getVZTList.do', {
          rows: _this.rows,
          page: _this.page
        }).then((response) => {
          _this.page++
          _this.ztData = _this.ztData.concat(response)
        })
      },
      showImg (item) {
        let _this = this
        let _wx = _this.wx
        _this.showMediaDetail(_this, _wx, item)
      },
      showMediaDetail (_this, _wx, item) {
        let mediaType = item.mediaType
        let ztid = item.ztid
        api.post('/wx/wxxq/ztxzInfo.do', {mediaType: mediaType, ztid: ztid}).then(function (res) {
          if (res) {
            if (mediaType === 'image') {
              _wx.previewImage({
                current: res[0], // 当前显示图片的http链接
                urls: res // 需要预览的图片http链接列表
              })
            } else if (mediaType === 'voice') {
              _this.showVoice(res, item.jlsj)
            }
          }
        })
      },
      showVoice (voiceUrl, jlsj) {
        this.voiceUrl = voiceUrl[0]
        if (jlsj) this.voiceTile = api.getFmtDate(jlsj, 'YYYY年MM月DD日')
        this.show = !this.show
      },
      stopPlay () {
        let audio = document.getElementById('ztVoice')
        if (audio) audio.pause()
      }
    },
    beforeRouteEnter (to, from, next) {
      next(vm => {
        vm.$store.dispatch('hideMainTabbar')
      })
    },
    beforeRouteLeave (to, from, next) {
      this.$store.dispatch('showMainTabbar')
      next()
    },
    data () {
      return {
        show: false,
        voiceUrl: '',
        voiceTile: '',
        config: {},
        page: 1,
        rows: 10,
        ztData: []
      }
    },
    mounted: function () {
      console.log('加载')
      api.post('/wx/common/getWXConfig.do', {
        url: location.href
      }).then((response) => {
        this.config = response
      })
      api.post('/wx/action/getVZTList.do', {
        rows: this.rows,
        page: this.page
      }).then((response) => {
        this.ztData = response
        this.page += 1
      })
    }
  }
</script>
<style scoped>
  .textTop{
    vertical-align: top;
  }
  .textTop p{
    font-size: 14px;
    font-size: 0.88rem;
  }
  .dateCls{
    width: 68px;
  }
  .inlineStyle{
    display: inline;
  }
  .ml{
    margin-left: 2px;
  }
</style>
