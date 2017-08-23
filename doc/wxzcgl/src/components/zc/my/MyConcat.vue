<template>
  <div>
    <div>
      <x-header :left-options="{showBack: true}">我的通讯录</x-header>
    </div>
    <group>
      <popup-picker title="所属部门" :data="bmList" v-model="bmValue" :columns="bmCol"  show-name @on-change="onChange"></popup-picker>
    </group>

    <div v-for="item in contactsList">
      <table>
        <tbody>
        <tr>
          <td>
            <div v-if="item.avatar === ''">
              <img src="../../../assets/deafult.png" alt="" height="80" width="80">
            </div>
            <div v-else>
              <img :src="item.avatar" alt="" height="80" width="80">
            </div>
          </td>
          <td width="100%">
            <p>
            <h3>{{item.name}}</h3>
            </p>
            <p class="text">联系方式:{{item.mobile}} </p>
            <p class="text">{{item.deptList[0].deptName}}(<a v-for="(tag, index) in item.tagList">{{ tag.tagName }}<b
              v-if="index < item.tagList.length - 1">,</b></a>)</p>
          </td>
          <td style="text-align: left">
            <a :href="tel(item.mobile)"><img src="../../../assets/telephone.png" height="50" width="50"></a>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div>
      <x-button type="primary" @click.native="jiazai">加载更多</x-button>
    </div>
  </div>
</template>

<script>
  import { XHeader, Group, XButton, PopupPicker } from 'vux'
  import api from '@/api/index'
  export default {
    components: {
      Group,
      XHeader,
      XButton,
      PopupPicker
    },
    methods: {
      tel (v) {
        return 'tel:' + v
      },
      onChange () {
        let _this = this
        _this.deptId = _this.bmValue[0]
        var temp = _this.bmValue
        _this.page = 1
        var postData = ''
        if (temp[0].indexOf('null') !== -1) {
          postData = ''
        } else if (temp[1].indexOf('null') !== -1) {
          postData = temp[0]
        } else if (temp[2].indexOf('null') !== -1) {
          postData = temp[1]
        } else {
          postData = temp[2]
        }
        _this.deptId = postData
        api.post('/wx/wxchuuser/getChuWXUserListByDept.do', {
          deptId: postData,
          rows: _this.rows,
          page: _this.page
        }).then((response) => {
          _this.page++
          _this.contactsList = response
        })
      },
      jiazai () {
        let _this = this
        api.post('/wx/wxchuuser/getChuWXUserListByDept.do', {
          deptId: _this.deptId,
          rows: _this.rows,
          page: _this.page
        }).then((response) => {
          _this.page++
          _this.contactsList = _this.contactsList.concat(response)
        })
      }
    },
    data () {
      return {
        contactsList: [],
        page: 1,
        rows: 10,
        bmCol: 3,
        deptId: '',
        bmList: [],
        bmValue: []
      }
    },
    mounted: function () {
      let _this = this
      api.post('/wx/wxchuuser/getChuWXUserListByDept.do', {
        deptId: _this.deptId,
        rows: _this.rows,
        page: _this.page
      }).then((response) => {
        _this.page++
        _this.contactsList = response
      })
      api.post('/wx/wxchudept/getWXDeptPickWithAll.do', {}).then((response) => {
        _this.bmList = response.res
        // _this.bmValue = [response.res[0][0].value]
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
  .textTop p {
    font-size: 10px;
  }

  .text {
    font-size: 10px;
  }
</style>
