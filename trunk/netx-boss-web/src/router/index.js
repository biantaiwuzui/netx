/**
 * Created by sailengsi on 2017/5/11.
 */
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

// import { Home } from 'layout/'
import { Login } from 'views/'

import Adv from './adv/'
import Function from './function/'
import Netx from './netx/'
import components from './components/'

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      hidden: true,
      redirect (to) {
        return 'login'
      }
    }, {
      path: '/login',
      name: '登录',
      hidden: true,
      component: Login
    },
    Netx,
    Function,
    components,
    Adv
  ]
})
