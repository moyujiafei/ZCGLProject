<template>
  <div>
    <div class="mainContainer">
      <x-header :left-options="{showBack: zcxqBackFlag}">资产详情</x-header>
      <div id="img_container">
        <card>
          <img slot="header" width="100%" height="100%" class="previewer-demo-img" v-for="(item, index) in imgList" :src="item.src" @click="show(index)">
          <div slot="content" class="card-padding">
            <p style="color:red;font-size:18px;font-weight: bolder;display: inline;">{{zcInfo.zcztmc}}</p>
            <p style="font-size:16px;line-height:1.2;display: inline;">{{zcInfo.zc}}（{{zcInfo.zcdm}}）于{{gzsj}}购买，目前由{{zcInfo.deptName}}的{{zcInfo.syrmc}}保管，存放在{{zcInfo.cfdd}}</p>
          </div>
        </card>
      </div>
      <div v-transfer-dom>
        <previewer :list="imgList" ref="previewer" :options="options"></previewer>
      </div>
      <zczt :zt-data="zcInfo.ztList" :wx-config="zcInfo.jssdkConfig"></zczt>
    </div>
  </div>
</template>
<script>
  import api from '@/api'
  import { XHeader, Cell, Group, Divider, Previewer, TransferDom, Card, Tabbar, TabbarItem } from 'vux'
  import Zczt from '@/components/navi/zclist/Zczt'
  import { mapGetters } from 'vuex'
  export default {
    directives: {
      TransferDom
    },
    components: {
      XHeader,
      Cell,
      Group,
      Divider,
      Zczt,
      Previewer,
      Card,
      Tabbar,
      TabbarItem
    },
    mounted () {
      this.loadZcdata()
    },
    computed: {
      ...mapGetters([
        'zcxqBackFlag'
      ]),
      gzsj () {
        if (this.zcInfo.gzsj) {
          return api.getFmtDate(this.zcInfo.gzsj, 'YYYY年MM月DD日')
        } else {
          return ''
        }
      },
      zjnx () {
        if (this.zcInfo.zjnx) {
          return this.zcInfo.zjnx + '年'
        } else {
          return ''
        }
      }
    },
    data () {
      return {
        zcInfo: {},
        imgList: [],
        options: {
          getThumbBoundsFn (index) {
            let thumbnail = document.querySelectorAll('.previewer-demo-img')[index]
            let pageYScroll = window.pageYOffset || document.documentElement.scrollTop
            let rect = thumbnail.getBoundingClientRect()
            return {x: rect.left, y: rect.top + pageYScroll, w: rect.width}
          }
        }
      }
    },
    methods: {
      loadZcdata () {
        let _this = this
        let pageUrl = document.URL
        api.post('/wx/wxxq/zcXQ.do', {zcid: _this.$route.query.zcid, pageUrl: pageUrl, tagName: _this.$route.query.tagName}).then(function (res) {
          if (res) {
            _this.zcInfo = res
            _this.imgList.push({src: res.picUrl})
          }
        })
      },
      show (index) {
        this.$refs.previewer.show(index)
      },
      doAction (action, item) {
        console.log(action)
        console.log(item)
      }
    },
    beforeRouteEnter (to, from, next) {
      next(vm => {
        vm.$store.dispatch('hideMainTabbar')
      })
    }
  }
</script>
<style scoped>
#img_container{
  text-align: center;
  margin-top: 3px;
}
.zcImg{
  width: 280px;
  height: 220px;
}
.mainContainer{
  /*margin-bottom: 52px;*/
}
.card-padding {
  padding: 15px;
  text-align: left;
}
</style>
