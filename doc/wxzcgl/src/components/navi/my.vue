<template>
  <div>
    <x-header :left-options="{showBack: showBack}">我的</x-header>
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
