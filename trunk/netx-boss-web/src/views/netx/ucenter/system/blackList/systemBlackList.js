export default{
    name:'system-blackList',
    data (){
        return{
            userInfo:JSON.parse(window.localStorage.netx_userinfo),
            systemBlackList:[],
            selectData:{
                userNumber:'',
                operateType:1
            },
            items:[
                {
                id:0,
                name:'白名单'
                },{
                id:1,
                name:'黑名单'
            }],
            dialog:{
                show:false,
                show_info:"",
                info:{
                    reason:'',//释放的原因
                    operateType:0
                }
            }
            ,
            // 需要给分页组件传的信息
            paginations: {
                current_page: 1,
                total: 0,
                page_size: 10,
                page_sizes: [10, 20, 30],
                layout: 'total, sizes, prev, pager, next, jumper'
            },
            fields:{
                id:{
                    info:{
                        prop:'id',
                        label:'用户id',
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
                        label:'用户昵称',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'150',
                        align:'center'
                    }
                },
                lv:{
                    info:{
                        prop:'lv',
                        label:'等级',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'100',
                        align:'center'
                    }
                },
                sex:{
                    info:{
                        prop:'sex',
                        label:'性别',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'100',
                        align:'center'
                    }
                },
                age:{
                    info:{
                        prop:'age',
                        label:'年龄',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'100',
                        align:'center'
                    }
                },
                credit:{
                    info:{
                        prop:'credit',
                        label:'信用',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'100',
                        align:'center'
                    }
                },
                userNumber:{
                    info:{
                        prop:'userNumber',
                        label:'网号',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'150',
                        align:'center'
                    }
                },
                reason:{
                    info:{
                        prop:'reason',
                        label:'被拉黑的原因',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'300',
                        align:'center'
                    }
                },
                operateUserNickname:{
                    info:{
                        prop:'operateUserNickname',
                        label:'管理员昵称',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'150',
                        align:'center'
                    }
                }
            }
        }
    },
    methods:{
        // onSelectData(){
        //     var data = {
        //         userNumber: this.selectData.userNumber,
        //         operateType: this.selectData.operateType,
        //         currentPage: this.paginations.current_page,
        //         size: this.paginations.page_size
        //     }
        //     this.$$api_user_getSystemBlackList({
        //         data,
        //         fn: result => {
        //             this.systemBlackList = result.data.userList,
        //             this.paginations.total = result.data.count
        //         }
        //     })
        // },
        getList({page, pageSize, operateType, fn} = {}) {
            this.paginations.current_page = page != undefined?page:1
            this.paginations.page_size = pageSize != undefined?pageSize:10
            if(operateType != undefined) {
                this.selectData.operateType = operateType
            }
            //this.paginations.current_page = page || parseInt(query.currentPage) || 1
            //this.paginations.page_size = pageSize || parseInt(query.size) || this.paginations.page_size
            //this.selectData.operateType = operateType || parseInt(query.operateType) || this.selectData.operateType
            var data = {
                userNumber: this.selectData.userNumber,
                operateType: this.selectData.operateType,
                currentPage: this.paginations.current_page,
                size: this.paginations.page_size,
            }
            this.$$api_user_getSystemBlackList({
                data,
                fn: result => {
                        this.systemBlackList = result.data.userList,
                        this.paginations.total = result.data.count
                    // console.log(result.map.list)
                    // console.log(result.map.total)
                    fn && fn()
                }
            })
        },

        /**
         * 格式化状态
         */

        /**
         * 改变页码和当前页时需要拼装的路径方法
         * @param {string} field 参数字段名
         * @param {string} value 参数字段值
         */
        setPath (field, value) {
            var path = this.$route.path
            var query = Object.assign({}, this.$route.query)

            if (typeof field === 'object') {
                query = field
            } else {
                query[field] = value
            }
        },

        /**
         * 改变当前页事件
         * @param  {number} page 当前页码
         */
        onChangeCurrentPage (page) {
            this.getList({
                page,
                fn: () => {
                    this.setPath('page', page)
                }
            })
        },

        /**
         * 改变每页显示数量事件
         * @param  {number} size 当前每页显示数量
         */
        onChangePageSize (pageSize) {
            this.getList({
                pageSize,
                fn: () => {
                    this.setPath('page_size', pageSize)
                }
            })
        },
        onEdit(blacklist){
            this.dialog.show=true
            this.dialog.show_info = blacklist
        }
        ,
        operatorBlacklist (){
            var data={
                userId:this.dialog.show_info.id,
                createUserName:this.userInfo.username,
                reason:this.dialog.info.reason,
                operateType:this.dialog.info.operateType
            }
            var comfirm = confirm("确定要添加该用户到黑白名单吗？")
            if(comfirm) {
                this.$$api_user_operateSystemBlacklist({
                    data,
                    fn: () => {
                        this.$message('操作成功')
                    }
                })
            }else{
                this.$message('操作取消')
            }
        }
    },
    mounted () {
        this.getList({
        })
    },
}