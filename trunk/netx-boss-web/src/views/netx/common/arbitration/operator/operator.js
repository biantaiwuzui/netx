
export default {
    name:'arbitration-operator',
    data() {
        return {
            dataInfo:[],
            templateData:'',
            fromSrcUrl:[],
            fromSrcUrlSize:0,
            appealSrcUrl:[],
            appealSrcUrlSize:0,
            dialogInfo:{
                show:false
            },
            onSelectData:{
                opUserId:'',
                CreditRefund:2,
                UserCreditPointValue:0,
                UserCreditPointReason:'',
                type:0,
                id:'',
                refundRadioButton:false,
                refundArbitrateReason:'',
                returnRadioButton:false,
                returnArbitrateReason:'',
                subReleaseFrozenMoneyRefund:0,
                subReleaseReleaseFrozenMoneyReason:''
            },
            rules_form:{
                UserCreditPointValue:[{
                    required:true,
                    message:'信用值不能为空',
                    trigger: 'blur'
                }],
                UserCreditPointReason:[{
                    required:true,
                    message: '（不）扣信用值的理由不能为空',
                    trigger:'change'
                }],
                refundArbitrateReason:[{
                    required:true,
                    message: '退（不）退款的理由不能为空',
                    trigger:'change'
                }],
                returnArbitrateReason:[{
                    required:true,
                    message: '退（不）退货的理由不能为空',
                    trigger:'change'
                }],
                subReleaseReleaseFrozenMoneyReason:[{
                    required:true,
                    message: '活动（不）撤销的理由不能为空',
                    trigger:'change'
                }]
            }
        }
    },methods:{
        boolIsEmpty(obj){
            if(obj == null){
                return true
            }
            if(obj.length === 0 ){
                return true
            }
            return false
        },
        getSplitString(objs){
            if( ! this.boolIsEmpty(objs) ){
                return objs.split(",")
            }
            return null
        },
        openDialog(){
          this.dialogInfo.show = true
        },
        onSubmit(refForm){
            var ref = this.$refs[refForm]
            ref.validate((valid) => {
                if (valid) {
                    var data = {
                        type: this.dataInfo.type,
                        id: this.dataInfo.id,
                        opUserId: 'string',
                        fromUserCreditRefund: this.onSelectData.CreditRefund === 0 ? 1 : 0,
                        fromUserCreditPoint: this.onSelectData.UserCreditPointValue,
                        fromUserCreditPointReason: this.onSelectData.UserCreditPointReason,
                        toUserCreditRefund: this.onSelectData.CreditRefund === 1 ? 1 : 0,
                        toUserCreditPoint: this.onSelectData.UserCreditPointValue,
                        toUserCreditPointReason: this.onSelectData.UserCreditPointReason,
                        refundRadioButton: this.onSelectData.refundRadioButton,
                        refundArbitrateReason: this.onSelectData.refundArbitrateReason,
                        returnRadioButton: this.onSelectData.returnRadioButton,
                        returnArbitrateReason: this.onSelectData.returnArbitrateReason,
                        substractReleaseFrozenMoneyRefund: this.onSelectData.subReleaseFrozenMoneyRefund,
                        substractReleaseFrozenMoneyReason: this.onSelectData.subReleaseReleaseFrozenMoneyReason
                    }

                    this.$$api_common_acceptHandle({
                        data,
                        fn: result => {
                            this.$message('受理成功');
                            this.$router.push({
                                path:'/netx/common/arbitrationList'
                            })

                        },
                        errFn: data => {
                            this.$message('错误信息')
                        }
                    })

                }
            })

        }
        ,
        reset_form (form) {
            this.$refs[form].resetFields()
        }
    },
    watch:{
        UserCreditPointValue:function (val) {
            if(!/^([1-9]\d*|0)(\.\d{1,2})$/.test(val)){
                this.$message('信用值只能大于0的整数或者小数');
            }
        }
    },
    created (){},
    mounted () {
        this.dateInfo=''
        this.dataInfo = this.$route.query.obj
        this.templateData = this.getSplitString(this.dataInfo.fromSrcUrl)
        for(let i in this.templateData){
            this.fromSrcUrl.push({
                index:this.fromSrcUrlSize++,
                url:this.templateData[i]
            })
        }
        this.templateData = this.getSplitString(this.dataInfo.appealSrcUrl)
        for(let i in this.templateData){
            this.appealSrcUrl.push({
                index:this.appealSrcUrlSize++,
                url:this.templateData[i]
            })
        }

        // for(let a in this.getSplitString(this.dataInfo['fromSrcUrl'])){
        //     this.fromSrcUrl.push({url:a})
        //     window.alert(a)
        // }
        // for(let a in this.getSplitString(this.dataInfo['appealSrcUrl'])){
        //     this.appealSrcUrl.push({url:a})
        //     window.alert(a)
        //
    }
}