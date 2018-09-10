/**
 * 管理员管理
 * CHEN-QIAN
 * @type {Object}
 */
export default [
  {
    name: '管理员列表',
    method: 'queryUserAdmins',
    path: '/auth/queryUserAdmins',
    type: 'post'
  },{
    name: '添加管理员',
    method: 'addUserAdmin',
    path: '/auth/addUserAdmin',
    type: 'post'
  },{
    name: '修改密码',
    method: 'updatePassword',
    path: '/auth/updatePassword',
    type: 'post'
  },{
    name: '禁用/恢复',
    method: 'delete',
    path: '/auth/delete',
    type: 'post'
  },{
    name: '重置密码',
    method: 'resetPassword',
    path: '/auth/resetPassword',
    type: 'post'
    }
]
