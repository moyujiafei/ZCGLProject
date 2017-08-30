<template>
  <div>
    <div>
      <x-header :left-options="{showBack: showBack}">任务详情</x-header>
      <div>
        <form-preview :header-label="headTitle" :header-value="czr" :body-items="rwInfoList" name="demo"></form-preview>
      </div>
      <div>
        <group title="资产列表">
          <div>
            <tab>
              <tab-item selected @on-item-click="onItemClick">未完成</tab-item>
              <tab-item @on-item-click="onItemClick">已完成</tab-item>
            </tab>
          </div>
          <panel v-show="tabIndex === 0" :footer="footer" :list="rwzcData0" :type="type" @on-click-footer="getMore" @on-click-item="toZcdetail"></panel>
          <panel v-show="tabIndex === 1" :footer="footer" :list="rwzcData1" :type="type" @on-click-footer="getMore" @on-click-item="toZcdetail"></panel>
        </group>
      </div>
    </div>
  </div>
</template>

<script>
  import api from '@/api'
  import { FormPreview, Cell, Group, Panel, XHeader, Tab, TabItem } from 'vux'
  import common from '@/components/common'
  export default {
    components: {
      FormPreview,
      Cell,
      Group,
      Panel,
      XHeader,
      Tab,
      TabItem
    },
    mounted () {
      this.loadRwData()
    },
    computed: {
      czr () {
        let czr = '操作人：'
        if (this.rwInfo.czrmc) {
          return czr + this.rwInfo.czrmc
        } else {
          return czr
        }
      },
      rwInfoList () {
        let rw = this.rwInfo
        let rwjd = rw.finish === 1 ? '已完成' : '未完成'
        let result = [{
          label: '任务类型',
          value: rw.lxmc
        }, {
          label: '开始时间',
          value: api.getDate(rw.kssj)
        }, {
          label: '结束时间',
          value: api.getDate(rw.jssj)
        }, {
          label: '验收人',
          value: rw.ysrmc
        }, {
          label: '验收时间',
          value: api.getDate(rw.yssj)
        }, {
          label: '验收结果',
          value: rw.ysRemark
        }, {
          label: '任务进度',
          value: rwjd
        }]
        return result
      }
    },
    data () {
      return {
        headTitle: '任务详情',
        showBack: false,
        rwInfo: {},
        rows: common.pageRowNo,
        page0: 1,
        page1: 1,
        type: '1',
        rwzcData0: [],
        rwzcData1: [],
        footer: {
          title: '查看更多'
        },
        tabIndex: 0
      }
    },
    methods: {
      loadRwData () {
        let _this = this
        api.post('/wx/wxxq/rwXQ.do', {rwid: _this.$route.query.rwid}).then(function (res) {
          if (res) {
            _this.rwInfo = res
            _this.loadRwzcData()
          }
        })
      },
      loadRwzcData () {
        let _this = this
        let finish = 0
        let page = _this.page0
        if (_this.tabIndex === 1) {
          finish = 1
          page = _this.page1
        }
        let param = {
          rwid: _this.$route.query.rwid,
          rows: _this.rows,
          page: page,
          finish: finish
        }
        api.post('/wx/wxxq/rwzcInfo.do', param).then(function (res) {
          if (res) {
            if (_this.tabIndex === 0) {
              _this.rwzcData0.push.apply(_this.rwzcData0, res.zcList)
              _this.page0++
            } else {
              _this.rwzcData1.push.apply(_this.rwzcData1, res.zcList)
              _this.page1++
            }
            console.log(_this.page0 + ';' + _this.page1)// 此处的log不要删除
          } else {
            _this.$vux.toast.text('没有更多数据', 'bottom')
          }
        })
      },
      toZcdetail (item) {
        this.$store.dispatch('zcxqShowBack')
        this.$router.push({name: 'Zcxq', query: {zcid: item.zcid}})
      },
      getMore () {
        this.loadRwzcData()
      },
      onItemClick (index) {
        if (index !== this.tabIndex) {
          this.tabIndex = index
          if ((index === 0 && this.rwzcData0.length === 0) || ((index === 1 && this.rwzcData1.length === 0))) {
            this.getMore()
          }
        }
      }
    },
    beforeRouteEnter (to, from, next) {
      next(vm => {
        vm.$store.dispatch('hideMainTabbar')
      })
    }
  }
</script>

<style>
  .weui-form-preview__hd .weui-form-preview__value{
    color: #999999 !important;
    font-size: 1em !important;
  }
</style>
