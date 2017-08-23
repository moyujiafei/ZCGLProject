import { base64 } from 'vux'
export default {
  state: {
    testToken: null
  },
  mutations: {},
  actions: {},
  getters: {
    testToken (state) {
      if (!state.testToken) {
        let token = {
          'TOKEN_KEY_CREATE_DATE': new Date().getTime(),
          'TOKEN_KEY_APP_ID': 17,
          'TOKEN_KEY_USER_ID': 'shangwei'
        }
        return base64.encode(JSON.stringify(token))
      } else {
        return state.testToken
      }
    }
  }
}
