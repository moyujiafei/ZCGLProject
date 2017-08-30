<template>
  <div id="app" style="height:100%;">
    <view-box ref="viewBox" body-padding-top="0" body-padding-bottom="0">
      <div>
        <transition :name="'vux-pop-' + (getDirection === 'forward' ? 'out' : 'in')">
          <keep-alive>
            <router-view class="router-view" v-if="$route.meta.keepAlive"></router-view>
          </keep-alive>
        </transition>
        <transition :name="'vux-pop-' + (getDirection === 'forward' ? 'out' : 'in')">
          <router-view class="router-view" v-if="!$route.meta.keepAlive"></router-view>
        </transition>
      </div>
      <tabbar v-show="showMainTabbar">
        <tabbar-item :selected="true" @on-item-click="toZc">
          <img slot="icon-active" src="./assets/zc_active.png">
          <img slot="icon" src="./assets/zc.png">
          <span slot="label">资产</span>
        </tabbar-item>
        <tabbar-item :badge="gzCountNo" @on-item-click="toGz">
          <img slot="icon-active" src="./assets/work_active.png">
          <img slot="icon" src="./assets/work.png">
          <span slot="label">工作</span>
        </tabbar-item>
        <tabbar-item @on-item-click="toMy">
          <img slot="icon-active" src="./assets/my_active.png">
          <img slot="icon" src="./assets/my.png">
          <span slot="label">我的</span>
        </tabbar-item>
      </tabbar>
    </view-box>
  </div>
</template>
<script>
import { Tabbar, TabbarItem, ViewBox } from 'vux'
import { mapGetters } from 'vuex'
export default {
  components: {
    Tabbar,
    TabbarItem,
    ViewBox
  },
  computed: {
    ...mapGetters([
      'showMainTabbar',
      'gzCountNo',
      'getDirection'
    ]),
    tagName () {
      return this.$route.query.tagName
    }
  },
  mounted () {
  },
  methods: {
    toZc () {
      this.$router.replace({name: 'NaviZc', query: {tagName: this.tagName}})
    },
    toGz () {
      this.$router.replace({name: 'NaviGz', query: {tagName: this.tagName}})
    },
    toMy () {
      this.$router.replace({name: 'NaviMy', query: {tagName: this.tagName}})
    },
    clearCount () {
    }
  }
}
</script>

<style lang="less">
@import '~vux/src/styles/reset.less';
@import '~vux/src/styles/1px.less';

body {
  background-color: #fbf9fe;
}
</style>

<style>
 html, body {
  height: 100%;
  width: 100%;
  overflow-x: hidden;
}
.router-view {
  width: 100%;
}
.weui-tab__panel{overflow-x: hidden !important;}
.vux-pop-out-enter-active,
.vux-pop-out-leave-active,
.vux-pop-in-enter-active,
.vux-pop-in-leave-active {
  will-change: transform;
  transition: all 500ms;
  height: 100%;
  position: absolute;
  backface-visibility: hidden;
  perspective: 1000;
}
.vux-pop-out-enter {
  opacity: 0;
  transform: translate3d(-100%, 0, 0);
}
.vux-pop-out-leave-active {
  opacity: 0;
  transform: translate3d(100%, 0, 0);
}
.vux-pop-in-enter {
  opacity: 0;
  transform: translate3d(100%, 0, 0);
}
.vux-pop-in-leave-active {
  opacity: 0;
  transform: translate3d(-100%, 0, 0);
}
</style>
