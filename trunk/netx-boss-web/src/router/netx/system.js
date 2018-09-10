/**
 * Created by sailengsi on 2017/5/11.
 */

import { Content } from 'layout/'

import { Netx } from 'views/'

export default {
  path: 'system',
  name: '系统设置',
  icon: 'inbox',
  component: Content,
  redirect: '/netx/system/tags',
  children: [{
    path: 'tags',
    name: '标签管理',
    icon: 'reorder',
    component: Netx.System.Tags
  },
  {
      path: 'luckyMoney',
      name: '红包设置',
      icon: 'reorder',
      component: Netx.System.LuckyMoney
  },
  {
      path: 'addLuckyMoney',
      name: '增加红包设置',
      hidden: true,
      icon: 'reorder',
      component: Netx.System.AddLuckyMoney
  },
  {
    path: 'innerTags',
        name: '内置标签',
    icon: 'reorder',
    hidden: true,
    component: Netx.System.InnerTags
  },
  {
    path: 'skillTags',
        name: '技能标签',
    icon: 'reorder',
    hidden: true,
    component: Netx.System.SkillTags
  },
  {
    path: 'interestTags',
    name: '兴趣标签',
    icon: 'reorder',
    hidden: true,
    component: Netx.System.InterestTags
  },
  {
    path: 'productTags',
    name: '商品标签',
    icon: 'reorder',
    hidden: true,
    component: Netx.System.ProductTags
  },
  {
    path: 'productKidTags',
    name: '商品子标签',
    icon: 'reorder',
    hidden: true,
    component: Netx.System.ProductKidTags
  },
  {
    path: 'otherSet',
    name: '其他设置',
    icon: 'reorder',
    component: Netx.System.OtherSet
  },
  {
    path: 'addOtherSet',
    name: '添加/更新其他设置',
    icon: 'reorder',
    hidden: true,
    component: Netx.System.AddOtherSet
  }]
}
