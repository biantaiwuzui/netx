
export default {
    name:'category-list', //类目管理
    data () {
        return {
            selectData:[],
            selectFormData:{
                name:'',
                parentId:'0',
                deleted:0,
                newParentId:'0'
            },
            parentIdItems:[
                {
                    id:null,
                    name:'全部类目'
                },{
                    id:'0',
                    name:'一级类目'
                }
            ],
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
                }
            ],
            iconItems:[
                {
                    id:1,
                    name:'前台类目'
                },{
                    id:2,
                    name:'后台类目'
                }
            ],
            paginations:{
                current_page:0,
                total:1,
                page_size:10,
                page_sizes:[10,20,30],
                layout:'total, sizes, prev, pager, next, jumper'
            },
            downDialog:{
                show:false,
                show_info:"",
                onSelectDataInfo:{
                    id:'',
                    name:''
                }
            },
            upDialog:{
                show:false,
                show_info:"",
                onSelectDataInfo:{
                    id:'',
                    name:''
                }
            },
            addDialog:{
                show:false,
                show_info:"",
                onSelectDataInfo:{
                    name:'',
                    parentId:'',
                    priority:'',
                    icon: ''
                }
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
                name:{
                    info:{
                        prop:'name',
                        label:'类目名称',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'300',
                        align:'center'
                    }
                },
                parentId:{
                    info:{
                        prop:'parentId',
                        label:'类目级别',
                        sortable:true,
                        formatter: (item) => {
                            switch(item.parentId) {
                                case '0':
                                    return "一级类目"
                                default:
                                    return "二级类目"
                            }
                        },
                    },
                    filter:{},
                    style:{
                        width:'120',
                        align:'center'
                    }
                },
                usedCount:{
                    info:{
                        prop:'usedCount',
                        label:'使用次数',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'120',
                        align:'center'
                    }
                },
                priority:{
                    info:{
                        prop:'priority',
                        label:'当前类目排序',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'150',
                        align:'center'
                    }
                },
                py:{
                    info:{
                        prop:'py',
                        label:'首字母',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'100',
                        align:'center'
                    }
                },
                icon:{
                    info:{
                        prop:'icon',
                        label:'类型',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'120',
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
                parentIdSelect,
                nameSelect,
                where,
                fn
            } = {}) {

            var query = this.$route.query

            this.paginations.current_page = page || parseInt(query.currentPage) || 1
            this.paginations.page_size = pageSize || parseInt(query.size) || this.paginations.page_size
            this.selectFormData.deleted = deletedSelect || parseInt(query.deleted) || this.selectFormData.deleted
            this.selectFormData.parentId = parentIdSelect || this.selectFormData.parentId
            this.selectFormData.name = nameSelect || this.selectFormData.name


            var data={
                deleted: this.selectFormData.deleted,
                current: this.paginations.current_page,
                size: this.paginations.page_size,
                name: this.selectFormData.name,
                parentId: this.selectFormData.parentId
            }
            if(where){
                data = Object.assign(data, where || {})
            }

            this.$$api_product_list({
                data,
                fn: result => {
                    this.selectData = result.list
                    this.paginations.total = result.total
                    fn && fn()
                }
            })
        },
        kidCategorySubmitForm(obj){
            this.selectFormData.parentId = obj
            var data = {
                deleted: this.selectFormData.deleted,
                current: this.paginations.current_page,
                size: this.paginations.page_size,
                name: this.selectFormData.name,
                parentId: this.selectFormData.parentId
            }
            this.$$api_product_list({
                data,
                fn: result => {
                    this.selectData = result.map.list
                    this.paginations.total = result.map.total
                    fn && fn()
                }
            })
            //调用后台的接口
        },
        addRefuseReset(ref){
            this.addDialog.onSelectDataInfo.name = ""
            this.addDialog.onSelectDataInfo.priority = ""
            this.addDialog.onSelectDataInfo.icon = ""
            this.$refs[ref].resetFields()
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
                onlineStatusSelect:this.selectFormData.onlineStatus,
                fn:() => {
                    this.setPath("onlineStatus",this.selectFormData.onlineStatus)
                }
            })
        },
        dateFormat:function(row, column) {
            var longTypeDate = row[column.property];
            if (longTypeDate == undefined) {
                return "";
            }
            var date = new Date();
            date.setTime(longTypeDate);
            return date.getFullYear() + "-" + (date.getUTCMonth()+1) + "-" + date.getUTCDate();
        },
        addRefuse(obj){
            this.addDialog.show = true
            this.addDialog.onSelectDataInfo.parentId = obj
        },
        //禁用类目
        downRefuse(obj){
            this.downDialog.show = true
            this.downDialog.show_info = obj
            this.downDialog.onSelectDataInfo.id = obj.id
            this.downDialog.onSelectDataInfo.name = obj.name
        },
        addSubmit(){
            var data = {
                name: this.addDialog.onSelectDataInfo.name,
                parentId: this.addDialog.onSelectDataInfo.parentId,
                icon: this.addDialog.onSelectDataInfo.icon,
                priority: this.addDialog.onSelectDataInfo.priority
            }
            this.$$api_product_addOrUpdate({
                data,
                fn: result => {
                    this.$message('添加类目成功'),
                        this.addDialog.show = false,
                        this.getList()
                }
            })
            //调用后台的接口
        },
        downRefuseReset(){
            this.downDialog.show = false
        },
        downSubmit(){
            var data = {
                id: this.downDialog.onSelectDataInfo.id
            }
            this.$$api_product_delete({
                data,
                fn: result => {
                    this.$message('禁用成功'),
                    this.downDialog.show = false,
                    this.getList()
                }
            })
            //调用后台的接口
        },
        //启用类目
        upRefuse(obj){
            this.upDialog.show = true
            this.upDialog.show_info = obj
            this.upDialog.onSelectDataInfo.id = obj.id
            this.upDialog.onSelectDataInfo.name = obj.name
        },
        upRefuseReset(ref){
            this.upDialog.show = false
        },
        upSubmit(){
            var data = {
                id: this.upDialog.onSelectDataInfo.id
            }
            this.$$api_product_recovery({
                data,
                fn: result => {
                    this.$message('重新上架成功'),
                    this.upDialog.show = false,
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
