<template>
  <div class="main">
    <div>
      <x-header :left-options="{showBack: true}">拒绝资产闲置申请</x-header>
    </div>

    <group title="拒绝原因：">
      <XTextarea :max="100" v-model="remark"></XTextarea>
    </group>

    <flexbox class="foot">
      <flexbox-item>
        <x-button type="primary" @click.native="refuseXZSQ">确认</x-button>
      </flexbox-item>
      <flexbox-item>
        <x-button type="warn" @click.native="cancel">取消</x-button>
      </flexbox-item>
    </flexbox>

  </div>
</template>

<script>
  import api from '@/api/index'
  import { PopupPicker, Group, Cell, Picker, XButton, ToastPlugin, XHeader, Flexbox, FlexboxItem, XTextarea } from 'vux'
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
      XHeader,
      XTextarea
    },
    methods: {
      cancel () {
        this.$router.go(-1)
      },
      refuseXZSQ () {
        if (this.remark.length === 0 || this.remark === null) {
          this.$vux.toast.show({
            type: 'warn',
            text: '请输入拒绝原因'
          })
          return
        }
        api.post('/wx/zczt/refuseXZSQ.do', {
          refuseRemark: this.remark,
          zcId: this.$route.query.zcId
        }).then((response) => {
          this.$router.go(-1)
          if (response === 'success') {
            this.$vux.toast.show({
              text: '已拒绝资产闲置申请'
            })
          } else {
            this.$vux.toast.show({
              type: 'warn',
              text: '拒绝资产闲置申请失败'
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
    data () {
      return {
        remark: ''
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
