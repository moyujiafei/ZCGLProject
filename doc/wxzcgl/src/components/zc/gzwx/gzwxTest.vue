<template>
  <div>
    <box>
      <flexbox>
        <flexbox-item>
          <x-button type="primary" @click.native="toWcwx">完成维修</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toSqxzzc">申请闲置资产</x-button>
        </flexbox-item>
        <flexbox-item>
          <x-button type="primary" @click.native="toSqzcwx">申请再次维修</x-button>
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
      toSqbfzc () {
        this.$router.push({name: 'Sqbfzc', query: {zcId: 170}})
      },
      toSqxzzc () {
        this.$router.push({name: 'Sqxzzc', query: {zcId: 170}})
      },
      toSqzcwx () {
        this.$router.push({name: 'Sqzcwx', query: {zcId: 170}})
      },
      toWcwx () {
        this.$router.push({name: 'Wcwx', query: {zcId: 170}})
      }
    }
  }
</script>
