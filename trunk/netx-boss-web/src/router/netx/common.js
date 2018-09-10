import { Content } from 'layout/'
import { Netx } from 'views/'


export default {
    path:'common',
    name:'通用管理',
    icon:'inbox',
    component: Content,
    redirect: '',
    children:[
        {
        path:'arbitrationList',
        name:'仲裁列表',
        icon:'reorder',
        component:Netx.Common.Arbitration.List
         },
        {
            path:'arbitrationOperator',
            name:'仲裁受理',
            icon:'reorder',
            hidden:true,
            component:Netx.Common.Arbitration.Operator
        },
        {
            path:'costSettingIndex',
            name:'费用标准',
            icon:'reorder',
            component:Netx.Common.CostSetting
        }
    ]
}