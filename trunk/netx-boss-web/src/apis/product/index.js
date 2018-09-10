/**
 * 商品管理
 * CHEN-QIAN
 * @type {Object}
 */
export default [
  {
    name: '获取商品列表',
    method: 'queryProductList',
    path: '/business/product/queryProductList',
    type: 'post'
  },{
    name: '商品上架',
    method: 'up',
    path: '/business/product/up',
    type: 'post'
  },{
    name: '强制下架',
    method: 'coercionDown',
    path: '/business/product/coercionDown',
    type: 'post'
  },{
    name: '商品类目列表',
    method: 'list',
    path: '/business/category/list',
    type: 'post'
  },{
    name: '添加商品类目',
    method: 'addOrUpdate',
    path: '/business/category/addOrUpdate',
    type: 'post'
  },{
    name: '禁用商品类目',
    method: 'delete',
    path: '/business/category/delete',
    type: 'post'
  },{
    name: '恢复商品类目',
    method: 'recovery',
    path: '/business/category/recovery',
    type: 'post'
  }
]
