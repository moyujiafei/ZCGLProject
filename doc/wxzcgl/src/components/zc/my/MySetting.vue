<template>
  <div>
    <div>
      <x-header :left-options="{showBack: true}">我的设置</x-header>
    </div>
    <group>
      <x-switch title="仅在WIFI环境下加载图片和音频" v-model="allowImg" @on-change="onClick"></x-switch>
    </group>
  </div>
</template>

<script>
  import { XSwitch, XHeader, Group, XButton } from 'vux'
  export default {
    components: {
      XSwitch,
      Group,
      XButton,
      XHeader
    },
    methods: {
      onClick () {
        if (window.localStorage.allowImg) {
          window.localStorage.removeItem('allowImg')
        } else {
          window.localStorage.allowImg = true
        }
      }
    },
    data () {
      return {
        allowImg: window.localStorage.allowImg
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
