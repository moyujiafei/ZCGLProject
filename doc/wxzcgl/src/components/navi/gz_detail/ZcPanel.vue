<template>
  <div>
    <panel :footer="footer" :list="zcListData" :type="type" @on-click-footer="loadZcListData" @on-click-item="toZcdetail"></panel>
  </div>
</template>
<script>
  import api from '@/api'
  import common from '@/components/common'
  import { Panel, stringTrim } from 'vux'
  export default {
    components: {
      Panel
    },
    props: ['tagName', 'tabIndex', 'doSearchIndex', 'searchParam'],
    data () {
      return {
        rows: common.pageRowNo,
        page: 1,
        type: '5',
        zcListData: [],
        footer: {
          title: '查看更多'
        }
      }
    },
    watch: {
      doSearchIndex () {
        if (this.doSearchIndex) {
          this.page = 1
          this.zcListData = []
          this.loadZcListData()
        }
      }
    },
    mounted () {
      this.loadZcListData()
    },
    methods: {
      loadZcListData () {
        let _this = this
        let param = {
          rows: _this.rows,
          page: _this.page,
          tagName: _this.tagName,
          pageUrl: document.URL,
          tabFlag: _this.tabIndex
        }
        if (_this.searchParam) {
          param['searchParam'] = stringTrim(_this.searchParam)
        }
        let url = '/wx/zcgl/getZCList.do'
        if (url) {
          api.post(url, param).then(function (res) {
            if (res) {
              // 添加资产列表
              let zcList = res.zcList
              _this.page++
              _this.zcListData.push.apply(_this.zcListData, zcList)
              // 设置统计数量
              if (res.countNo) {
                _this.$store.dispatch('setGzCount', res.countNo.toString())
              }
              console.log(_this.page)// 此处的log不要删除
            } else {
              _this.$vux.toast.text('没有更多数据', 'bottom')
            }
          })
        }
      },
      toZcdetail (item) {
        this.$store.dispatch('zcxqShowBack')
        this.$router.push({name: 'ZcxqNav', query: {zcid: item.zcid, tagName: this.tagName}})
      }
    }
  }
</script>
<style scoped>

</style>
