const tagNames = {
  hqglryz: '后勤管理人员组',
  syrz: '使用人组',
  wxz: '维修组',
  xjz: '巡检组',
  bmzcglyz: '部门资产管理员组'
}
const pageRowNo = 10
export default {
  tagNames,
  pageRowNo,
  scanCodeInfo (result) {
    if (result) {
      let json = {}
      try {
        json = JSON.parse(result.resultStr)
        json = json.zcdm
        if (json == null || json === '') {
          json = result.resultStr
        }
      } catch (error) {
        json = result.resultStr
      }
      if (json.length > 35) {
        return false
      }
      return json
    } else {
      return false
    }
  }
}

