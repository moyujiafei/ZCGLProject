<template>
  <div>
    <div>
      <x-header :left-options="{showBack: true}">Test入口</x-header>
    </div>

    <box gap="10px 10px">
      <x-button type="primary" @click.native="toZcdj">资产登记Test</x-button>
      <x-button type="primary" @click.native="toWxxj">维修巡检Test</x-button>
    </box>
  </div>
</template>


<script>
  import { XButton, Box, GroupTitle, Group, XHeader, Divider } from 'vux'

  export default {
    components: {
      XButton,
      Box,
      GroupTitle,
      Group,
      XHeader,
      Divider
    },
    methods: {
      toZcdj () {
        this.$router.push({name: 'zcdjTest', query: {zcId: 170}})
      },
      toWxxj () {
        this.$router.push({name: 'wxxjTest', query: {zcId: 170}})
      }
    }
  }
</script>
