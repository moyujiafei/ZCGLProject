<template>
  <div>
    <ZcPanel :tag-name="tagName" :tab-index="tabIndex" :do-search-index="doSearchIndex"  :search-param="searchParam"></ZcPanel>
  </div>
</template>
<script>
  import ZcPanel from './ZcPanel'
  export default {
    components: {
      ZcPanel
    },
    props: ['tagName', 'tabIndex', 'doSearchIndex', 'searchParam']
  }
</script>
<style scoped>
  
</style>
