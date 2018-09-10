export default {
    name: 'user-verify',
    data () {
        return {
            verifyList: [],
            selectData: {
                nickname: "",
                userNumber: "",
                mobile: "",
                status: 0,
                verifyType: null
            },
            statusItems:[
                {
                    id:null,
                    name:'全部'
                },{
                    id:0,
                    name:'待审核'
                },{
                    id:1,
                    name:'已通过'
                },{
                    id:2,
                    name:'未通过'
                }
            ],
            verifyTypeItems:[
                {
                    id:null,
                    name:'全部'
                },{
                    id:1,
                    name:'身份认证'
                },{
                    id:2,
                    name:'视频认证'
                },{
                    id:3,
                    name:'车辆认证'
                },{
                    id:4,
                    name:'房产认证'
                },{
                    id:5,
                    name:'学历认证'
                }
            ],
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
                        label: '用户ID',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        align: 'center'
                    }
                },
                nickName: {
                    info: {
                        prop: 'nickName',
                        label: '用户昵称',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '160',
                        align: 'center'
                    }
                },
                lv: {
                    info: {
                        prop: 'lv',
                        label: '等级',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '100',
                        align: 'center'
                    }
                },
                sex: {
                    info: {
                        prop: 'sex',
                        label: '性别',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '100',
                        align: 'center'
                    }
                },
                age: {
                    info: {
                        prop: 'age',
                        label: '年龄',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '100',
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
                        width: '130',
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
                        width: '120',
                        align: 'center'
                    }
                },
                mobile: {
                    info: {
                        prop: 'mobile',
                        label: '手机号码',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '130',
                        align: 'center'
                    }
                },
                reason: {
                    info: {
                        prop: 'reason',
                        label: '不通过原因',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '300',
                        align: 'center'
                    }
                },
                admin: {
                    info: {
                        prop: 'admin',
                        label: '处理人',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '120',
                        align: 'center'
                    }
                },
                status: {
                    info: {
                        prop: 'status',
                        label: '审核状态',
                        sortable: true,
                        formatter: (item) => {
                            switch(item.status) {
                                case 0:
                                    return "待审核"
                                case 1:
                                    return "已通过"
                                default:
                                    return "未通过"
                            }
                        },
                    },
                    filter: {},
                    style: {
                        width: '120',
                        align: 'center'
                    }
                },
                verifyType: {
                    info: {
                        prop: 'verifyType',
                        label: '认证类型',
                        sortable: true,
                        formatter: (item) => {
                            switch(item.verifyType) {
                                case 1:
                                    return "身份认证"
                                case 2:
                                    return "视频认证"
                                case 3:
                                    return "车辆认证"
                                case 4:
                                    return "房产认证"
                                default:
                                    return "学历认证"
                            }
                        },
                    },
                    filter: {},
                    style: {
                        width: '120',
                        align: 'center'
                    }
                },

            }
        }
    },
    methods: {

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

        /**
         * 获取列表
         * @param  {number} options.page      当前页码，切换页码时用
         * @param  {number} options.page_size 每页显示数量，改变每页数量时用
         * @param  {function} options.fn                            } 获取列表后的回调函数
         */
        getList ({
                     page,
                     pageSize,
                     nicknameSelect,
                     mobileSelect,
                     userNumberSelect,
                 }) {
            var query = this.$route.query

            this.paginations.current_page = page || parseInt(query.currentPage) || 1
            this.paginations.page_size = pageSize || parseInt(query.size) || this.paginations.page_size
            this.selectData.nickname = nicknameSelect || this.selectData.nickname
            this.selectData.mobile = mobileSelect || this.selectData.mobile
            this.selectData.userNumber = userNumberSelect || this.selectData.userNumber

            var data = {
                currentPage: this.paginations.current_page,
                size: this.paginations.page_size,
                nickName:this.selectData.nickname,
                mobile:this.selectData.mobile,
                userNumber:this.selectData.userNumber,
                status:this.selectData.status,
                verifyType:this.selectData.verifyType
            }
            this.$$api_user_selectUserVerifyList({
                data,
                fn: result => {
                    if (result.list==null) {
                        this.verifyList=null
                    }
                    this.verifyList = result.list.list
                    this.paginations.total = result.list.total
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
        onEditUser(user){
            this.$router.push({
                path:"/netx/ucenter/verifyResource",
                query:{
                    userId:user.userId,
                }
            })
        },
        onSelectData () {
            this.getList({
                nicknameSelect:this.selectData.nickname,
                mobileSelect:this.selectData.mobile,
                userNumberSelect:this.selectData.userNumber,
                statusSelect:this.selectData.status,
                fn: () => {
                    this.setPath('nickname', this.selectData.nickname),
                    this.setPath('mobile', this.selectData.mobile),
                    this.setPath('userNumber', this.selectData.userNumber),
                    this.setPath('status', this.selectData.status)
                    this.setPath('verifyType', this.selectData.verifyType)
                }
            })
        },
    },
    mounted () {
        this.getList({
        })
    },
}
