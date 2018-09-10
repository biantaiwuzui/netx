
export default {
    name:'costSetting-index',
    data () {
        return {
            list:[],
            dialog:{
                show:false
            },
            showDialog:{
                show:false
            },
            fields: [{
                key: 'state',
                label: '是否审核',
                formatter: (item) => {
                    return item.state == 1 ? '已经审核' : '未审核'
                }
            },{
                key: 'sharedFee',
                label: '分成',
            },{
                key: 'withdrawFee',
                label: '提现手续费',
            },{
                key: 'shopManagerFee',
                label: '注册商家管理费',
            },{
                key: 'shopManagerFeeLimitDate',
                label: '注册商家管理费期限',
                formatter: (item) => {
                    if(item.shopManagerFeeLimitDate==0){
                        return "终身有效"
                    }else if(item.shopManagerFeeLimitDate==1){
                        return "一年"
                    }else if(item.shopManagerFeeLimitDate==3){
                        return "三年"
                    }
                }
            },{
                key: 'creditIssueFee',
                label: '网币发行费',
            },{
                key: 'creditFundsInterest',
                label: '网币竞购系数',
            },{
                key: 'creditSubscribeFee',
                label: '网币报名认购费用',
            },{
                key: 'creditInst',
                label: '网币资金利息',
            },{
                key: 'clickFee',
                label: '点击费用',
            },{
                key: 'picAndVoicePublishDeposit',
                label: '图文发布押金',
            },{
                key: 'violationClickFee',
                label: '违规图文点击费',
            },{
                key: 'wishCapitalManageFee',
                label: '心愿资金管理费',
            },{
                key: 'salerSharedFee',
                label: '销售收入分成',
            }],
            btn_info: {
                condition: {
                width: 200,
                batch: false,
                add: true,
                select: false,
                update: false,
                delete:false,
                list: [{
                    text: '审核通过', // 按钮文本
                    type: 'primary', // 按钮类型，遵循elementUI的几种按钮类型
                    // 如果不传按钮，默认会触发onClickBtn方法，传了则不会调用onClickBtn方法
                    fn: (opts) => {
                        if(opts.data.state==1){
                        this.$message("该设置已经通过审核")
                        }else{
                            if(this.$store.getters.getUserinfo.username != "superadmin"){
                                this.$message("超级管理员才能审核")
                            }else{
                                var data = {
                                    id:opts.data.id,
                                    disposeUser:"superadmin",
                                    dispose:1
                                }
                                this.$$api_common_costSettingDispose({
                                    data,
                                    fn: result => {
                                        this.$message.success("审核通过")
                                        this.getList()
                                    }
                                })
                            }
                        }
                    }
                }]
                }
            },
            addData : {
                sharedFee: 0,//分成
                withdrawFee: 0,//提现手续费
                shopManagerFee: 0,//注册商家管理费
                shopManagerFeeLimitDate: 0,//注册商家管理费有效期/0:终身有效，1一年，3三年
                creditIssueFee: 0,//网币发行费
                creditFundsInterest: 0,//网币竞购系数
                creditSubscribeFee: 0,//网币报名认购费用
                creditInst: 0,//网币资金利息
                picAndVoicePublishDeposit: 0,//图文、音视的发布押金
                clickFee: 0,//点击费用
                violationClickFee: 0,//违规图文、音视的点击费用
                wishCapitalManageFee: 0,//心愿资金管理费
                salerSharedFee: 0,//销售收入分成
            }
        }
    },
    methods:{
        getList(){ //从数据库获取表单的值
            this.$$api_common_costSettingQueryList({
                fn: result => {
                    this.list = result.result
                },
                errFn: data => {
                    this.$message('调用后台接口异常')
                }
            })
        },

        show(){
            this.showDialog.show = true
            var data = {}
            this.$$api_common_costSettingQuery({
                data,
                fn: result => {
                    this.addData = result.result
                }
            })
        },
        closeShow(){
            this.showDialog.show = false
        },
        onClickBtnAdd(){
            this.dialog.show = true
        },
        add(){
            if(this.$store.getters.getUserinfo.username != "superadmin"){
                var data = this.addData
                this.$$api_common_costSettingSave({
                    data,
                    fn: result => {
                        this.$message.success("添加成功")
                        this.getList()
                    }
                })
            }else{
                var data = this.addData
                this.$$api_common_costSettingSaveSuper({
                    data,
                    fn: result => {
                        this.$message.success("添加成功")
                        this.getList()
                    }
                })
            }
            this.dialog.show = false
        }
        // submitForm(
        //     {
        //         id,
        //         dispose,
        //         disposeUser
        //         }={}){//提交表单
        //     var data = {
        //         id: id || this.selectDataField.id,
        //         dispose: dispose === 1 ? 1 : 0 ,               //0不同意,1同意
        //         disposeUser: disposeUser || ''  //如果都没有值的话就是空字符串
        //     }
        //     this.$$api_common_costSettingDispose({
        //         data,
        //         fn: result => {
        //             if(result.apiCode === 1200){
        //                 this.$message('审批成功')
        //             }
        //         },
        //         errFn: result => {
        //             this.$message('审批异常'+result.msg)
        //         }
        //     })

        // },
        // save(){           //保存操作
        //     var data= {
        //         id: this.selectData.id,
        //         sharedFee: this.selectData.sharedFee,
        //         withdrawFee: this.selectData.withdrawFee,
        //         shopManagerFee: this.selectData.shopManagerFee,
        //         shopManagerFeeLimitDate: this.selectData.shopManagerFeeLimitDate,
        //         currencyInst: this.selectData.currencyInst,
        //         currencyIssueFee: this.selectData.currencyIssueFee,
        //         currencyFundsInterest: this.selectData.currencyFundsInterest,
        //         currencySubscribeFee: this.selectData.currencySubscribeFee,
        //         clickFee: this.selectData.clickFee,
        //         violationClickFee: this.selectData.violationClickFee,
        //         picAndVoicePublishDeposit: this.selectData.picAndVoicePublishDeposit,
        //         wishCapitalManageFee: this.selectData.wishCapitalManageFee,
        //         salersharedFee: this.selectData.salersharedFee
        //     }
        //     this.$$api_common_costSettingSave({
        //         data,
        //         fn: result => {
        //             if(result.apiCode === 1200){
        //                 this.$message('新增成功')
        //             }
        //         },
        //         errFn: result => {
        //             this.$message('新增失败')
        //         }
        //     })
        // }
    },
    watch : {
        '$route' : 'getList'
    },
    created () {
    },
    mounted () {
      this.getList()
    },
    '$route' (to, from) {
    }
}