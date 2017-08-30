export default {
  state: {
    showMainTabbar: true,
    gzCountNo: ''
  },
  mutations: {
    hideMainTabbar (state) {
      state.showMainTabbar = false
    },
    showMainTabbar (state) {
      state.showMainTabbar = true
    },
    clearGzCount (state) {
      state.gzCountNo = ''
    },
    setGzCount (state, payload) {
      state.gzCountNo = payload.count
    }
  },
  actions: {
    hideMainTabbar: ({commit}) => {
      commit('hideMainTabbar')
    },
    showMainTabbar: ({commit}) => {
      commit('showMainTabbar')
    },
    clearGzCount: ({commit}) => {
      commit('clearGzCount')
    },
    setGzCount: ({commit}, count) => {
      if (count) {
        commit({type: 'setGzCount', count: count})
      }
    }
  },
  getters: {
    showMainTabbar (state) {
      return state.showMainTabbar
    },
    gzCountNo (state) {
      return state.gzCountNo
    }
  }
}
