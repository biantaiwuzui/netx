/**
 * Created by sailengsi on 2017/5/11.
 */

import { Home } from 'layout/'

import article from './article.js'
import ucenter from './ucenter.js'
import orderstatis from './order-statis.js'
import system from './system.js'
import common from './common.js'
import shoppingmall from './shoppingmall.js'
import product from './product.js'
import worth from './worth.js'
import admin from './admin.js'

export default {
  path: '/netx',
  name: '用户管理',
  icon: 'inbox',
  component: Home,
  redirect: '/netx/ucenter',
  children: [article, ucenter, shoppingmall, product, orderstatis, worth, system, common, admin]
}
