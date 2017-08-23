<template>
  <div>
    <div>
      <x-header :left-options="{showBack: true}">联系开发者</x-header>
    </div>
    <group>
       <cell title="联系人" value="许庆炜"></cell>
      <cell title="联系方式" value="13419567499"></cell>
       <cell title="开发单位" value="湖北潮涌信息科技有限公司"></cell>
       <cell title="单位地址"></cell>
       <x-textarea value="武汉市东湖新技术开发区软件园东路1号E1栋204号" :rows="2" :readonly="true"></x-textarea>
    </group>
  </div>
</template>

<script>
  import { XHeader, Group, Cell, XTextarea } from 'vux'
  export default {
    components: {
      Group,
      XHeader,
      Cell,
      XTextarea
    },
    methods: {
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
