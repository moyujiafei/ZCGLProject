<template>
  <div class="main">
    <div>
      <x-header :left-options="{showBack: true}">申请归还资产</x-header>
    </div>
    <group title="申请原因">
      <x-textarea :max="100" v-model="applyReason"></x-textarea>
    </group>

    <flexbox class="foot">
      <flexbox-item>
        <x-button type="primary" @click.native="revertZC">确认</x-button>
      </flexbox-item>
      <flexbox-item>
       <x-button type="warn" @click.native="cancel">取消</x-button>
      </flexbox-item>
    </flexbox>
  </div>
</template>

<script>
  import api from '@/api/index'
  import { Group, XButton, XHeader, Flexbox, FlexboxItem, XTextarea } from 'vux'
  export default {
    components: {
      Group,
      XHeader,
      XTextarea,
      Flexbox,
      FlexboxItem,
      XButton
    },
    methods: {
      cancel () {
        this.$router.go(-1)
      },
      revertZC () {
        api.post('/wx/zczt/revertZC.do', {
          zcId: this.$route.query.zcId,
          applyReason: this.applyReason
        }).then((response) => {
          if (response === 'success') {
            this.$vux.toast.show({
              text: '成功'
            })
            this.$router.go(-1)
          } else {
            this.$vux.toast.show({
              type: 'warn',
              text: '失败'
            })
          }
        })
      }
    },
    data () {
      return {
        applyReason: ''
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
  /*.main{position:fixed;top:0px;bottom:42px;width:100%;}*/
  .foot{position:fixed;left:0;height:42px;line-height:42px;width:100%;}
  .foot{bottom:0px;}

</style>
