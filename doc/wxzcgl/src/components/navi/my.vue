<template>
  <div>
<<<<<<< HEAD
    <x-header :left-options="{showBack: showBack}">我的信息</x-header>
=======
    <x-header :left-options="{showBack: showBack}">我的</x-header>
>>>>>>> upstream/master
    <my></my>
  </div>
</template>
<script>
import { XHeader } from 'vux'
import my from '@/components/zc/my/My'

export default {
  components: {
    XHeader,
    my
  },
  computed: {
    tagName () {
      return this.$route.query.tagName
    }
  },
  data () {
    return {
      showBack: false
    }
  },
  mounted () {
  },
  methods: {
  }
}
</script>
<style scoped>

</style>
