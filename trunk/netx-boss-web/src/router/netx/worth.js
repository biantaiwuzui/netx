import { Content } from 'layout/'
import { Netx } from 'views/'

export default {
    path: 'worth',
    name: '网能管理',
    icon: 'inbox',
    component: Content,
    redirect: '/netx/worth/queryWishList',
    children: [{
        path: 'queryWishList',
        name: '心愿提现列表',
        icon: 'reorder',
        component: Netx.Worth.Wish
    }]
}