
export default {
    name:'admin-list', //商品管理
    data () {
        return {
            selectData:[],
            selectFormData:{
                userName:'',
                realName:'',
                deleted:0
            },
            deletedItems:[
                {
                    id:0,
                    name:'正常'
                },{
                    id:1,
                    name:'禁用'
                },{
                    id:null,
                    name:'全部'
                },
            ],
            superAdminItems:[
                {
                    id:false,
                    name:'否'
                },{
                    id:true,
                    name:'是'
                }
            ],
            paginations:{
                current_page:0,
                total:0,
                page_size:10,
                page_sizes:[10,20,30],
                layout:'total, sizes, prev, pager, next, jumper'
            },
            addDialog:{
                show:false,
                show_info:"",
                onSelectDataInfo:{
                    userName:'',
                    realName:'',
                    password:'',
                    mobile:'',
                    superAdmin:''
                }
            },
            resetDialog:{
                show:false,
                show_info:"",
                onSelectDataInfo:{
                    userName:'',
                    password:''
                }
            },
            downDialog:{
                show:false,
                show_info:"",
                onSelectDataInfo:{
                    id:'',
                    userName:'',
                    reason:'',
                }
            },
            down_rules:{
                reason:[{
                    required:true,
                    message:'禁用的理由不能为空',
                    trigger: 'change'
                }]
            },
            upDialog:{
                show:false,
                show_info:"",
                onSelectDataInfo:{
                    id:'',
                    userName:'',
                    reason:'',
                }
            },
            up_rules:{
                reason:[{
                    required:true,
                    message:'恢复的理由不能为空',
                    trigger: 'change'
                }]
            },
            //样式配置
            fields:{
                id:{
                    info:{
                        prop:'id',
                        label:'主键id',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'300',
                        align:'center'
                    }
                },
                userName:{
                    info:{
                        prop:'userName',
                        label:'登录名',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'150',
                        align:'center'
                    }
                },
                realName:{
                    info:{
                        prop:'realName',
                        label:'真实姓名',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'120',
                        align:'center'
                    }
                },
                mobile:{
                    info:{
                        prop:'mobile',
                        label:'手机号码',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'150',
                        align:'center'
                    }
                },
                superAdmin:{
                    info:{
                        prop:'superAdmin',
                        label:'超级管理员',
                        sortable:true,
                        formatter: (item) => {
                            if (item.superAdmin) {
                                return "是"
                            }else{
                                return "否"
                            }
                        },
                    },
                    filter:{},
                    style:{
                        width:'130',
                        align:'center'
                    }
                },
                deleted:{
                    info:{
                        prop:'deleted',
                        label:'状态',
                        sortable:true,
                        formatter: (item) => {
                            if (item.deleted == 0) {
                                return "正常"
                            }else{
                                return "禁用"
                            }
                        },
                    },
                    filter:{},
                    style:{
                        width:'100',
                        align:'center'
                    }
                },
                reason:{
                    info:{
                        prop:'reason',
                        label:'禁用/恢复原因',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'300',
                        align:'center'
                    }
                },
                createUserName:{
                    info:{
                        prop:'createUserName',
                        label:'创建人',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'130',
                        align:'center'
                    }
                },
                updateUserName:{
                    info:{
                        prop:'updateUserName',
                        label:'更新人',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'130',
                        align:'center'
                    }
                },
                createTime:{
                    info:{
                        prop:'createTime',
                        label:'创建时间',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'120',
                        align:'center'
                    }
                }
            }
        }
    },
    methods:{
        onChangePageSize(pageSize){
            this.getList({
                pageSize,
                fn: () => {
                    this.setPath('page_size',pageSize)
                }
            })
        },
        onChangeCurrentPage(page){
            this.getList({
                page,
                fn:() => {
                    this.setPath('page',page)
                }
            })
        },
        getList({
                page,
                pageSize,
                deletedSelect,
                userNameSelect,
                realNameSelect,
                where,
                fn
            } = {}) {

            var query = this.$route.query

            this.paginations.current_page = page || parseInt(query.currentPage) || 1
            this.paginations.page_size = pageSize || parseInt(query.size) || this.paginations.page_size
            this.selectFormData.deleted = deletedSelect || parseInt(query.deleted) || this.selectFormData.deleted
            this.selectFormData.userName = userNameSelect || this.selectFormData.userName
            this.selectFormData.realName = realNameSelect || this.selectFormData.realName


            var data={
                deleted: this.selectFormData.deleted,
                current: this.paginations.current_page,
                size: this.paginations.page_size,
                userName: this.selectFormData.userName,
                realName: this.selectFormData.realName
            }
            if(where){
                data = Object.assign(data, where || {})
            }

            this.$$api_admin_queryUserAdmins({
                data,
                fn: result => {
                    this.selectData = result.map.list
                    this.paginations.total = result.map.total
                     console.log(result.map.list)
                    // console.log(result.map.total)
                    fn && fn()
                }
            })
        },
        setPath(field ,value){
            var path = this.$route.path
            var query = Object.assign({},this.$route.query)

            if(typeof field ==='object'){
                query = field
            }else{
                query[field] = value
            }
        },
        //查询
        onSubmitForm(){
            this.getList({
                deletedSelect:this.selectFormData.deleted,
                fn:() => {
                    this.setPath("deleted",this.selectFormData.deleted)
                }
            })
        },
        dateFormat:function(row, column) {
            var longTypeDate = row[column.property];
            if (longTypeDate == undefined) {
                return "";
            }
            var date = new Date(longTypeDate);
            date.setTime(longTypeDate);
            return date.getFullYear() + "-" + (date.getUTCMonth()+1) + "-" + date.getUTCDate();
        },
        addRefuse(){
            this.addDialog.show = true
        },
        addRefuseReset(ref){
            this.addDialog.onSelectDataInfo.userName = ""
            this.addDialog.onSelectDataInfo.realName = ""
            this.addDialog.onSelectDataInfo.password = ""
            this.addDialog.onSelectDataInfo.mobile = ""
            this.addDialog.onSelectDataInfo.superAdmin = ""
            this.$refs[ref].resetFields()
        },
        //禁用
        downRefuse(obj){
            this.downDialog.show = true
            this.downDialog.show_info = obj
            this.downDialog.onSelectDataInfo.id = obj.id
        },
        resetRefuse(obj){
            this.resetDialog.show = true
            this.resetDialog.show_info = obj
            this.resetDialog.onSelectDataInfo.userName = obj.userName
        },
        resetRefuseReset(ref){
            this.resetDialog.onSelectDataInfo.password = ""
            this.$refs[ref].resetFields()
        },
        downRefuseReset(ref){
            this.downDialog.onSelectDataInfo.reason=""
            this.$refs[ref].resetFields()
        },
        downSubmit(){
            var data = {
                id: this.downDialog.onSelectDataInfo.id,
                userName: JSON.parse(window.localStorage.netx_userinfo).username,
                reason: this.downDialog.onSelectDataInfo.reason
            }
            this.$$api_admin_delete({
                data,
                fn: result => {
                    this.$message('禁用成功'),
                    this.downDialog.show = false,
                    this.getList()
                }
            })
            //调用后台的接口
        },
        //恢复
        upRefuse(obj){
            this.upDialog.show = true
            this.upDialog.show_info = obj
            this.upDialog.onSelectDataInfo.id = obj.id
        },
        upRefuseReset(ref){
            this.upDialog.onSelectDataInfo.reason=""
            this.$refs[ref].resetFields()
        },
        upSubmit(){
            var data = {
                id: this.upDialog.onSelectDataInfo.id,
                userName: JSON.parse(window.localStorage.netx_userinfo).username,
                reason: this.upDialog.onSelectDataInfo.reason
            }
            this.$$api_admin_delete({
                data,
                fn: result => {
                    this.$message('恢复管理员成功'),
                    this.upDialog.show = false,
                    this.getList()
                }
            })
            //调用后台的接口
        },
        addSubmit(){
            var data = {
                userName: this.addDialog.onSelectDataInfo.userName,
                createUserName: JSON.parse(window.localStorage.netx_userinfo).username,
                realName: this.addDialog.onSelectDataInfo.realName,
                password: this.addDialog.onSelectDataInfo.password,
                mobile: this.addDialog.onSelectDataInfo.mobile,
                superAdmin: this.addDialog.onSelectDataInfo.superAdmin
            }
            this.$$api_admin_addUserAdmin({
                data,
                fn: result => {
                    this.$message('添加管理员成功'),
                        this.addDialog.show = false,
                        this.getList()
                }
            })
            //调用后台的接口
        },
        resetSubmit(){
            var data = {
                userName: this.resetDialog.onSelectDataInfo.userName,
                superUserName: JSON.parse(window.localStorage.netx_userinfo).username,
                password: this.resetDialog.onSelectDataInfo.password
            }
            this.$$api_admin_resetPassword({
                data,
                fn: result => {
                    this.$message('重置密码成功'),
                        this.resetDialog.show = false,
                        this.getList()
                }
            })
            //调用后台的接口
        }
    },
    mounted(){
        this.getList({
        })
    }
}
