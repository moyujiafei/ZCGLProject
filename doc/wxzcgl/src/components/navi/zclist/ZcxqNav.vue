﻿<template>
  <div>
    <div :class="mainContainer" id="mainContainerDiv">
      <x-header :left-options="{showBack: zcxqBackFlag}">资产详情</x-header>
      <div id="img_container">
        <card>
          <img slot="header" width="100%" height="100%" class="previewer-demo-img" v-for="(item, index) in imgList" :src="item.src" @click="show(index)">
          <div slot="content" class="card-padding">
            <div style="color:red;font-size:18px;font-weight: bolder;">{{zcInfo.zcztmc}} <span style="color:black;font-size:16px;line-height:1.2;font-weight: normal;">{{zcInfo.zc}}（{{zcInfo.zcdm}}）于{{gzsj}}购买，目前由{{zcInfo.deptName}}的{{zcInfo.syrmc}}保管，存放在{{zcInfo.cfdd}}</span>
            </div>
          </div>
        </card>
      </div>
      <div v-transfer-dom>
        <previewer :list="imgList" ref="previewer" :options="options"></previewer>
      </div>
      <zczt :zt-data="zcInfo.ztList" :wx-config="zcInfo.jssdkConfig"></zczt>
      <div>
        <tabbar>
          <tabbar-item v-for="item in zcInfo.actionList" :key="item.id" :link="item.link" @on-item-click="doAction(item.action)">
            <img slot="icon" :src="item.icon">
            <span slot="label">{{item.label}}</span>
          </tabbar-item>
        </tabbar>
      </div>
    </div>
  </div>
</template>
<script>
  import api from '@/api'
  import { XHeader, Cell, Group, Divider, Previewer, TransferDom, Card, Tabbar, TabbarItem } from 'vux'
  import Zczt from './Zczt'
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
      },
      mainContainer () {
        if (this.zcInfo && this.zcInfo.actionList && this.zcInfo.actionList.length > 0) {
          return 'mainContainer'
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
        let params = {
          zcid: _this.$route.query.zcid,
          pageUrl: document.URL,
          tagName: _this.$route.query.tagName
        }
        api.post('/wx/zcgl/getZC.do', params).then(function (res) {
          if (res) {
            _this.zcInfo = res
            _this.imgList.push({src: res.picUrl})
          }
        })
      },
      show (index) {
        this.$refs.previewer.show(index)
      },
      doAction (action) {
        if (action) {
          let _this = this
          api.post('/wx/zcgl/doAction.do', {zcid: _this.$route.query.zcid, action: action, tagName: _this.$route.query.tagName}).then(function (res) {
            if (res) {
              let gzCount = res.gzCount
              if (gzCount) {
                _this.$vux.alert.show({
                  content: '操作成功',
                  onHide () {
                    _this.$router.go(-1)
                    _this.$store.dispatch('setGzCount', gzCount.toString())
                  }
                })
              } else {
                _this.$vux.alert.show({
                  content: '操作失败'
                })
              }
            }
          })
        }
      }
    },
    beforeRouteEnter (to, from, next) {
      if (from.name === 'NaviZc') {
        delete to.query.tagName// 我的资产没有操作选项
      }
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
  margin-bottom: 52px;
}
.card-padding {
  padding: 15px;
  text-align: left;

}
/*防止纯英文数字溢出*/
#mainContainerDiv{
  word-wrap:break-word; word-break:break-all;
}
</style>
