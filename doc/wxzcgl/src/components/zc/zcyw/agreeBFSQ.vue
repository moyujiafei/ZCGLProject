<template>
  <div class="main">
    <div>
      <x-header :left-options="{showBack: true}">同意资产报废申请</x-header>
    </div>
    <group>
       <popup-picker :title="title" :data="cfddList" v-model="cfdd" :columns="3" ref="picker3" show-name></popup-picker>
    </group>
    <flexbox class="foot">
      <flexbox-item>
        <x-button type="primary" @click.native="agreeBFSQ">确认</x-button>
      </flexbox-item>
      <flexbox-item>
        <x-button type="warn" @click.native="cancel">取消</x-button>
      </flexbox-item>
    </flexbox>
  </div>
</template>

<script>
  import api from '@/api/index'
  import { PopupPicker, Group, Cell, Picker, XButton, ToastPlugin, XHeader, Flexbox, FlexboxItem } from 'vux'
  import Vue from 'vue'
  Vue.use(ToastPlugin)
  export default {
    components: {
      PopupPicker,
      Group,
      Picker,
      ToastPlugin,
      XButton,
      Flexbox,
      FlexboxItem,
      Cell,
      XHeader
    },
    methods: {
      cancel () {
        this.$router.go(-1)
      },
      agreeBFSQ () {
        let _this = this
        if (_this.cfdd === null || _this.cfdd.length === 0) {
          _this.$vux.toast.show({
            type: 'warn',
            text: '请选择一个存放地点'
          })
          return
        }
        var temp = _this.cfdd
        var postData = ''
        if (temp[0].indexOf('null') !== -1) {
          postData = ''
        } else if (temp[1].indexOf('null') !== -1) {
          postData = temp[0]
        } else if (temp[2].indexOf('null') !== -1) {
          postData = temp[1]
        } else {
          postData = temp[2]
        }
        api.post('/wx/zczt/agreeBFSQ.do', {
          zcId: _this.$route.query.zcId,
          cfdd: postData
        }).then((response) => {
          _this.$router.go(-1)
          if (response === 'success') {
            _this.$vux.toast.show({
              text: '同意资产报废申请成功'
            })
          } else {
            _this.$vux.toast.show({
              type: 'warn',
              text: '请求失败'
            })
          }
        })
        .catch((response) => {
          _this.$vux.toast.show({
            type: 'warn',
            text: '失败'
          })
        })
      }
    },
    mounted: function () {
      api.get('/wx/zczt/getFJPicker.do').then((response) => {
        this.cfddList = response
      })
        .catch((response) => {
          console.log(response)
        })
    },
    data () {
      return {
        title: '新的存放地点 :',
        cfddList: [],
        cfdd: []
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
    }
  }
</script>

<style scoped>
  .picker-buttons {
    margin: 0 15px;
  }
  .main{position:fixed;top:0px;bottom:42px;width:100%;}
  .foot{position:fixed;left:0;height:42px;line-height:42px;width:100%;}
  .foot{bottom:0px;}

</style>
