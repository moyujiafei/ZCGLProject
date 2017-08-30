<template>
  <div>
    <box>
      <flexbox>
        <flexbox-item>
          <x-button type="primary" @click.native="toZcdb">资产调拨调拨</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toZccxdb">资产重新调拨</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toZcjjgh">拒绝归还资产</x-button>
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
      toZccxdb () {
        this.$router.push({name: 'Zccxdb', query: {zcId: 170}})
      },
      toZcjjgh () {
        this.$router.push({name: 'Zcjjgh', query: {zcId: 170}})
      },
      toZcdb () {
        this.$router.push({name: 'Zcdb', query: {zcId: 170}})
      }
    }
  }
</script>
