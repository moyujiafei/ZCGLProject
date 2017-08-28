<template>
  <div>
    <div>
      <x-header :left-options="{showBack: true}">资产登记</x-header>
    </div>
    <grid>
      <grid-item label="选择照片" @on-item-click="chooseImage">
        <img slot="icon" src="../../assets/icon_chooseImg.png">
      </grid-item>
      <grid-item label="预览照片" @on-item-click="previewImage">
        <img slot="icon" src="../../assets/icon_preImg.png">
      </grid-item>
      <grid-item label="上传照片" @on-item-click="uploadImage">
        <img slot="icon" src="../../assets/upload.png">
      </grid-item>
    </grid>
    <div class="aaa">
    <group>
    <x-input title="资产代码：" disabled v-model="zcdm"></x-input>
      <x-input title="资产名称：" v-model="zcmc" required type="text"></x-input>
      <popup-picker title="资产类型：" required :data="zclxList" v-model="zclx" :columns="2"  show-name></popup-picker>
      <x-input title="单价：" required v-model="price" type="number"></x-input>
      <x-input title="数量：" required v-model="nums" type="number"></x-input>
      <x-input title="规格型号：" v-model="ggxh" type="text"></x-input>
      <x-input title="出厂编号：" v-model="ccbh" type="text"></x-input>
      <datetime title="购置时间：" show v-model="gzsj" @on-change="timeChange"></datetime>
      <x-input title="折旧年限：" v-model="zjnx" type="number"></x-input>
    </group>
    <flexbox class="foot">
      <flexbox-item>
        <x-button type="primary" @click.native="sumbitZcdj">确认</x-button>
      </flexbox-item>
      <flexbox-item>
        <x-button type="warn" @click.native="cancel">取消</x-button>
      </flexbox-item>
    </flexbox>
    </div>
  </div>
</template>


<script>
  /* eslint-disable yoda */

  import { Flexbox, PopupPicker, FlexboxItem, Grid, Datetime, GridItem, XInput, GroupTitle, Group, XHeader, XButton } from 'vux'
  import api from '@/api/index'
  export default {
    data () {
      return {
        zcdm: '',
        zcmc: '',
        price: 0,
        nums: 1,
        ggxh: '',
        ccbh: '',
        gzsj: '',
        zjnx: '',
        zclxList: [],
        zclx: [],
        isVoice: true,
        value: 0,
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
          'chooseImage',
          'previewImage',
          'uploadImage',
          'downloadImage'
        ]
        wx.config(this.config)
        return wx
      }
    },
    components: {
      Grid,
      GridItem,
      Datetime,
      PopupPicker,
      Flexbox,
      GroupTitle,
      XHeader,
      FlexboxItem,
      XButton,
      Group,
      XInput
    },
    methods: {
      cancel () {
        this.$router.go(-1)
      },
      dd () {
        alert(this.$route.query.zcdm)
      },
      timeChange () {
        console.log(this.gzsj)
      },
      sumbitZcdj () {
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
        api.post('/wx/gzwx/submitBFSQ.do', {
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
          count: 1, // 默认9
          isShowProgressTips: 1, // 默认为1，显示进度提示
          success: function (res) {
            _this.images.localId = res.localIds
            alert('已选择 ' + res.localIds.length + ' 张图片')
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
          this.$vux.toast.show({
            type: 'warn',
            text: '请选择图片'
          })
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
      formatDate (date, fmt) {
        if (/(y+)/.test(fmt)) {
          fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
        }
        let o = {
          'M+': date.getMonth() + 1,
          'd+': date.getDate(),
          'h+': date.getHours(),
          'm+': date.getMinutes(),
          's+': date.getSeconds()
        }
        for (let k in o) {
          if (new RegExp(`(${k})`).test(fmt)) {
            let str = o[k] + ''
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : this.padLeftZero(str))
          }
        }
        return fmt
      },
      padLeftZero (str) {
        return ('00' + str).substr(str.length)
      }
    },
    mounted: function () {
      var temp = this.formatDate(new Date(), 'yyyy-MM-dd')
      this.gzsj = temp
      this.zcdm = this.$route.query.zcdm
      console.log(this.gzsj)
      api.post('/wx/common/getWXConfig.do', {
        url: location.href,
        appId: this.$route.query.appId
      }).then((response) => {
        this.config = response
      })
        .catch((response) => {
          console.log(response)
        })
      api.post('/wx/zczt/getZCLXPicker.do', {
      }).then((response) => {
        this.zclxList = response
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
  .foot{position:fixed;left:0;height:42px;line-height:42px;width:100%;}
  .foot{bottom:0px;}
  .aaa {
    margin-bottom: 38px;
  }
</style>
