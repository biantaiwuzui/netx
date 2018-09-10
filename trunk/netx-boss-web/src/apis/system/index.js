/**
 * Created by sailengsi on 2017/4/30.
 */
/**
 * 系统设置
 * @type {Object}
 */
export default [
  {
    name: '获取系统设置信息',
    method: 'getSetting',
    path: '/System/getSetting',
    type: 'get'
  },
  {
    name: '模糊分页查询标签',
    method: 'selectTags',
    path: '/common/tags/selectTags',
    type: 'post'
  },
  {
    name: '分页查询商品一级标签',
    method: 'selectProductTags',
    path: '/common/tags/selectProductTags',
    type: 'post'
  },
  {
    name: '分页查询商品二级标签',
    method: 'selectKidTags',
    path: '/common/tags/selectKidTags',
    type: 'post'
  },
  {
    name: '添加或修改商品类别',
    method: 'addOrUpdateCategoryTags',
    path: '/common/tags/addOrUpdate',
    type: 'post'
  },
  {
    name: '删除商品类别',
    method: 'deleteCategoryById',
    path: '/common/tags/deleteCategoryById',
    type: 'post'
  },
  {
      name: '增加红包设置',
      method: 'add',
      path: '/luckyMoney/add',
      type: 'post'
  },
  {
      name: '获取红包设置',
      method: 'query',
      path: '/luckyMoney/query',
      type: 'post'
  },
  {
      name: '修改昨日红包为今日红包',
      method: 'updateLuckMoneySet',
      path: '/luckyMoney/updateLuckMoneySet',
      type: 'post'
  },
  {
      name: '审核红包设置',
      method: 'examineRedpacket',
      path: '/luckyMoney/examineRedpacket',
      type: 'post'
  },
  {
    name: '通过id删除标签',
    method: 'deleteTagsById',
    path: '/common/tags/delete',
    type: 'post'
  },
  {
    name: '添加标签',
    method: 'addTags',
    path: '/common/tags/saveOrUpdate',
    type: 'post'
  },
  {
    name: '修改标签',
    method: 'updateTags',
    path: '/common/tags/updateTags',
    type: 'post'
  },
  {
    name: '查询所有其他设置',
    method: 'getList',
    path: '/common/otherSet/getList',
    type: 'post'
  },
  {
    name: '添加其他设置',
    method: 'addOtherSet',
    path: '/common/otherSet/saveOrUpdate',
    type: 'post'
  },
  {
    name: '删除其他设置',
    method: 'deleteOtherSet',
    path: '/common/otherSet/delete',
    type: 'post'
  },
  {
    name: '通过其他设置审核',
    method: 'dispose',
    path: '/common/otherSet/dispose',
    type: 'post'
  }
]
