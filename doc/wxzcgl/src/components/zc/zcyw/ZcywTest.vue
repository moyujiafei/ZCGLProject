<template>
  <div>
    <box>
      <flexbox>
        <flexbox-item>
          <x-button type="primary" @click.native="toRefuseWXSQ">拒绝资产维修申请</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toAgreeBFSQ">同意资产报废申请</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toRefuseBFSQ">拒绝资产报废申请</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toAgreeXZSQ">同意资产闲置申请</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toRefuseXZSQ">拒绝资产闲置申请</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toAgreeBFZC">同意报废资产</x-button>
        </flexbox-item>
      </flexbox>
    </box>
  </div>
</template>
<script>
  import { XButton, Box, GroupTitle, Group, Flexbox, FlexboxItem, Divider } from 'vux'

  export default {
    components: {
      XButton,
      Box,
      GroupTitle,
      Group,
      Flexbox,
      FlexboxItem,
      Divider
    },
    methods: {
      toRefuseWXSQ () {
        this.$router.push({name: 'refuseWXSQ', query: {zcId: 170}})
      },
      toAgreeBFSQ () {
        this.$router.push({name: 'agreeBFSQ', query: {zcId: 170}})
      },
      toRefuseBFSQ () {
        this.$router.push({name: 'refuseBFSQ', query: {zcId: 170}})
      },
      toAgreeXZSQ () {
        this.$router.push({name: 'agreeXZSQ', query: {zcId: 170}})
      },
      toRefuseXZSQ () {
        this.$router.push({name: 'refuseXZSQ', query: {zcId: 170}})
      },
      toAgreeBFZC () {
        this.$router.push({name: 'agreeBFZC', query: {zcId: 170}})
      }
    }
  }
</script>
