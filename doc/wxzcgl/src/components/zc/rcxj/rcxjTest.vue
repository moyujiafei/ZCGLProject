<template>
  <div>
    <box>
      <flexbox>
        <flexbox-item>
          <x-button type="primary" @click.native="toWcrcxj">完成日常巡检</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toSqwxzc">申请维修资产</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toSqxzzc">申请闲置资产</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toSqbfzc">申请报废资产</x-button>
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
      toWcrcxj () {
        this.$router.push({name: 'XjWcrcxj', query: {zcId: 170}})
      },
      toSqwxzc () {
        this.$router.push({name: 'XjSqwxzc', query: {zcId: 170}})
      },
      toSqxzzc () {
        this.$router.push({name: 'XjSqxzzc', query: {zcId: 170}})
      },
      toSqbfzc () {
        this.$router.push({name: 'XjSqbfzc', query: {zcId: 170}})
      }
    }
  }
</script>
