/**
 * Created by sailengsi on 2017/4/30.
 */
/**
 * 文章管理
 * @type {Object}
 */
export default [
  {
    name: '后台删除咨讯',
    method: 'deleteArticleByBoss',
    path: '/deleteArticleByBoss',
    type: 'post'
  },
  {
    name: '获取用户发布的文章列表',
    method: 'getUserArticleList',
    path: '/getUserArticleList',
    type: 'post'
  },
  {
    name: '列入资讯异常',
    method: 'pushException',
    path: '/pushException',
    type: 'post'
  },
  {
    name: '解除资讯异常',
    method: 'popException',
    path: '/popException',
    type: 'post'
  },
  {
    name: '押金退回',
    method: 'returnAmount',
    path: '/returnAmount',
    type: 'post'
  },
  {
    name:"修改咨询状态",
    method:'modifyArticleStatus',
    path:'/modifyArticleStatus',
    type:'post'
   }
]
