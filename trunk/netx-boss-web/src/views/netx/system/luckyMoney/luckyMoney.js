export default {
    name: 'user-luckyMoney',
    data () {
        return {
            luckyMoney: [],
            selectFormData: 1,
            status: 1,
            examineStatus: "",
            ids: [],
            typeItems:[
            {
                id:1,
                name:'使用中'
            },{
                id:2,
                name:'等待审核'
            },{
                id:3,
                name:'明天生效'
            }
            ],
            fields: {
                id: {
                    info: {
                        prop: 'id',
                        label: 'ID',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        align: 'center'
                    }
                },
                sendTime: {
                    info: {
                        prop: 'sendTime',
                        label: '发放时间',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '160',
                        align: 'center'
                    }
                },
                sendPeople: {
                    info: {
                        prop: 'sendPeople',
                        label: '人数比例',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '160',
                        align: 'center'
                    }
                },
                sendCount: {
                    info: {
                        prop: 'sendCount',
                        label: '发放比例',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '160',
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
                        width: '160',
                        align: 'center'
                    }
                },
                status: {
                    info: {
                        prop: 'status',
                        label: '状态',
                        sortable: true
                    },
                    filter: {},
                    style: {
                        width: '160',
                        align: 'center'
                    }
                }
            }
        }
    },
    methods: {
        onSubmitForm() {
            this.status = this.selectFormData
            this.getList()
        },
        getList () {
            var data = {
                status: this.selectFormData,
                time: ""
            }
            this.$$api_system_query({
                data,
                fn: result => {
                    this.luckyMoney = result.list
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
        timeFormat:function(row, column) {
            var longTypeDate = row[column.property];
            if (longTypeDate == undefined) {
                return "";
            }
            var time = new Date();
            time.setTime(longTypeDate);
            return time.getHours()+":"+time.getMinutes()+":"+time.getSeconds();
        },
        stautsFormat:function(row, column) {
            var status = row[column.property];
            if (status == 1) {
                return "使用中"
            }
            if (status == 2) {
                return "等待审核"
            }
            if (status == 3) {
                return "明天生效"
            }
        },
        agree () {
            this.examineStatus = 1   
            var confim = confirm("确定要通过审核吗？审核通过后将为明日生效")
            if(confim){
                this.examineRedpacket()
            }   
        },
        disagree () {
            this.examineStatus = 0    
            var confim = confirm("确定要拒绝通过吗？")
            if(confim){
                this.examineRedpacket()
            }   
        },
        examineRedpacket () {
                this.getIds()
                var data = {
                    examineUserId: this.$store.getters.getUserinfo.username,                    
                    ids: this.ids,
                    status: this.examineStatus
                }
                this.$$api_system_examineRedpacket({
                    data,
                    fn: detFlag => {
                        this.$message.success("操作成功")
                    },
                    errFn:(data) => {
                        this.$message.error("操作失败")
                    }
                })
        },
        getIds () {
            if (this.luckyMoney !== null) {
                for (var i=0;i<this.luckyMoney.length;i++) {
                    this.ids.push(this.luckyMoney[i].id)
                }
            }
        },
        update () {
            var confim = confirm("确定要立刻生效吗？生效后将替换成使用中")
            if(confim){
                this.$$api_system_updateLuckMoneySet({
                    fn: detFlag => {
                        this.$message.success("操作成功")
                    },
                    errFn:(data) => {
                        this.$message.error("操作失败")
                    }
                })
            }   
        },
        add () {
            this.$router.push({
                path:"/netx/system/addLuckyMoney"
            })
        },
    },
    mounted () {
        this.getList({
        })
    },
}
