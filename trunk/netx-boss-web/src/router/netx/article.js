/**
 * Created by sailengsi on 2017/5/11.
 */

import { Content } from 'layout/'

import { Netx } from 'views/'

export default {
  path: 'article',
  name: '图文管理',
  icon: 'inbox',
  component: Content,
  redirect: '/netx/article/list',
  children: [{
    path: 'list',
    name: '图文列表',
    icon: 'reorder',
    component: Netx.Article.List
  }, {
    path: 'edit',
    name: '编辑文章',
    icon: 'edit',
    component: Netx.Article.Edit
  }]
}
