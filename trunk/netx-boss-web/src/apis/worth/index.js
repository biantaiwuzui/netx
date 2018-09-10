/**
 * 网能模块
 * CHEN-QIAN
 * @type {Object}
 */
export default [
  {
    name: '申请提现列表',
    method: 'queryWishList',
    path: '/worth/wish/queryWishList',
    type: 'post'
  },{
    name: '提现成功',
    method: 'success',
    path: '/worth/wish/success',
    type: 'post'
  },{
    name: '提现失败',
    method: 'fail',
    path: '/worth/wish/fail',
    type: 'post'
  }
]
