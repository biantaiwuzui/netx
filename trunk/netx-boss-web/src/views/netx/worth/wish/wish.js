
export default {
    name:'wish-list', //心愿提现列表
    data () {
        return {
            selectData:[],
            selectFormData:{
                userNumber:'',
                status:null
            },
            statusItems:[
                {
                    id:null,
                    name:'全部'
                },{
                    id:1,
                    name:'待提现'
                },{
                    id:2,
                    name:'提现成功'
                },{
                    id:3,
                    name:'提现失败'
                }
            ],
            paginations:{
                current_page:0,
                total:0,
                page_size:10,
                page_sizes:[10,20,30],
                layout:'total, sizes, prev, pager, next, jumper'
            },
            successDialog:{
                show:false,
                show_info:"",
                onSelectDataInfo:{
                    id:'',
                    wishApplyId:'',
                }
            },
            failDialog:{
                show:false,
                show_info:"",
                onSelectDataInfo:{
                    id:'',
                    wishApplyId:'',
                    reason:'',
                }
            },
            fail_rules:{
                reason:[{
                    required:true,
                    message:'提现失败的原因不能为空',
                    trigger: 'change'
                }]
            },
            //样式配置
            fields:{
                id:{
                    info:{
                        prop:'id',
                        label:'心愿历史表ID',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'300',
                        align:'center'
                    }
                },
                wishApplyId:{
                    info:{
                        prop:'wishApplyId',
                        label:'心愿使用表ID',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'300',
                        align:'center'
                    }
                },
                userNumber:{
                    info:{
                        prop:'userNumber',
                        label:'用户网号',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'150',
                        align:'center'
                    }
                },
                nickname:{
                    info:{
                        prop:'nickname',
                        label:'用户昵称',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'120',
                        align:'center'
                    }
                },
                accountName:{
                    info:{
                        prop:'accountName',
                        label:'开户人',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'120',
                        align:'center'
                    }
                },
                depositBank:{
                    info:{
                        prop:'depositBank',
                        label:'开户银行',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'150',
                        align:'center'
                    }
                },
                account:{
                    info:{
                        prop:'account',
                        label:'银行卡号',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'260',
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
                amount:{
                    info:{
                        prop:'amount',
                        label:'提现金额',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'120',
                        align:'center'
                    }
                },

                status:{
                    info:{
                        prop:'status',
                        label:'提现状态',
                        sortable:true,
                        formatter: (item) => {
                            switch(item.status) {
                                case 1:
                                    return "待提现"
                                case 2:
                                    return "提现成功"
                                default:
                                    return "提现失败"
                            }
                        },
                    },
                    filter:{},
                    style:{
                        width:'120',
                        align:'center'
                    }
                },
                updateTime:{
                    info:{
                        prop:'updateTime',
                        label:'更新时间',
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
                    statusSelect,
                    userNumberSelect,
                    where,
                    fn
                } = {}) {

            var query = this.$route.query

            this.paginations.current_page = page || parseInt(query.currentPage) || 1
            this.paginations.page_size = pageSize || parseInt(query.size) || this.paginations.page_size
            this.selectFormData.status = statusSelect || parseInt(query.status) || this.selectFormData.status
            this.selectFormData.userNumber = userNumberSelect || this.selectFormData.userNumber


            var data={
                status: this.selectFormData.status,
                currentPage: this.paginations.current_page,
                size: this.paginations.page_size,
                userNumber: this.selectFormData.userNumber,
            }
            if(where){
                data = Object.assign(data, where || {})
            }

            this.$$api_worth_queryWishList({
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
                statusSelect:this.selectFormData.status,
                fn:() => {
                    this.setPath("status",this.selectFormData.status)
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
        //提现成功
        successRefuse(obj){
            this.successDialog.show = true
            this.successDialog.show_info = obj
            this.successDialog.onSelectDataInfo.id = obj.id
            this.successDialog.onSelectDataInfo.wishApplyId = obj.wishApplyId
        },
        successRefuseReset(){
            this.successDialog.show = false
        },
        successSubmit(){
            var data = {
                id: this.successDialog.onSelectDataInfo.id,
                userId: JSON.parse(window.localStorage.netx_userinfo).username,
                wishApplyId: this.successDialog.onSelectDataInfo.wishApplyId
            }
            this.$$api_worth_success({
                data,
                fn: result => {
                    this.$message('操作成功'),
                    this.successDialog.show = false,
                    this.getList()
                }
            })
            //调用后台的接口
        },
        //提现失败
        failRefuse(obj){
            this.failDialog.show = true
            this.failDialog.show_info = obj
            this.failDialog.onSelectDataInfo.id = obj.id
            this.failDialog.onSelectDataInfo.wishApplyId = obj.wishApplyId
        },
        failRefuseReset(ref){
            this.failDialog.onSelectDataInfo.reason=""
            this.$refs[ref].resetFields()
        },
        failSubmit(){
            var data = {
                id: this.failDialog.onSelectDataInfo.id,
                userId: JSON.parse(window.localStorage.netx_userinfo).username,
                wishApplyId: this.failDialog.onSelectDataInfo.wishApplyId,
                reason: this.failDialog.onSelectDataInfo.reason
            }
            this.$$api_worth_fail({
                data,
                fn: result => {
                    this.$message('操作成功'),
                    this.failDialog.show = false,
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
