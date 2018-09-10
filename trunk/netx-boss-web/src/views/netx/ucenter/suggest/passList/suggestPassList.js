export default {
    name: 'suggest-passList',
    data() {
        return {
            suggestPassList: [],
            selectData: {
                operateType: 'REFUSE',
                numCheck: ""
            },
            items: [
                {
                    id: 'REFUSE',
                    name: '不通过'
                }, {
                    id: 'ACCEPT',
                    name: '通过'
                }, {
                    id: 'WAITING',
                    name: '未审核'
                },
                {
                    id: 'SHELVE',
                    name: '搁置'
                },
                {
                    id:null,
                    name:'全部'
                }
            ],
            dialog: {
                show: false,
                show_info: "",
                info:{
                    nickName:'',
                    userNumber:'',
                    userId:'',
                    reason: "",//审批结果描述
                    createTime:"",
                    operateType: null
                }
            },
            items1: [
                {
                    id: 0,
                    name: '不通过'
                },
                {
                    id: 2,
                    name: '搁置'
                },
                {
                    id: 1,
                    name: '通过'
                }],
            dialog1: {
                show: false,
                show_info: "",
                info: {
                    userNumber:'',
                    mobile:'',
                    suggest:'',
                    reason: '',//审批结果描述
                    operateType: 1
                }
            },
            dialog2: {
                show: false,
                info: {
                    mobile:'',
                }
            },
            pwdlog:{
                show:false,
                info:{
                    password:'',
                }
            },
            // 需要给分页组件传的信息
            paginations: {
                current_page: 1,
                total: 0,
                page_size: 10,
                page_sizes: [10, 20, 30],
                layout: 'total, sizes, prev, pager, next, jumper'
            },
            fields: {
                userId: {
                    info: {
                        prop: 'userId',
                        label: '建议者id',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '300',
                        align: 'center'
                    }
                },
                id: {
                    info: {
                        prop: 'id',
                        label: '列表id',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '100',
                        align: 'center'
                    }
                },
                nickname: {
                    info: {
                        prop: 'nickname',
                        label: '用户昵称',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '150',
                        align: 'center'
                    }
                },
                result: {
                    info: {
                        prop: 'result',
                        label: '审批结果描述',
                        sortable: true,
                    },
                    filter: {},
                    style: {
                        width: '150',
                        align: 'center'
                    }
                },
                suggest: {
                    info: {
                        prop: 'suggest',
                        label: '建议描述',
                        sortable: true,
                    },
                    filter: {},
                    style: {
                        width: '200',
                        align: 'center'
                    }
                },
                userNumber: {
                    info: {
                        prop: 'userNumber',
                        label: '网号',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '200',
                        align: 'center'
                    }
                },
                isEffective: {
                    info: {
                        prop: 'isEffective',
                        label: '状态',
                        sortable: true,
                        formatter: (item) => {
                            var status1 = item.isEffective
                            console.info(status1)
                            //confirm('用户id'+status1)
                            if (status1 == undefined) {
                                return "未审核"
                            }
                            if (status1==1) {
                                return "通过"
                            }
                            if (status1==2){
                                return "搁置"
                            } else {
                                return "不通过"
                            }
                        },
                    },
                    filter: {},
                    style: {
                        width: '100',
                        align: 'center'
                    }
                },
                credit: {
                    info: {
                        prop: 'credit',
                        label: '信用',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '100',
                        align: 'center'
                    }
                },
                auditUserName: {
                    info: {
                        prop: 'auditUserName',
                        label: '审批人',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '200',
                        align: 'center'
                    }
                },
                result: {
                    info: {
                        prop: 'result',
                        label: '审批描述',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '300',
                        align: 'center'
                    }
                },
                createTime: {
                    info: {
                        prop: 'createTime',
                        label: '创建时间',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '150',
                        align: 'center'
                    }
                }

            }
        }
    },
    methods: {
        //默认查询所有数据
        onSelectAllData() {
            var data = {
                currentPage: this.paginations.current_page,
                status:this.status,
                size: this.paginations.page_size
            }
            this.$$api_user_getSuggestPassList({
                data,
                fn: result => {
                    this.suggestPassList = result.list
                    this.paginations.total = result.total
                }
            })
        },
        passwordCheck(){
            var data={
                password:this.pwdlog.info.password,
            }
            var comfirm=confirm("确定密码无误吗？");
            if(comfirm){
                this.$$api_user_passwordCheck({
                    data,
                    fn:result => {
                        this.$message('密码正确')
                        this.pwdlog.show=false
                        this.addScoreModel()
                    }
                })
             }else{
                 this.$message('操作取消')
                 this.pwdlog.show = false
                 this.getList()
             }
        },
        //页数
        addScore(){
            var data = {
                mobile:this.dialog2.info.mobile,
            }
            var comfirm = confirm("确定添加吗?")
            if (comfirm) {
                this.$$api_user_addSuggestPass({
                    data,
                    fn: result => {
                        this.$message('操作成功')
                        this.dialog2.show = false
                        this.getList()
                    }
                })
            } else {
                this.$message('操作取消')
                this.dialog2.show = false
                this.getList()
            }
        },
        onChangePageSize(pageSize) {
            this.getList({
                pageSize,
                fn: () => {
                    this.setPath('page_size', pageSize)
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
        //页数
        onChangeCurrentPage(page) {
            this.getList({
                page,
                fn: () => {
                    this.setPath('page', page)
                }
            })
        },
        addSuggest(){
            this.dialog1.show = true
        },
        addScoreModel(){
            this.dialog2.show = true
        },
        addPasswordCheck(){
            this.pwdlog.show=true
        },
        suggestPass(){
            var data = {
                userNumber: this.dialog1.info.userNumber,
                mobile:this.dialog1.info.mobile,
                suggest:this.dialog1.info.suggest,
                result: this.dialog1.info.reason,
                effective: this.dialog1.info.operateType,
                auditUserName: JSON.parse(window.localStorage.netx_userinfo).username
            }
            var comfirm = confirm("确定添加吗?")
            if (comfirm) {
                this.$$api_user_addSuggestPass({
                    data,
                    fn: result => {
                        this.$message('操作成功')
                        this.dialog1.show = false
                        this.getList()
                    }
                })
            } else {
                this.$message('操作取消')
                this.dialog1.show = false
                this.getList()
            }
        },
        //修改
        suggest() {
            var data = {
                userId: this.dialog.show_info.userId,
                result: this.dialog.info.reason,
                effective: this.dialog1.info.operateType,
                id: this.dialog.show_info.id,
                createTime:this.dialog.show_info.createTime,
                auditUserName: JSON.parse(window.localStorage.netx_userinfo).username
            }
            var comfirm = confirm("确定审批建议吗？")
            if (comfirm) {
                this.$$api_user_suggest({
                    data,
                    fn: result => {

                        // this.$refs[ref].resetFields()
                        this.$message('操作成功')
                        // location.reload()
                        this.dialog.info.reason = ""
                        this.dialog.show = false
                        this.getList()
                    }
                })
            } else {
                //this.this.dialog.info.result = ""
                // this.$refs[ref].resetFields()
                this.$message('操作取消')
                // location.reload()
                this.dialog.info.reason = ""
                this.dialog.show = false
                this.getList()
            }
        },
        //分页点击点击查询
        getList({numCheck,page, pageSize, operateType, fn} = {}) {
            if(page != undefined){
                this.paginations.current_page = page
            }
            if(pageSize != undefined){
                this.paginations.page_size = pageSize
            }
            if(operateType != undefined){
                this.selectData.operateType = operateType
            }
            //this.paginations.current_page = page || parseInt(query.currentPage) || 1
            //this.paginations.page_size = pageSize || parseInt(query.size) || this.paginations.page_size
            //this.selectData.operateType = operateType || parseInt(query.operateType) || this.selectData.operateType
            var data = {
                numCheck:this.selectData.numCheck,
                status: this.dialog.info.operateType,
                currentPage: this.paginations.current_page,
                size: this.paginations.page_size,
            }
            this.$$api_user_getSuggestPassList({
                data,
                fn: result => {
                    this.suggestPassList = result.list
                    this.paginations.total = result.total
                    // console.log(result.map.list)
                    // console.log(result.map.total)
                    fn && fn()
                }
            })
        },
        //设置路由
        setPath(field, value) {
            var path = this.$route.path
            var query = Object.assign({}, this.$route.query)

            if (typeof field === 'object') {
                query = field
            } else {
                query[field] = value
            }
        },
        //点击显示
        onEdit(passList) {
            this.dialog.show = true
            this.dialog.show_info = passList
        },
        change(){

        }
    },
    //开启时执行
    mounted () {
        this.getList({})
    }
}

