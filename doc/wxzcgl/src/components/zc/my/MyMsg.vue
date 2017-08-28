<template>
  <div>
    <div>
      <x-header :left-options="{showBack: true}">我的消息</x-header>
    </div>
    <group>
      <popup-picker title="消息类型" :data="lxList" v-model="lxValue" show-name @on-change="onChange"></popup-picker>
    </group>

    <div v-for="item in msgList">
      <group>
        <table>
          <tbody>
          <tr>
            <td class="textTop dateCls">
              <h3 class="inlineStyle">{{item.fssj | normalTime('DD')}}</h3><h5 class="inlineStyle ml">{{item.fssj | normalTime('MM月')}}</h5>
            </td>
            <td>
              {{item.nr}}
            </td>
          </tr>
          </tbody>
        </table>
      </group>
    </div>
  </div>
</template>

<script>
  import { XSwitch, XHeader, Group, XButton, PopupPicker } from 'vux'
  import api from '@/api/index'
  export default {
    components: {
      XSwitch,
      Group,
      XHeader,
      XButton,
      PopupPicker
    },
    methods: {
      onChange () {
        let _this = this
        api.post('/wx/mymsg/getMsgList.do', {
          msgLx: _this.lxValue[0]
        }).then((response) => {
          _this.msgList = response
        })
      }
    },
    data () {
      return {
        msgList: [],
        lxList: [],
        lxValue: []
      }
    },
    mounted: function () {
      let _this = this
      api.post('/wx/mymsg/getMsgList.do', {}).then((response) => {
        _this.msgList = response
      })
      api.post('/wx/mymsg/getLxPickListWithAll.do', {}).then((response) => {
        _this.lxList = response
        _this.lxValue = [response[0][0].value]
      })
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
<style scoped>
  .textTop {
    vertical-align: top;
  }

  .textTop p {
    font-size: 10px;
  }

  .ttt {
    font-size: 10px;
  }

  .dateCls {
    width: 68px;
  }

  .inlineStyle {
    display: inline;
  }

  .ml {
    margin-left: 2px;
  }
</style>
