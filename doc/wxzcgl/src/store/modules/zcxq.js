export default {
  state: {
    showBack: false
  },
  mutations: {
    zcxqShowBack (state) {
      state.showBack = true
    },
    zcxqHideBack (state) {
      state.showBack = false
    }
  },
  actions: {
    zcxqShowBack: ({commit}) => {
      commit('zcxqShowBack')
    },
    zcxqHideBack: ({commit}) => {
      commit('zcxqHideBack')
    }
  },
  getters: {
    zcxqBackFlag (state) {
      return state.showBack
    }
  }
}
