/**
 * Created by sailengsi on 2017/5/10.
 */
import {
  store
} from 'utils/'

import * as types from './mutations_types'

export default {
  [types.UPDATE_USERINFO] (state, userDb) {
    state.userinfo = userDb.userinfo || {}
    store.set('userinfo', state.userinfo)
  },

  [types.REMOVE_USERINFO] (state) {
    store.remove('userinfo')
    state.userinfo = {}
  }
}
