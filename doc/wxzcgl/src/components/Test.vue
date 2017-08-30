<template>
  <div>
    <div>
      <img class="previewer-demo-img" v-for="(item, index) in list" :src="item.src" width="100" @click="show(index)">
    </div>
    <div v-transfer-dom>
      <previewer :list="list" ref="previewer" :options="options"></previewer>
    </div>
    <div>
      <group>
        <x-input title="appId" v-model="appId"></x-input>
        <x-input title="userId" v-model="userId"></x-input>
      </group>
      <x-button type="primary" @click.native="generateToken">生成token</x-button>
      <x-button type="primary" @click.native="toRwxq">查看任务详情</x-button>
      <x-button type="primary" @click.native="toZcxq">查看资产详情</x-button>
      <x-button type="primary" @click.native="toUserxq">查看用户详情</x-button>
      <x-button type="default" @click.native="clearToken">清理token</x-button>
    </div>
  </div>
</template>

<script>
import { Previewer, TransferDom, XButton, XInput, Group, base64 } from 'vux'

export default {
  directives: {
    TransferDom
  },
  components: {
    Previewer,
    XButton,
    XInput,
    Group
  },
  mounted () {
    // this.clearToken()
  },
  watch: {
    '$route': 'clearToken'
  },
  methods: {
    generateToken () {
      let userId = this.userId
      let appId = this.appId
      let _this = this
      if (!userId || !appId) {
        _this.$vux.alert.show({content: '请输入appId和userId'})
        return false
      }
      window.localStorage.removeItem('token')
      let token = {
        'TOKEN_KEY_CREATE_DATE': new Date().getTime(),
        'TOKEN_KEY_APP_ID': appId,
        'TOKEN_KEY_USER_ID': userId
      }
      window.localStorage.token = base64.encode(JSON.stringify(token))
      console.log(window.localStorage.token)
    },
    show (index) {
      console.log(this.$refs)
      this.$refs.previewer.show(index)
    },
    clearToken () {
      window.localStorage.clear()
      console.log(window.localStorage.token)
    },
    toRwxq () {
      // 通过消息查询任务详情
      this.$router.push({name: 'Rwxq', query: {rwid: 2}})
    },
    toZcxq () {
      this.$store.dispatch('zcxqHideBack')
      this.$router.push({name: 'Zcxq', query: {zcid: 1}})
    },
    toUserxq () {
      this.$router.push({name: 'Userxq', query: {appId: 17, userId: 'shangwei'}})
    }
  },
  data () {
    return {
      userId: 'shangwei',
      appId: '17',
      list: [{
        src: 'https://ooo.0o0.ooo/2017/05/17/591c271ab71b1.jpg',
        w: 800,
        h: 400
      },
      {
        src: 'https://ooo.0o0.ooo/2017/05/17/591c271acea7c.jpg'
      }, {
        src: 'https://ooo.0o0.ooo/2017/06/15/59425a592b949.jpeg'
      }],
      options: {
        getThumbBoundsFn (index) {
          // find thumbnail element
          let thumbnail = document.querySelectorAll('.previewer-demo-img')[index]
          // get window scroll Y
          let pageYScroll = window.pageYOffset || document.documentElement.scrollTop
          // optionally get horizontal scroll
          // get position of element relative to viewport
          let rect = thumbnail.getBoundingClientRect()
          // w = width
          return {x: rect.left, y: rect.top + pageYScroll, w: rect.width}
          // Good guide on how to get element coordinates:
          // http://javascript.info/tutorial/coordinates
        }
      }
    }
  }
}
</script>
