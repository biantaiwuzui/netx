
export default {
    name:'common-arbitration-list', //通用的仲裁管理
    data () {
        return {
            selectData:[],
            selectFormData:{
                nickname:'',
                type:null,
                statusCode:null
            },
            statusCodeItems:[
                {
                    id:null,
                    name:'全部'
                },{
                    id:1,
                    name:'未受理'
                },{
                    id:2,
                    name:'已受理'
                },{
                    id:3,
                    name:'已申诉'
                },{
                    id:4,
                    name:'已裁决'
                },{
                    id:5,
                    name:'拒绝受理'
                }
            ],
            typeItems:[
                {
                    id:null,
                    name:'全部投诉'
                },{
                    id:1,
                    name:'订单仲裁'
                },{
                    id:2,
                    name:'其他（网号投诉）'
                },{
                    id:3,
                    name:'需求投诉'
                },{
                    id:4,
                    name:'心愿投诉'
                },{ 
                    id:5,
                    name:'技能投诉'
                },{
                    id:6,
                    name:'活动投诉'
                }
            ],
            paginations:{
                current_page:0,
                total:1,
                page_size:10,
                page_sizes:[10, 20, 30],
                layout:'total, sizes, prev, pager, next, jumper'
            }
            ,
            refuseDialog:{
                show:false,
                show_info:"",
                onSelectDataInfo:{
                    arbitrationId:'',
                    reason:'',
                }
            },
            refuse_rules:{
                reason:[{
                    required:true,
                    message:'拒绝的理由不能为空',
                    trigger: 'change'
                }],
                id:[{
                    required:true,
                    message:'仲裁id不能为空',
                    trigger: 'blur'
                }]
            }
            ,
            total:0,
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
                fromNickname:{
                    info:{
                        prop:'fromNickname',
                        label:'投诉人昵称',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'160',
                        align:'center'
                    }
                },
                toNickname:{
                    info:{
                        prop:'toNickname',
                        label:'被投诉人昵称',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'160',
                        align:'center'
                    }
                },
                statusCode:{
                    info:{
                        prop:'statusCode',
                        label:'处理进度',
                        sortable:true,
                        formatter: (item) => {
                            switch (item.statusCode){
                                case 1:
                                    return "未受理"
                                case 2:
                                    return "已受理"
                                case 3:
                                    return "已申诉"
                                case 4:
                                    return "已裁决"
                                case 5:
                                    return "拒绝受理"
                            }
                        },
                    },
                    filter:{},
                    style:{
                        width:'120',
                        align:'center'
                    }
                },
                typeName:{
                    info:{
                        prop:'type',
                        label:'投诉类型',
                        sortable:true,
                        formatter: (item) => {
                            switch (item.type){
                                case 1:
                                    return "订单仲裁"
                                case 3:
                                    return "需求投诉"
                                case 4:
                                    return "心愿投诉"
                                case 5:
                                    return "技能投诉"
                                case 6:
                                    return "活动投诉"
                                default:
                                    return "其他（网号投诉）"
                            }
                        },
                    },
                    filter:{},
                    style:{
                        width:'160',
                        align:'center'
                    }
                },
                typeId:{
                    info:{
                        prop:'typeId',
                        label:'事件id',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'300',
                        align:'center'
                    }
                },
                theme:{
                    info:{
                        prop:'theme',
                        label:'主题',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'330',
                        align:'center'
                    }
                },
                reason:{
                    info:{
                        prop:'reason',
                        label:'原因',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'330',
                        align:'center'
                    }
                },
                fromUserId:{
                    info:{
                        prop:'fromUserId',
                        label:'投诉者用户id',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'100',
                        align:'center'
                    }
                },
                toUserId:{
                    info:{
                        prop:'toUserId',
                        label:'被投诉者用户id',
                        sortable:true
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
                typeSelect,
                statusCodeSelect,
                where,
                fn
            } = {}) {

            var query = this.$route.query

            this.paginations.current_page = page || parseInt(query.currentPage) || 1
            this.paginations.page_size = pageSize || parseInt(query.size) || this.paginations.page_size
            this.selectFormData.type = typeSelect || parseInt(query.type) || this.selectFormData.type
            this.selectFormData.statusCode = statusCodeSelect || parseInt(query.statusCode) || this.selectFormData.statusCode



            var data={
                type: this.selectFormData.type,
                current: this.paginations.current_page,
                size: this.paginations.page_size,
                statusCode: this.selectFormData.statusCode
            }
            if(where){
                data = Object.assign(data, where || {})
            }

            this.$$api_common_getArbitrationList({
                data,
                fn: result => {
                    this.selectData = result.list
                    this.total = result.count
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
        onSubmitForm(){
            this.getList({
                typeSelect:this.selectFormData.type,
                statusCodeSelect:this.selectFormData.statusCode,
                fn:() => {
                    this.setPath("type",this.selectFormData.type)
                    this.setPath("statusCode", this.selectFormData.statusCode)
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
            return date.getFullYear() + "-" + date.getUTCMonth() + "-" + date.getUTCDate();
        },
        onAccept(obj){
            this.$router.push({
                path:'/netx/common/arbitrationOperator',
                query:{
                    obj:obj
                }
            })
        },
        onRefuse(obj){
            this.refuseDialog.show = true
            this.refuseDialog.show_info = obj
            this.refuseDialog.onSelectDataInfo.arbitrationId=obj.id
            console.log(obj.id)
        },
        onRefuseReset(ref){
            this.refuseDialog.onSelectDataInfo.reason=""
            this.$refs[ref].resetFields()
        },
        refuseSubmit(){ // 拒绝受理的提交 接口
            var data = {
                id: this.refuseDialog.show_info.id,
                opUserId: JSON.parse(window.localStorage.netx_userinfo).username,
                descriptions: this.refuseDialog.onSelectDataInfo.reason
            }
            this.$$api_common_refuseAcceptHandle({
                data,
                fn: result => {
                    this.$message('成功'),
                    this.refuseDialog.show = false,
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
