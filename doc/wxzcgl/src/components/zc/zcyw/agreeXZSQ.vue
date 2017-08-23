<template>
  <div class="main">
    <div>
      <x-header :left-options="{showBack: true}">同意资产闲置申请</x-header>
    </div>
    <group>
       <popup-picker :title="title" :data="cfddList" v-model="cfdd" :columns="3" ref="picker3" show-name></popup-picker>
    </group>
    <flexbox class="foot">
      <flexbox-item>
        <x-button type="primary" @click.native="agreeXZSQ">确认</x-button>
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
      agreeXZSQ () {
        if (this.cfdd === null || this.cfdd.length === 0) {
          this.$vux.toast.show({
            type: 'warn',
            text: '请选择一个存放地点'
          })
          return
        }
        api.post('/wx/zczt/agreeXZSQ.do', {
          zcId: this.$route.query.zcId,
          cfdd: this.cfdd[2]
        }).then((response) => {
          this.$router.go(-1)
          if (response === 'success') {
            this.$vux.toast.show({
              text: '同意资产闲置申请成功'
            })
          } else {
            this.$vux.toast.show({
              type: 'warn',
              text: '请求失败'
            })
          }
        })
        .catch((response) => {
          this.$vux.toast.show({
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
  .main{position:fixed;top:10px;bottom:42px;width:100%;}
  .foot{position:fixed;left:0;height:42px;line-height:42px;width:100%;}
  .foot{bottom:10px;}

</style>
