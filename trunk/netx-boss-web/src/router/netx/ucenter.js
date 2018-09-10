/**
 * Created by sailengsi on 2017/5/11.
 */

import { Content } from 'layout/'
import { Netx } from 'views/'

export default {
  path: 'ucenter',
  name: '用户管理',
  icon: 'inbox',
  component: Content,
  redirect: '/netx/ucenter/list',
  children: [{
    path: 'list',
    name: '用户列表',
    icon: 'reorder',
    component: Netx.Ucenter.List
  }, {
    path: 'edit',
    name: '编辑用户',
    hidden: true,
    icon: 'edit',
    component: Netx.Ucenter.Edit
  }, {
      path: 'detail',
      name: '用户详情',
      hidden: true,
      icon: 'reorder',
      component: Netx.Ucenter.Detail,
  },{
      path: 'education',
      name: '教育背景',
      hidden: true,
      icon: 'reorder',
      component: Netx.Ucenter.Education
  },{
      path: 'profession',
      name: '工作经历',
      hidden: true,
      icon: 'reorder',
      component: Netx.Ucenter.Profession
  },{
      path: 'interest',
      name: '兴趣爱好',
      hidden: true,
      icon: 'reorder',
      component: Netx.Ucenter.Interest
  },{
      path: 'verify',
      name: '用户认证',
      icon: 'reorder',
      component: Netx.Ucenter.Verify
  },{
    path: 'systemBlacklist',
    name: '系统用户黑白名单',
    icon: 'reorder',
    component: Netx.Ucenter.System.BlackList
  },{
      path: 'systemBlacklists',
      name: '用户建议列表',
      icon: 'reorder',
      component: Netx.Ucenter.Suggest.PassList
  },{
    path: 'verifyResource',
    name: '用户认证资源',
    hidden: true,
    icon: 'reorder',
    component: Netx.Ucenter.VerifyResource
}]
}
