
export default {
    name:'product-list', //商品管理
    data () {
        return {
            selectData:[],
            selectFormData:{
                merchantName:'',
                productName:'',
                onlineStatus:null
            },
            onlineStatusItems:[
                {
                    id:null,
                    name:'全部'
                },{
                    id:1,
                    name:'上架'
                },{
                    id:2,
                    name:'下架'
                },{
                    id:3,
                    name:'强制下架'
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
                    productId:'',
                    userId:'',
                    onlineStatus:'',
                    reason:'',
                }
            },
            down_rules:{
                reason:[{
                    required:true,
                    message:'强制下架的理由不能为空1',
                    trigger: 'blur'
                }]
            },
            upDialog:{
                show:false,
                show_info:"",
                onSelectDataInfo:{
                    productId:'',
                    userId:'',
                    onlineStatus:'',
                    reason:'',
                }
            },
            up_rules:{
                reason:[{
                    required:true,
                    message:'重新上架的理由不能为空',
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
                nickName:{
                    info:{
                        prop:'nickName',
                        label:'发布者昵称',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'150',
                        align:'center'
                    }
                },
                merchantName:{
                    info:{
                        prop:'merchantName',
                        label:'商家名称',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'200',
                        align:'center'
                    }
                },
                name:{
                    info:{
                        prop:'name',
                        label:'商品名称',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'200',
                        align:'center'
                    }
                },
                characteristic:{
                    info:{
                        prop:'characteristic',
                        label:'商品描述',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'500',
                        align:'center'
                    }
                },
                delivery:{
                    info:{
                        prop:'delivery',
                        label:'是否配送',
                        sortable:true,
                        formatter: (item) => {
                            if (item.delivery) {
                                return "配送"
                            }else{
                                return "不配送"
                            }
                        },
                    },
                    filter:{},
                    style:{
                        width:'120',
                        align:'center'
                    }
                },
                onlineStatus:{
                    info:{
                        prop:'onlineStatus',
                        label:'商品状态',
                        sortable:true,
                        formatter: (item) => {
                            switch(item.onlineStatus) {
                                case 1:
                                    return "上架"
                                case 2:
                                    return "下架"
                                default:
                                    return "强制下架"
                            }
                        },
                    },
                    filter:{},
                    style:{
                        width:'120',
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
                onlineStatusSelect,
                merchantNameSelect,
                productNameSelect,
                where,
                fn
            } = {}) {

            var query = this.$route.query

            this.paginations.current_page = page || parseInt(query.currentPage) || 1
            this.paginations.page_size = pageSize || parseInt(query.size) || this.paginations.page_size
            this.selectFormData.onlineStatus = onlineStatusSelect || parseInt(query.onlineStatus) || this.selectFormData.onlineStatus
            this.selectFormData.merchantName = merchantNameSelect || this.selectFormData.merchantName
            this.selectFormData.productName = productNameSelect || this.selectFormData.productName


            var data={
                onlineStatus: this.selectFormData.onlineStatus,
                currentPage: this.paginations.current_page,
                size: this.paginations.page_size,
                merchantName: this.selectFormData.merchantName,
                productName: this.selectFormData.productName
            }
            if(where){
                data = Object.assign(data, where || {})
            }

            this.$$api_product_queryProductList({
                data,
                fn: result => {
                    this.selectData = result.list
                    this.paginations.total = result.total
                    // console.log(result.map.list)
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
        //编辑按钮判断
        down(status){
            if(status == 3){
                return false
            }
            return true
        },
        up(status){
            if(status == 3){
                return true
            }
            return false
        },
        //强制下架
        downRefuse(obj){
            this.downDialog.show = true
            this.downDialog.show_info = obj
            this.downDialog.onSelectDataInfo.productId=obj.id
        },
        downRefuseReset(ref){
            this.downDialog.onSelectDataInfo.reason=""
            this.$refs[ref].resetFields()
        },
        downSubmit(){
            var data = {
                productId: this.downDialog.onSelectDataInfo.productId,
                userId: JSON.parse(window.localStorage.netx_userinfo).username,
                reason: this.downDialog.onSelectDataInfo.reason,
                onlineStatus: '3'
            }
            this.$$api_product_coercionDown({
                data,
                fn: result => {
                    this.$message('强制下架成功'),
                    this.downDialog.show = false,
                    this.getList()
                }
            })
            //调用后台的接口
        },
        //重新上架
        upRefuse(obj){
            this.upDialog.show = true
            this.upDialog.show_info = obj
            this.upDialog.onSelectDataInfo.productId=obj.id
        },
        upRefuseReset(ref){
            this.upDialog.onSelectDataInfo.reason=""
            this.$refs[ref].resetFields()
        },
        upSubmit(){
            var data = {
                productId: this.upDialog.onSelectDataInfo.productId,
                userId: JSON.parse(window.localStorage.netx_userinfo).username,
                reason: this.upDialog.onSelectDataInfo.reason,
                onlineStatus: '1'
            }
            this.$$api_product_up({
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
