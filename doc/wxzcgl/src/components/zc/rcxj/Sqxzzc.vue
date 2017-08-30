<template>
  <div>
    <div>
      <x-header :left-options="{showBack: true}">申请闲置资产</x-header>
    </div>
    <grid>
      <grid-item label="选择照片" @on-item-click="chooseImage">
        <img slot="icon" src="../../../assets/icon_chooseImg.png">
      </grid-item>
      <grid-item label="预览照片" @on-item-click="previewImage">
        <img slot="icon" src="../../../assets/icon_preImg.png">
      </grid-item>
      <grid-item label="上传照片" @on-item-click="uploadImage">
        <img slot="icon" src="../../../assets/upload.png">
      </grid-item>
      <grid-item label="开始录音" v-show="isVoice" @on-item-click="startRecord">
        <img slot="icon" src="../../../assets/start_record.png">
      </grid-item>
      <grid-item label="停止录音" v-show="!isVoice" @on-item-click="stopRecord">
        <img slot="icon" src="../../../assets/stop_record.png">
      </grid-item>
      <grid-item label="播放录音" @on-item-click="playVoice">
        <img slot="icon" src="../../../assets/play_record.png">
      </grid-item>
      <grid-item label="上传录音" @on-item-click="uploadVoice">
        <img slot="icon" src="../../../assets/upload.png">
      </grid-item>
    </grid>

    <flexbox class="foot">
      <flexbox-item>
        <x-button type="primary" @click.native="resubmitWXSQ">确认</x-button>
      </flexbox-item>
      <flexbox-item>
        <x-button type="warn" @click.native="cancel">取消</x-button>
      </flexbox-item>
    </flexbox>

    <group title="申请原因：">
      <x-textarea :max="100" v-model="textValue"></x-textarea>
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
        images: {
          localId: [],
          serverId: []
        },
        voice: {
          localId: '',
          serverId: ''
        },
        config: {}
      }
    },
    computed: {
      wx: function () {
        let wx = this.$wechat
        this.config['debug'] = false
        this.config['jsApiList'] = [
          'startRecord',
          'stopRecord',
          'onVoiceRecordEnd',
          'playVoice',
          'pauseVoice',
          'stopVoice',
          'onVoicePlayEnd',
          'uploadVoice',
          'downloadVoice',
          'chooseImage',
          'previewImage',
          'uploadImage',
          'downloadImage'
        ]
        wx.config(this.config)
        wx.ready(function () {
          let _this = this
          wx.onVoiceRecordEnd({
            complete: function (res) {
              _this.voice.localId = res.localId
              _this.$vux.alert.show({content: '录音时间已超过一分钟'})
            }
          })
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
      resubmitWXSQ () {
        let _this = this
        let ids = _this.images.serverId
        let data = ''
        for (var i = 0; i < ids.length - 1; i++) {
          data = data + ids[i] + '###'
        }
        data = data + ids[ids.length - 1]
        if (ids.length === 0) {
          this.$vux.toast.show({
            type: 'warn',
            text: '请选择图片'
          })
          return
        }
        api.post('/wx/gzwx/submitXJXZSQ.do', {
          imgId: data,
          voiceId: _this.voice.serverId,
          zcId: _this.$route.query.zcId,
          sqRemark: _this.textValue
        }).then((response) => {
          if (response === 'success') {
            this.$vux.toast.show({
              text: '成功'
            })
            this.$router.go(-1)
          }
        })
          .catch((response) => {
            _this.$vux.toast.show({
              type: 'warn',
              text: '失败'
            })
          })
      },
      chooseImage () {
        let _this = this
        _this.wx.chooseImage({
          isShowProgressTips: 1, // 默认为1，显示进度提示
          success: function (res) {
            _this.images.localId = res.localIds
            _this.$vux.alert.show({content: '已选择 ' + res.localIds.length + ' 张图片'})
          }
        })
      },
      previewImage () {
        let _this = this
        _this.wx.previewImage({
          current: _this.images.localId[0],
          urls: _this.images.localId
        })
      },
      uploadImage () {
        let _this = this
        if (_this.images.localId.length === 0) {
          _this.$vux.alert.show({content: '请先选择图片'})
          return
        }
        var i = 0
        var length = _this.images.localId.length
        _this.images.serverId = []
        function upload () {
          _this.wx.uploadImage({
            localId: _this.images.localId[i],
            success: function (res) {
              i++
              _this.images.serverId.push(res.serverId)
              if (i < length) {
                upload()
              }
            },
            fail: function (res) {
              alert(JSON.stringify(res))
            }
          })
        }

        upload()
      },
      upload () {
      },
      startRecord () {
        this.isVoice = !this.isVoice
        let _this = this
        this.wx.startRecord({
          cancel: function () {
            _this.$vux.alert.show({content: '用户拒绝授权录音'})
          }
        })
      },
      stopRecord () {
        let _this = this
        this.isVoice = !this.isVoice
        this.wx.stopRecord({
          success: function (res) {
            _this.voice.localId = res.localId
          },
          fail: function (res) {
            alert(JSON.stringify(res))
          }
        })
      },
      playVoice () {
        let _this = this
        if (_this.voice.localId === '') {
          _this.$vux.alert.show({content: '请先录制一段声音'})
          return
        }
        this.wx.playVoice({
          localId: _this.voice.localId
        })
      },
      uploadVoice () {
        let _this = this
        if (_this.voice.localId === '') {
          _this.$vux.alert.show({content: '请先录制一段声音'})
          return
        }
        _this.wx.uploadVoice({
          localId: _this.voice.localId,
          success: function (res) {
            _this.voice.serverId = res.serverId
          }
        })
      }
    },
    mounted: function () {
      console.log('加载')
      api.post('/wx/common/getWXConfig.do', {
        url: location.href
      }).then((response) => {
        this.config = response
      })
        .catch((response) => {
          console.log(response)
        })
    },
    beforeRouteEnter (to, from, next) {
      next(vm => {
        vm.$store.dispatch('hideMainTabbar')
      })
    },
    beforeRouteLeave (to, from, next) {
      this.$store.dispatch('showMainTabbar')
      next()
    }
  }
</script>


<style scoped>
  .foot {
    position: fixed;
    left: 0;
    height: 42px;
    line-height: 42px;
    width: 100%;
  }

  .foot {
    bottom: 0;
  }
</style>
