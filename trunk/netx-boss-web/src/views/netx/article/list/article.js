import {userInfo} from "os";
import {formatDate} from "element-ui/lib/date-picker";

// import { gbs } from 'config/settings.js'

export default {
    name: 'user-article',
    data() {
        return {
            articles: [],
            selectFormData: {
                title: '',
                statusCode: null,
                advertorialType: null,
                createTime: null,
                userNumber: ""
            },
            advertorialTypeItems: [
                {
                    id: null,
                    name: '全部'
                }, {
                    id: 0,
                    name: '免费图文'
                }, {
                    id: 1,
                    name: '软文【广告】'
                }
            ],
            statusCode: {
                id: 0
            },
            statusCodeItems: [
                {
                    id: null,
                    name: '全部'
                }, {
                    id: 0,
                    name: '正常'
                }, {
                    id: 1,
                    name: '异常'
                }, {
                    id: 2,
                    name: '待审核'
                }, {
                    id: 3,
                    name: '待交押金'
                }, {
                    id: 4,
                    name: '押金余额不足'
                }, {
                    id: 5,
                    name: "下架"
                }
            ],
            reason: "",
            type: "",
            article_info: {},
            whos: {
                '0': '所有人',
                '1': '全部好友',
                '2': '我关注组的人',
                '3': '关注我的人',
                '4': '指定用户'
            },
            dialog: {
                show: false
            },
            status: {
                '0': '正常',
                '1': '异常',
                '2': '待审核',
                '3': '待交押金',
                '4': '押金余额不足',
                '5': '下架',
                show: false
            },
            advertorialTypes: {
                '0': '免费图文',
                '1': '软文【广告】',
            },
            fields: [{
                type: 'image',
                key: 'pic',
                label: '图片',
                width: '200',
                height: '200'
            }, {
                key: 'title',
                label: '标题',
                width: '280'
            }, {
                key: 'who',
                label: '谁可看',
                width: '100',
                formatter: (item) => {
                    return this.whos[item.who]
                }
            }, {
                key: 'nickname',
                label: '发布者',
                width: '100'
            }, {
                key: 'hits',
                label: '点击数',
                width: '100'
            }, {
                key: 'advertorialType',
                label: '软文类型',
                width: '100',
                formatter: (item) => {
                    return this.advertorialTypes[item.advertorialType]
                },
            }, {
                key: 'amounts',
                label: '押金',
                width: '100'
            }, {
                key: 'statusCode',
                label: '状态',
                width: '100',
                formatter: (item) => {
                    return this.status[item.statusCode]
                },
                filter_list: [{
                    text: '正常',
                    value: 0
                }, {
                    text: '异常',
                    value: 1
                }, {
                    text: '待审核',
                    value: 2
                }, {
                    text: '待交押金',
                    value: 3
                }, {
                    text: '押金余额不足',
                    value: 4
                }, {
                    text: '下架',
                    value: 5
                }],
                filter_method: (value, item) => {
                    return item.statusCode === value
                },
                filter_multiple: false
            }, {
                key: 'isLock',
                label: '锁定',
                width: '80',
                formatter: (item) => {
                    return item.isLock === 1 ? '是' : '否'
                }
            }, {
                key: 'createTime',
                label: '发布时间',
                width: '200',
                formatter: (item) => {
                    var date = new Date();
                    date.setTime(item.createTime);
                    return date.getFullYear() + "-" + (date.getUTCMonth() + 1) + "-" + date.getUTCDate();
                }
            }],
            pagination: {
                current_page: 1,
                total: 30,
                page_size: 10,
                page_sizes: [10, 20, 30],
                layout: 'total, sizes, prev, pager, next, jumper'
            },
            btn_info: {
                condition: {
                    width: 300,
                    batch: false,
                    add: false,
                    select: false,
                    update: false,
                    delete: true,
                    list: [{
                        text:'上架',
                        type: 'primary',
                        fn: (opts) => {
                            this.article_info = opts.data
                            this.statusSuccess()
                        }
                    },{
                        text:'下架',
                        type:'warning',
                        fn: (opts) => {
                            this.article_info = opts.data
                            this.statusObtained()
                        }
                    },/*{
                        text: '列入异常', // 按钮文本
                        type: 'warning', // 按钮类型，遵循elementUI的几种按钮类型
                        // 如果不传按钮，默认会触发onClickBtn方法，传了则不会调用onClickBtn方法
                        fn: (opts) => {
                            this.article_info = opts.data
                            this.type = 1
                            this.dialog.show = true
                        }
                    }, {
                        text: '解除异常',
                        type: 'primary',
                        fn: (opts) => {
                            this.article_info = opts.data
                            this.type = 2
                            this.dialog.show = true
                        }
                    }, */{
                        text: '退还押金',
                        type: 'danger',
                        fn: (opts) => {
                            this.onReturnAmount(opts.data)
                        }
                    }, {
                        text: '修改状态',
                        type: 'primary',
                        fn: (opts) => {
                            this.status.show = true
                            this.article_info = opts.data
                        }
                    }]
                }
            },
            expand: {
                show: true
            }
        }
    },
    methods: {
        //正常or上架
        statusSuccess() {
            var comfirm = confirm("确定要修改吗？");
            if (comfirm) {
                this.statusCode.id = 0
                this.modifyStatus()
            }
            this.status.show = false
        },
        //列入异常
        inclusionException(){
            var comfirm = confirm("确定要修改吗？");
            if (comfirm) {
                this.type = 1
                this.dialog.show = true
            }
            this.status.show = false
        },
        //解除异常
        undoException(){
            var comfirm = confirm("确定要修改吗？");
            if (comfirm) {
                this.type = 2
                this.dialog.show = true
            }
            this.status.show = false
        },
        //待审核
        statusReview() {
            var comfirm = confirm("确定要修改吗？");
            if (comfirm) {
                this.statusCode.id = 2
                this.modifyStatus()
            }
            this.status.show = false
        },
        //下架
        statusObtained() {
            var comfirm = confirm("确定要修改吗？");
            if (comfirm) {
                this.statusCode.id = 5
                this.modifyStatus()
            }
            this.status.show = false
        },
        modifyStatus() {
            var data = {
                status: this.statusCode.id,
                articleId: this.article_info.id,
                pic: this.article_info.pic,
                length: this.article_info.length,
                reason: "9999",
            }
            this.$$api_article_modifyArticleStatus({
                data,
                fn: result => {
                    this.$message.success("修改状态成功")
                    this.getList()
                }
            })
        },

        /**
         * 改变页码和当前页时需要拼装的路径方法
         * @param {string} field 参数字段名
         * @param {string} value 参数字段值
         */
        setPath(field, value) {
            var path = this.$route.path
            var query = Object.assign({}, this.$route.query)

            if (typeof field === 'object') {
                query = field
            } else {
                query[field] = value
            }
        },
        onClickBtnDelete(row) {
            var confim = confirm("确定要删除吗")
            if (confim) {
                var data = {
                    id: row.data.id
                }
                this.$$api_article_deleteArticleByBoss({
                    data,
                    fn: detFlag => {
                        this.$message.success("删除图文成功")
                    },
                    errFn: (data) => {
                        this.$message.error("删除图文失败")
                    }
                })
            }
        },

        /**
         * 改变当前页事件
         * @param  {number} page 当前页码
         */
        onChangeCurPage(page) {
            this.getList({
                page,
                fn: () => {
                    this.setPath('page', page)
                }
            })
        },
        onSubmitForm() {
            this.getList({
                title: this.selectFormData.title,
                advertorialType: this.selectFormData.advertorialType,
                statusCode: this.selectFormData.statusCode,
                userNumber: this.selectFormData.userNumber,

                fn: () => {
                    this.setPath("title", this.selectFormData.title),
                        this.setPath("advertorialType", this.selectFormData.advertorialType),
                        this.setPath("statusCode", this.selectFormData.statusCode)
                    this.setPath("userNumber", this.selectFormData.userNumber)
                }
            })
        },
        onSubmit() {
            var confim = confirm("确定要修改吗")
            if (confim) {
                var data = {
                    articleId: this.article_info.id,
                    reason: this.reason
                }
                if (this.type == 1) {
                    if (this.article_info.statusCode == 1) {
                        this.$message("已经是异常状态")
                    } else {
                        this.$$api_article_pushException({
                            data,
                            fn: result => {
                                this.dialog.show = false
                                this.status.show = false
                                this.reason = ""
                                console.log(result)
                                this.$message.success("修改状态成功")
                                this.getList()
                            }
                        })
                    }
                } else if (this.type == 2) {
                    if (this.article_info.statusCode == 0) {
                        this.$message("该咨询不是异常状态")
                    } else {
                        this.$$api_article_popException({
                            data,
                            fn: result => {
                                this.dialog.show = false
                                this.reason = ""
                                console.log(result)
                                this.$message.success("修改状态成功")
                                this.getList()
                            }
                        })
                    }
                }
            }

        },
        onReturnAmount(obj) {
            var confim = confirm("确定要退款吗")
            if (confim) {
                console.log(obj)
                if (obj.statusCode == 0) {
                    this.$message("该咨询是正常状态")
                } else {
                    var data = {
                        id: obj.id
                    }
                    this.$$api_article_returnAmount({
                        data,
                        fn: result => {
                            this.$message('审核通过，退款成功！'),
                                this.$message.success("修改状态成功")
                                this.getList()
                        }
                    })
                }
            }
        },
        /**
         * 改变每页显示数量事件
         * @param  {number} size 当前每页显示数量
         */
        onChangeCurPageSize(pageSize) {
            this.getList({
                pageSize,
                fn: () => {
                    this.setPath('page_size', pageSize)
                }
            })
        },

        getList({
                    page,
                    pageSize,
                    fn
                } = {}) {
            var query = this.$route.query

            this.pagination.current_page = page || parseInt(query.page) || 1
            this.pagination.page_size = pageSize || parseInt(query.page_size) || this.pagination.page_size

            var data = {
                current: this.pagination.current_page,
                size: this.pagination.page_size,
                statusCode: this.selectFormData.statusCode,
                advertorialType: this.selectFormData.advertorialType,
                title: this.selectFormData.title,
                userNumber: this.selectFormData.userNumber
            }

            this.$$api_article_getUserArticleList({
                data,
                fn: result => {
                    this.articles = result.map.records
                    this.pagination.total = result.map.total
                    fn && fn()
                }
            })
        }
    },
    padLeftZero(str) {
        return ('00' + str).substr(str.length);
    },
    formatDate1(date, fmt) {
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
        }
        let o = {
            'M+': date.getMonth() + 1,
            'd+': date.getDate(),
            'h+': date.getHours(),
            'm+': date.getMinutes(),
            's+': date.getSeconds()
        };
        for (let k in o) {
            if (new RegExp(`(${k})`).test(fmt)) {
                let str = o[k] + '';
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str));
            }
        }
        return fmt;
    },
    watch: {
        '$route': 'getList'
    },
    created() {
    },
    mounted() {
        this.getList()
    },
    '$route'(to, from) {
    }
}
