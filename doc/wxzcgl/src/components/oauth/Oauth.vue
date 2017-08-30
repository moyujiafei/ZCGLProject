<template>
  <div></div>
</template>
<script>
  // import { base64 } from 'vux'
  export default {
    data () {
      return {
        token: ''
      }
    },
    mounted () {
      this.getToken()
    },
    methods: {
      getToken () {
        window.localStorage.removeItem('token')
        let _this = this
        let result = _this.$route.query
        if (result.token) {
          window.localStorage.token = result.token
          setTimeout(() => { _this.goWelcome(result.token) }, 500)
        } else {
          _this.$vux.alert.show({content: '网页授权失败'})
        }
      },
      goWelcome (token) {
        let _this = this
        if (window.localStorage.token) {
          _this.toWelcome()
        } else {
          setTimeout(() => {
            _this.toWelcome()
          }, 500)
        }
      },
      toWelcome () {
        let random = Math.random()
        this.$router.replace({name: 'Welcome', query: {appId: this.$route.query.appId, randomMsg: random}})
      }
    }
  }
</script>
<style>
  
</style>
