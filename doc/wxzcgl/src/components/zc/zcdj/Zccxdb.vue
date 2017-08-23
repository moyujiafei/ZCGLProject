<template>
  <div class="main">
    <div>
      <x-header :left-options="{showBack: true}">资产重新调拨</x-header>
    </div>

    <group>
      <cell :title="title1" v-model="oldGlbmValue" ></cell>
      <cell :title="title2" v-model="oldCfddValue" ></cell>
    </group>

    <group>
      <popup-picker :title="title3" :data="glbmList" :columns="3" v-model="glbmValue" show-name placeholder="请选择管理部门"></popup-picker>
      <popup-picker :title="title4" :data="cfddLidt" :columns="3" v-model="cfddValue" placeholder="请选择存放地点"  @on-change="onChange"></popup-picker>
    </group>

    <flexbox class="foot">
      <flexbox-item>
        <x-button type="primary" @click.native="allocateZC">确认</x-button>
      </flexbox-item>
      <flexbox-item>
        <x-button type="warn" @click.native="cancel">取消</x-button>
      </flexbox-item>
    </flexbox>

  </div>
</template>

<script>
  import api from '@/api/index'
  import { PopupPicker, Group, Cell, Picker, XButton, XHeader, Flexbox, FlexboxItem } from 'vux'
  export default {
    components: {
      PopupPicker,
      Group,
      Picker,
      XButton,
      Flexbox,
      FlexboxItem,
      Cell,
      XHeader
    },
    methods: {
      cancel () {
        this.$router.go(-1)
      },
      onChange () {
        let _this = this
        var n = parseInt(_this.cfddValue[2])
        if (_this.cfddValue[2].indexOf('null') >= 0) {
          _this.cfddCopy = _this.cfddValue
          var temp = ['', '', '']
          _this.cfddValue = temp
        } else if (isNaN(n)) {
          return
        } else {
          _this.cfddCopy = _this.cfddValue
          api.post('/wx/zczt/getCFDDByFjid.do', {
            fjId: _this.cfddCopy[2]
          }).then((response) => {
            if (response != null) {
              var temp = ['', '', response]
              _this.cfddValue = temp
            } else {
              this.$vux.toast.show({
                type: 'warn',
                text: '失败'
              })
            }
          })
            .catch((response) => {
              this.$vux.toast.show({
                type: 'warn',
                text: '失败'
              })
            })
        }
      },
      allocateZC () {
        let _this = this
        if (_this.glbmValue.length === 0) {
          _this.$vux.toast.show({
            type: 'warn',
            text: '请选择资产管理部门'
          })
          return
        }
        if (_this.cfddValue.length === 0) {
          _this.cfddCopy = ['null1', 'null2', 'null3']
        }
        var temp = _this.glbmValue
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
        api.post('/wx/zczt/reallocateZC.do', {
          zcId: this.$route.query.zcId,
          deptNo: postData,
          cfdd: this.cfddCopy[2]
        }).then((response) => {
          if (response === 'success') {
            this.$vux.toast.show({
              text: '成功'
            })
            this.$router.go(-1)
          } else {
            this.$vux.toast.show({
              type: 'warn',
              text: '失败'
            })
          }
        })
          .catch((response) => {
            this.$vux.toast.show({
              type: 'warn',
              text: '失败'
            })
          })
      }
    },
    data () {
      return {
        glbmList: [],
        glbmValue: [],
        cfddLidt: [],
        cfddValue: [],
        cfddCopy: [],
        oldGlbmValue: [],
        oldCfddValue: [],
        title1: '原有资产管理部门',
        title2: '原有资产存放地点',
        title3: '新的资产管理部门',
        title4: '新的资产存放地点'
      }
    },
    mounted: function () {
      api.get('/wx/wxchudept/getWXDeptPick.do').then((response) => {
        this.glbmList = response.res
      })
        .catch((response) => {
          console.log(response)
        })
      api.post('/wx/zczt/getGLBMByZcid.do', { zcId: this.$route.query.zcId }).then((response) => {
        this.oldGlbmValue = response
      })
        .catch((response) => {
          console.log(response)
        })
      api.post('/wx/zczt/getCFDDByZcid.do', { zcId: this.$route.query.zcId }).then((response) => {
        this.oldCfddValue = response
      })
        .catch((response) => {
          console.log(response)
        })
      api.get('/wx/zczt/getFJPicker.do').then((response) => {
        this.cfddLidt = response
      })
        .catch((response) => {
          console.log(response)
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
  /*.main{position:fixed;top:0px;bottom:42px;width:100%;}*/
  .foot{position:fixed;left:0;height:42px;line-height:42px;width:100%;}
  .foot{bottom:0;}

</style>
