<template>
  <div>
    <group title="资产状态记录">
      <div v-for="item in ztData">
        <table>
          <tbody>
            <tr>
              <td style="width: 68px" class="textTop dateCls">
                <h3 class="inlineStyle">{{item.jlsj | normalTime('DD')}}</h3><h5 class="inlineStyle ml">{{item.jlsj | normalTime('MM月')}}</h5>
              </td>
              <td width="5"> &nbsp;</td>
              <td>
                <table>
                  <tr>
                    <td style="width: 81px">
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
  </div>
</template>
<script>
  import api from '@/api'
  import { TransferDom, Group, Popup } from 'vux'
  export default {
    directives: {
      TransferDom
    },
    components: {
      Group,
      Popup
    },
    props: ['ztData', 'wxConfig'],
    methods: {
      showImg (item) {
        let _this = this
        if (_this.wxConfig) {
          let config = _this.wxConfig
          config['debug'] = false
          config['jsApiList'] = ['previewImage']
          let wx = _this.$wechat
          wx.config(config)
          wx.ready(function () {
            _this.showMediaDetail(_this, wx, item)
          })
          wx.error(function (res) {
          })
        }
      },
      showMediaDetail (_this, wx, item) {
        let mediaType = item.mediaType
        let ztid = item.ztid
        api.post('/wx/wxxq/ztxzInfo.do', {mediaType: mediaType, ztid: ztid}).then(function (res) {
          if (res) {
            if (mediaType === 'image') {
              wx.previewImage({
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
    data () {
      return {
        show: false,
        voiceUrl: '',
        voiceTile: ''
      }
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
.popup0 {
  padding-bottom:15px;
  height:100px;
}
</style>
