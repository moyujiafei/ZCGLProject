<template>
  <div>
    <div>
      <x-header :left-options="{showBack: true}">软件功能更新说明</x-header>
    </div>
    <group>
       <cell
        title="2017年8月18日"
        is-link
        :border-intent="false"
        :arrow-direction="showContent002?'up':'down'"
        @click.native="showContent002 =!showContent002"></cell>

        <template v-if="showContent002">
          <cell-box :border-intent="false" class="sub-item">1.管理控制台增加数据备份功能</cell-box>
          <cell-box :border-intent="false" class="sub-item">2.管理控制台增加二维码打印模块</cell-box>
          <cell-box :border-intent="false" class="sub-item">3.微信端增加资产登记模块</cell-box>
          <cell-box :border-intent="false" class="sub-item">4.微信端扫码支持条形码</cell-box>
          <cell-box :border-intent="false" class="sub-item">5.修正一组Bug</cell-box>
        </template>
    </group>
    <group>
       <cell
        title="2017年7月30日"
        is-link
        :border-intent="false"
        :arrow-direction="showContent001?'up':'down'"
        @click.native="showContent001 =!showContent001"></cell>

        <template v-if="showContent001">
          <cell-box :border-intent="false" class="sub-item">1.《资产管理微助手系统》测试版上线。</cell-box>
        </template>
    </group>
  </div>
</template>

<script>
  import { XSwitch, XHeader, Group, XButton, Cell, CellBox } from 'vux'
  export default {
    components: {
      XSwitch,
      Group,
      XButton,
      XHeader,
      Cell,
      CellBox
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
        showContent001: true,
        showContent002: true,
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

<style scoped>
  .sub-item {
    color: #888;
    font-size: 14px;
  }
</style>>
