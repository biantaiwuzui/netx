// import { gbs } from 'config/settings.js'

export default {
  name: 'system-otherSet',
  data () {
    return {
        list : [],
        fields: [{
                key: 'canUse',
                label: '是否审核',
                formatter: (item) => {
                    return item.canUse == 1 ? '是' : '否'
                }
            },{
                key: 'skillLimitType',
                label: '发布技能限制类别',
                formatter: (item) => {
                    return item.skillLimitType == 0 ? '人员名单' : '限制条件'
                }
            },{
                key: 'picLimitType',
                label: '发布图文限制类别',
                formatter: (item) => {
                    return item.picLimitType == 0 ? '人员名单' : '限制条件'
                }
            },{
                key: 'wishLimitType',
                label: '发布心愿限制类别',
                formatter: (item) => {
                    return item.wishLimitType == 0 ? '人员名单' : '限制条件'
                }
            },{
                key: 'regMerchantLimitType',
                label: '注册商家限制类型',
                formatter: (item) => {
                    return item.regMerchantLimitType == 0 ? '人员名单' : '限制条件'
                }
            },{
                key: 'creditLimitType',
                label: '发行网币限制类型',
                formatter: (item) => {
                    return item.creditLimitType == 0 ? '人员名单' : '限制条件'
                }
            },{
                key: 'shareLimitType',
                label: '赠送礼物限制类别',
                formatter: (item) => {
                    return item.shareLimitType == 0 ? '人员名单' : '限制条件'
                }
            }],
        btn_info: {
            condition: {
            width: 200,
            batch: false,
            add: true,
            select: true,
            update: false,
            delete:true,
            list: [{
                text: '审核通过', // 按钮文本
                type: 'primary', // 按钮类型，遵循elementUI的几种按钮类型
                // 如果不传按钮，默认会触发onClickBtn方法，传了则不会调用onClickBtn方法
                fn: (opts) => {
                    if(opts.data.canUse==1){
                    this.$message("该设置已经通过审核")
                    }
                    if(this.$store.getters.getUserinfo.username != "superadmin"){
                        this.$message("超级管理员才能审核")
                    }else{
                        var data = {
                            id:opts.data.id,
                            disposeUserId:"superadmin",
                            canUse:1
                        }
                        this.$$api_system_dispose({
                            data,
                            fn: result => {
                                this.$message.success("审核通过")
                                this.getList()
                            }
                        })
                    }
                }
            }]
            }
        },
        row:{},
        dialog:{
            show:false
        },
        skillDialog:{
            show:false
        },
        picDialog:{
            show:false
        },
        wishDialog:{
            show:false
        },
        regMerchantDialog:{
            show:false
        },
        creditDialog:{
            show:false
        },
        shareDialog:{
            show:false
        },
        skillLimitCondition:[],
        picLimitCondition:[],
        wishLimitCondition:[],
        regMerchantLimitCondition:[],
        creditLimitCondition:[],
        shareLimitCondition:[],

        skillLimitPoint:"",
        picLimitPoint:"",
        wishLimitPoint:"",
        regMerchantLimitPoint:"",
        creditLimitPoint:"",
        creditLimitMoreThen:"",
        creditLimitBalance:"",
        shareLimitPoint:"",

    }
  },
  methods: {

    skillSubmit(){
        this.skillDialog.show = false
    },
    picSubmit(){
        this.picDialog.show = false
    },
    wishSubmit(){
        this.wishDialog.show = false
    },
    regMerchantSubmit(){
        this.regMerchantDialog.show = false
    },
    creditSubmit(){
        this.creditDialog.show = false
    },
    shareSubmit(){
        this.shareDialog.show = false
    },

    showSkillLimitCondition(){
        if(!this.row.skillLimitCondition){
            this.$message("发布技能没有限制条件")
        }else{
            this.skillLimitCondition=this.row.skillLimitCondition.split(",")
            this.skillLimitPoint=this.row.skillLimitPoint
            this.skillDialog.show = true
        }
    },
    showPicLimitCondition(){
        if(!this.row.picLimitCondition){
            this.$message("发布图文咨询没有限制条件")
        }else{
            this.picLimitCondition=this.row.picLimitCondition.split(",")
            this.picLimitPoint=this.row.picLimitPoint
            this.picDialog.show = true
        }
    },
    showWishLimitCondition(){
        if(!this.row.wishLimitCondition){
            this.$message("发布心愿没有限制条件")
        }else{
            this.wishLimitCondition=this.row.wishLimitCondition.split(",")
            this.wishLimitPoint=this.row.wishLimitPoint
            this.wishDialog.show = true
        }
    },
    showRegMerchantLimitCondition(){
        if(!this.row.regMerchantLimitCondition){
            this.$message("注册商家没有限制条件")
        }else{
            this.regMerchantLimitCondition=this.row.regMerchantLimitCondition.split(",")
            this.regMerchantLimitPoint=this.row.regMerchantLimitPoint
            this.regMerchantDialog.show = true
        }
    },
    showCreditLimitCondition(){
        if(!this.row.creditLimitCondition){
            this.$message("发行网币没有限制条件")
        }else{
            this.creditLimitCondition=this.row.creditLimitCondition.split(",")
            this.creditLimitPoint=this.row.creditLimitPoint
            this.creditLimitMoreThen=this.row.creditLimitMoreThen
            this.creditLimitBalance=this.row.creditLimitBalance
            this.creditDialog.show = true
        }
    },
    showShareLimitCondition(){
        if(!this.row.shareLimitCondition){
            this.$message("接收礼物邀请没有限制条件")
        }else{
            this.shareLimitCondition=this.row.shareLimitCondition.split(",")
            this.shareLimitPoint=this.row.shareLimitPoint
            this.shareDialog.show = true
        }
    },

    onClickBtnSelect(row){
        this.row=row.data
        this.dialog.show = true
    },

    onClickBtnDelete(row){
        var confim = confirm("确定要删除吗")
        if(confim){
            if(this.$store.getters.getUserinfo.username != "superadmin"){
                this.$message("超级管理员才能删除")
            }else{
                var data = {
                    id:row.data.id
                }
                this.$$api_system_deleteOtherSet({
                    data,
                    fn: result => {
                        this.$message.success("删除成功")
                        this.getList()
                    },
                    errFn:(data) => {
                        this.$message.error("删除失败")
                    }
                })
            }
        }
    },
    onClickBtnAdd(){
        this.$router.push({
            path:"/netx/system/addOtherSet"
        })
    },
    getList(){
        var data = {}
        this.$$api_system_getList({
            data,
            fn: result => {
                this.list = result.list
            }
        })
    }
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
