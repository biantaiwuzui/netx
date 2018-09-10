/**
 * Created by sailengsi on 2017/4/30.
 */

/**
 * 用户模块
 * @type {Object}
 */
export default [
  {
    name: '登录',
    method: 'login',
    path: '/auth/login',
    type: 'post'
  },
  {
    name: '获取用户详细信息',
    method: 'getUserProfile',
    path: '/getUserProfile',
    type: 'post'
  },
  {
    name: '获取用户教育背景',
    method: 'getUserEducation',
    path: '/getUserEducation',
    type: 'post'
  }, {
        name: '获取用户教育背景',
        method: 'editUserEducationInfo',
        path: '/editUserEducationInfo',
        type: 'post'
    },
    {
        name: '根据id用户删除教育背景',
        method: 'deleteUserEducation',
        path: '/deleteUserEducation',
        type: 'post'
    },
  {
        name: '修改或新增用户',
        method: 'updateUserDetails',
        path: '/updateUserDetails',
        type: 'post'
    },
    {
        name: '修改用户详情',
        method: 'editUser',
        path: '/editUser',
        type: 'post'
    },
    {
        name: '根据信息id修改用户兴趣信息',
        method: 'editUserHobbyInfo',
        path: '/editUserHobbyInfo',
        type: 'post'
    },
    {
        name: '根据信息id修改用户工作经历',
        method: 'editUserWorkExperience',
        path: '/editUserWorkExperience',
        type: 'post'
    },
  {
    name: '获取用户详细信息',
    method: 'getUserProfile',
    path: '/getUserProfile',
    type: 'post'
  },
  {
    name: '获取用户工作经验',
    method: 'getUserProfession',
    path: '/getUserProfession',
    type: 'post'
  },
  {
    name: '删除用户工作经验',
    method: 'deleteUserProfessionDetail',
    path: '/deleteUserProfessionDetail',
    type: 'post'
  },
  {
    name: '增加用户工作经验',
    method: 'insertUserProfessionDetail',
    path: '/insertUserProfessionDetail',
    type: 'post'
  },
  {
    name: '获取用户兴趣爱好',
    method: 'selectUserInterest',
    path: '/selectUserInterest',
    type: 'post'
  },
  {
    name: '删除用户兴趣爱好',
    method: 'deleteUserInterestDetail',
    path: '/deleteUserInterestDetail',
    type: 'post'
  },
  {
    name: '增加用户兴趣爱好',
    method: 'insertUserInterestDetail',
    path: '/insertUserInterestDetail',
    type: 'post'
  },
  {
    name: '获取用户认证列表',
    method: 'selectUserVerifyList',
    path: '/selectUserVerifyList',
    type: 'post'
  },
  {
    name: '根据用户id获取用户认证列表',
    method: 'selectUserVerify',
    path: '/selectUserVerify',
    type: 'post'
  },{
    name: '根据认证id和用户id审核用户认证内容操作',
    method: 'operateUserVerify',
    path: '/operateUserVerify',
    type: 'post'
  },{
    name: '注册',
    method: 'register',
    path: '/User/register',
    type: 'post'
  },
  {
    name: '获取用户列表',
    method: 'selectUser',
    path: '/getUserList',
    type: 'post'
  },
  {
    name: '添加修改用户公用接口',
    method: 'saveUser',
    path: '/User/saveUser',
    type: 'post'
  },
  {
    name: '删除用户',
    method: 'deleteUserById',
    path: '/deleteUserById',
    type: 'post'
  },
  {
    name: '获取用户信息',
    method: 'findUser',
    path: '/User/findUser',
    type: 'get'
  },
  {
    name: '修改密码',
    method: 'updatePass',
    path: '/User/updatePass',
    type: 'post'
  },
  {
    name: '设置权限',
    method: 'updateUserAccess',
    path: '/User/updateUserAccess',
    type: 'post'
  },
  {
    name: '设置用户状态',
    method: 'updateUserStatus',
    path: '/User/updateUserStatus',
    type: 'post'
  },
  {
    name: '系统用户黑名单,白名单 ',
    method: 'getSystemBlackList',
    path: '/queryUserInSystemBlacklist',
    type: 'post'
  },
  {
    name: '操作系统用户黑，白名单 ',
    method: 'operateSystemBlacklist',
    path: '/operateSystemBlacklist',
    type: 'post'
  },
    {
        name: '操作建议列表 ',
        method: 'getSuggestPassList',
        path: '/getUserSuggestList',
        type: 'post'
    },
    {
        name: '操作建议列表 ',
        method: 'suggest',
        path: '/suggest',
        type: 'post'
    },
    {
        name: '后台添加通过建议 ',
        method: 'addSuggestPass',
        path: '/addSuggestPass',
        type: 'post'
    },
    {
        name:'后台密码判断',
        method:'passwordCheck',
        path:'/passwordCheck',
        type:'post'
    }
]
