<template>
  <div>
    <x-header :left-options="{showBack: showBack}">用户详情</x-header>
    <group :gutter="0">
      <table width="100%" cellspacing="0" cellpadding="0" border="0">
        <tbody>
          <tr>
            <td width="101px;">
              <!-- <x-img :src="userInfo.avatar" class="ximg-demo" error-class="ximg-error"></x-img> -->
              <img :src="userInfo.avatar" class="ximg-demo" alt="">
            </td>
            <td>
              <div class="titCls">
                <div class="nameCls">{{userInfo.name}}</div>
                <div class="wxhCls">微信号：{{userInfo.userid}}</div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </group>
    <group>
      <cell title="性别" :value="userInfo.sex"></cell>
      <cell title="所属部门" :value="deptName"></cell>
      <cell title="联系方式" :value="userInfo.mobile"></cell>
      <cell title="EMAIL" :value="userInfo.email"></cell>
      <cell title="用户角色" :value="roleName"></cell>
      <cell title="当前状态" :value="userInfo.status"></cell>
    </group>
  </div>
</template>
<script>
  import api from '@/api'
  import { XHeader, XImg, Group, Cell } from 'vux'
  export default {
    components: {
      XHeader,
      XImg,
      Group,
      Cell
    },
    mounted () {
      this.loadUserData()
    },
    computed: {
      deptName () {
        let obj = this.userInfo.deptList
        let deptNameStr = ''
        if (obj) {
          for (var i = 0; i < obj.length; i++) {
            if (i === (obj.length - 1)) {
              deptNameStr += (obj[i].deptName)
            } else {
              deptNameStr += (obj[i].deptName + '，')
            }
          }
        }
        return deptNameStr
      },
      roleName () {
        let obj = this.userInfo.tagList
        let roleNameStr = ''
        if (obj) {
          for (var i = 0; i < obj.length; i++) {
            if (i === (obj.length - 1)) {
              roleNameStr += (obj[i].tagName)
            } else {
              roleNameStr += (obj[i].tagName + '，')
            }
          }
        }
        return roleNameStr
      }
    },
    data () {
      return {
        showBack: false,
        userInfo: {}
      }
    },
    methods: {
      loadUserData () {
        let _this = this
        api.post('/wx/wxxq/userXQ.do', {appId: _this.$route.query.appId, userId: _this.$route.query.userId}).then(function (res) {
          if (res) {
            _this.userInfo = res
            // console.log(_this.userInfo)
          }
        })
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
.ximg-demo{
  width: 85px;
  height: 85px;
  margin: 6px;
}
/*.ximg-error {
  background-image: url('../../../assets/no_img.png');
  background-size:85px 85px;
  margin: 6px;
}
.ximg-error:after {
  content: '加载失败';
  color: red;
}*/
.titCls{
  text-align: left;
}
.nameCls{
  margin-bottom: 10px;
  color: #000;
}
.wxhCls{
  color: #b4b4b4;
}
</style>
