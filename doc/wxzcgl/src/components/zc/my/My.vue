<template>
  <group>
    <cell title="我的消息" :link="{name:'MyMsg'}" is-link></cell>
    <cell title="我的操作" :link="{name:'MyAction'}" is-link></cell>
    <cell title="我的设置" :link="{name:'MySetting'}" is-link></cell>
    <cell title="我的通讯录" :link="{name:'MyConcat'}" is-link></cell>
    <cell title="联系开发者" :link="{name:'MyAbout'}" is-link></cell>
    <cell title="软件功能更新说明" :link="{name:'MyUpdate'}" is-link></cell>
    <!-- <cell title="test" :link="{name:'gzwxTest'}" is-link></cell>
    <cell title="payTest" :link="{name:'payTest'}" is-link></cell> -->
    <!--<cell title="tttt" :link="{name:'JssdkTest'}" is-link></cell>-->
    <!--<cell title="zc" :link="{name:'ZcfpTest'}" is-link></cell>-->
  </group>
</template>


<script>
  import { Cell, CellBox, CellFormPreview, Group, Badge } from 'vux'
  export default {
    components: {
      Group,
      Cell,
      CellFormPreview,
      CellBox,
      Badge
    },
    methods: {},
    data () {
      return {}
    }
  }
</script>
