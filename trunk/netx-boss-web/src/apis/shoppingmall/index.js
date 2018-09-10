/**
 * lcx
 */
/**
 * 商家管理管理
 * @type {Object}
 */
export default [
  {
    name: '获取商家列表',
    method: 'getSellerList',
    path: '/business/seller/getSellerList',
    type: 'post'
  },{
    name: '拉黑商家',
    method: 'defriend',
    path: '/business/seller/defriend',
    type: 'post'
  },{
    name: '解除黑名单',
    method: 'overBack',
    path: '/business/seller/overBack',
    type: 'post'
  }
]
