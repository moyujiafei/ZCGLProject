<template>
  <div>
    <box>
      <flexbox>
        <flexbox-item>
          <x-button type="primary" @click.native="toJjlyzc">拒绝领用资产</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toSjzc">上交资产</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toSqwxzc">申请维修资产</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toSqzcbf">申请报废资产</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toSqzcxz">申请闲置资产</x-button>
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
      toJjlyzc () {
        this.$router.push({name: 'Jjlyzc', query: {zcId: 170}})
      },
      toSjzc () {
        this.$router.push({name: 'Sjzc', query: {zcId: 170}})
      },
      toSqwxzc () {
        this.$router.push({name: 'Sqwxzc', query: {zcId: 170}})
      },
      toSqzcbf () {
        this.$router.push({name: 'Sqzcbf', query: {zcId: 170}})
      },
      toSqzcxz () {
        this.$router.push({name: 'Sqzcxz', query: {zcId: 170}})
      }
    }
  }
</script>
