export default [
    {
        name:'仲裁管理',
        method: 'getArbitrationList',
        path:'/common/arbitration/getArbitrationListByPage',
        type:'post'
    },
    {
        name:'拒绝仲裁受理',
        method: 'refuseAcceptHandle',
        path:'/common/arbitration/refuseAcceptHandle',
        type:'post'
    }
    ,
    {
        name:'仲裁受理',
        method: 'acceptHandle',
        path:'/common/arbitration/acceptHandle',
        type:'post'
    }
    ,
    {
        name:'查询费用设定',
        method: 'costSettingQueryList',
        path:'/common/costSetting/queryList',
        type:'post'
    }
    ,
    {
        name:'查询费用设定',
        method: 'costSettingQuery',
        path:'/common/costSetting/query',
        type:'post'
    }
    ,
    {
        name:'审核',//dispose(必填):0审核未通过,1审核通过,disposeUser(必填)
        method: 'costSettingDispose',
        path:'/common/costSetting/dispose',
        type:'post'
    }
    ,
    {
        name:'添加费用设置',
        method: 'costSettingSave',
        path:'/common/costSetting/save',
        type:'post'
    }
    ,
    {
        name:'超级管理员添加费用设置',
        method: 'costSettingSaveSuper',
        path:'/common/costSetting/saveSuper',
        type:'post'
    }
]