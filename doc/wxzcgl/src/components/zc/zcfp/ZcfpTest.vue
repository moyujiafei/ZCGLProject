<template>
  <div>
    <box>
      <flexbox>
        <flexbox-item>
          <x-button type="primary" @click.native="toSqgh">申请归还资产</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toZcfp">资产分配</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toZccxfp">资产重新分配</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toJjzcsj">拒绝上交资产</x-button>
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
      toSqgh () {
        this.$router.push({name: 'Sqgh', query: {zcId: 170}})
      },
      toZcfp () {
        this.$router.push({name: 'Zcfp', query: {zcId: 170}})
      },
      toZccxfp () {
        this.$router.push({name: 'Zccxfp', query: {zcId: 170}})
      },
      toJjzcsj () {
        this.$router.push({name: 'Jjzcsj', query: {zcId: 170}})
      }
    }
  }
</script>
