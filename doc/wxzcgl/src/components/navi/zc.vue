<template>
  <div>
    <x-header :left-options="{showBack: false}">我的资产</x-header>
    <div class="zclistPanel">
      <MyZclist :tag-name="tagName"></MyZclist>
    </div>
  </div>
</template>
<script>
import { XHeader } from 'vux'
import MyZclist from './zclist/MyZclist.vue'
export default {
  components: {
    XHeader,
    MyZclist
  },
  computed: {
    tagName () {
      return this.$route.query.tagName
    }
  },
  mounted () {
  },
  methods: {
  }
}
</script>
<style scoped>
.zclistPanel{
  margin-bottom: 53px;
}
</style>
