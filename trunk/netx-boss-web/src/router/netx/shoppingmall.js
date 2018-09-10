/**
 * Created by sailengsi on 2017/5/11.
 */

import { Content } from 'layout/'
import { Netx } from 'views/'

export default {
  path: 'shoppingmall',
  name: '商家管理',
  icon: 'inbox',
  component: Content,
  redirect: '/netx/shoppingmall/sellerWhite',
  children: [{
    path: 'sellerWhite',
    name: '商家白名单列表',
    icon: 'reorder',
    component: Netx.Shoppingmall.SellerWhite
  },{
    path: 'sellerBack',
    name: '商家黑名单列表',
    icon: 'reorder',
    component: Netx.Shoppingmall.SellerBack
  }]
}
