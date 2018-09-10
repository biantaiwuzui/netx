import { Content } from 'layout/'
import { Netx } from 'views/'

export default {
    path: 'product',
    name: '商品管理',
    icon: 'inbox',
    component: Content,
    redirect: '/netx/product/queryProductList',
    children: [{
        path: 'queryProductList',
        name: '商品列表',
        icon: 'reorder',
        component: Netx.Product.Product
    },{
        path: 'category',
        name: '类目列表',
        icon: 'reorder',
        component: Netx.Category.Category
    }]
}
