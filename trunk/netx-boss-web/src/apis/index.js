/**
 * Created by sailengsi on 2017/5/11.
 */

/**
 * 导出所有模块需要用到接口
 * 一级属性：模块名
 * 一级属性中的方法：当前模块需要用的接口
 * @type {Object}
 */

import user from './user/'
import article from './article/'
import order from './order/'
import system from './system/'
import test from './test/'
import common from './common'
import shoppingmall from './shoppingmall'
import product from './product'
import worth from './worth'
import admin from './admin'

export default [{
  module: 'user',
  name: '用户管理',
  list: user
}, {
  module: 'shoppingmall',
  name: '商家管理',
  list: shoppingmall
}, {
    module: 'product',
    name: '商品管理',
    list: product
}, {
    module: 'worth',
    name: '网能管理',
    list: worth
}, {
  module: 'article',
  name: '文章管理',
  list: article
}, {
  module: 'order',
  name: '订单管理',
  list: order
}, {
  module: 'system',
  name: '系统设置',
  list: system
}, {
  module: 'test',
  name: '测试模块',
  list: test
}, {
    module: 'common',
    name: '通用模块',
    list: common
}, {
    module: 'admin',
    name: '管理员管理',
    list: admin
}]
