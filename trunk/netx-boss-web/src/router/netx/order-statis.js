/**
 * Created by sailengsi on 2017/5/11.
 */

import { Content } from 'layout/'

import { Netx } from 'views/'

// console.log(Demo);

export default {
  path: 'orderstatis',
  name: '订单统计',
  icon: 'inbox',
  component: Content,
  redirect: '/netx/orderstatis/bar',
  children: [{
    path: 'bar',
    name: '柱状图',
    icon: 'reorder',
    component: Netx.OrderStatis.Bar
  }, {
    path: 'pie',
    name: '饼状图',
    icon: 'edit',
    component: Netx.OrderStatis.Pie
  }]
}
