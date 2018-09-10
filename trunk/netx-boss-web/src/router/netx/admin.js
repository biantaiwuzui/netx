import { Content } from 'layout/'
import { Netx } from 'views/'

export default {
    path: 'admin',
    name: '管理员管理',
    icon: 'inbox',
    component: Content,
    redirect: '/netx/admin/queryUserAdmins',
    children: [{
        path: 'queryUserAdmins',
        name: '管理员列表',
        icon: 'reorder',
        component: Netx.Admin.Admin
    }]
}
