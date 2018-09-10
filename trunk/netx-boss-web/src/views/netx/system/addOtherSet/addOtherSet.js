// import { gbs } from 'config/settings.js'

export default {
  name: 'system-addOtherSet',
  data () {
    return {
        form: {
            skillLimitType: "",
            picLimitType: "",
            wishLimitType: "",
            regMerchantLimitType: "",
            creditLimitType: "",
            shareLimitType: "",

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
            shareLimitPoint:"",

            skillLimitUserIds:"",
            picLimitUserIds:"",
            wishLimitUserIds:"",
            regMerchantLimitUserIds:"",
            creditLimitUserIds:"",
            
            creditLimitMoreThen:"",
            creditLimitBalance:"",
            canUse:"",
            disposeUserId:"",
            updateUser:"",
            createUser:"",
            type:0

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
    }
  },
  methods: {
      
    //onchange事件，选择限制类别
    skillLimitType(value){
          if(value == 0){
            //   this.$router.push({
            //       path : "/netx/ucenter/list",
            //       query : {
            //         batch : "人员名单"
            //       }
            //   })
          }else{
              this.skillDialog.show = true
          }
    },
    picLimitType(value){
          if(value == 0){
            //   this.$router.push({
            //       path : "/netx/ucenter/list",
            //       query : {
            //         batch : "人员名单"
            //       }
            //   })
          }else{
              this.picDialog.show = true
          }
    },
    wishLimitType(value){
          if(value == 0){
            //   this.$router.push({
            //       path : "/netx/ucenter/list",
            //       query : {
            //         batch : "人员名单"
            //       }
            //   })
          }else{
              this.wishDialog.show = true
          }
    },
    regMerchantLimitType(value){
          if(value == 0){
            //   this.$router.push({
            //       path : "/netx/ucenter/list",
            //       query : {
            //         batch : "人员名单"
            //       }
            //   })
          }else{
              this.regMerchantDialog.show = true
          }
    },
    creditLimitType(value){
          if(value == 0){
            //   this.$router.push({
            //       path : "/netx/ucenter/list",
            //       query : {
            //         batch : "人员名单"
            //       }
            //   })
          }else{
              this.creditDialog.show = true
          }
    },
    shareLimitType(value){
          if(value == 0){
            //   this.$router.push({
            //       path : "/netx/ucenter/list",
            //       query : {
            //         batch : "人员名单"
            //       }
            //   })
          }else{
              this.shareDialog.show = true
          }
    },
    //选好限制条件好关闭弹窗
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
    submit(){

        var temp = ""
        for (var x of this.form.skillLimitCondition) {
            temp = temp + x +","
        }
        if(temp.indexOf("0") == -1){
            this.form.skillLimitPoint=""
        }
        this.form.skillLimitCondition = temp.substring(0,temp.length-1)


        var temp = ""
        for (var x of this.form.picLimitCondition) {
            temp = temp + x +","
        }
        if(temp.indexOf("0") == -1){
            this.form.picLimitPoint=""
        }
        this.form.picLimitCondition = temp.substring(0,temp.length-1)


        var temp = ""
        for (var x of this.form.wishLimitCondition) {
            temp = temp + x +","
        }
        if(temp.indexOf("0") == -1){
            this.form.wishLimitPoint=""
        }
        this.form.wishLimitCondition = temp.substring(0,temp.length-1)


        var temp = ""
        for (var x of this.form.regMerchantLimitCondition) {
            temp = temp + x +","
        }
        if(temp.indexOf("0") == -1){
            this.form.regMerchantLimitPoint=""
        }
        this.form.regMerchantLimitCondition = temp.substring(0,temp.length-1)


        var temp = ""
        for (var x of this.form.creditLimitCondition) {
            temp = temp + x +","
        }
        if(temp.indexOf("0") == -1){
            this.form.creditLimitPoint=""
        }
        if(temp.indexOf("4") == -1){
            this.form.creditLimitMoreThen=""
        }
        if(temp.indexOf("5") == -1){
            this.form.creditLimitBalance=""
        }
        this.form.creditLimitCondition = temp.substring(0,temp.length-1)


        var temp = ""
        for (var x of this.form.shareLimitCondition) {
            temp = temp + x +","
        }
        if(temp.indexOf("0") == -1){
            this.form.shareLimitPoint=""
        }
        this.form.shareLimitCondition = temp.substring(0,temp.length-1)

        this.form.updateUser=this.$store.getters.getUserinfo.username
        this.form.createUser=this.$store.getters.getUserinfo.username
        if(this.$store.getters.getUserinfo.username == "supperadmin"){
            this.form.canUse=1
            this.form.type=1
        }else{
            this.form.canUse=0
            this.form.type=0
        }

        var data = this.form
        this.$$api_system_addOtherSet({
            data,
            fn: result => {
              this.$message.success("添加成功")
              this.form.skillLimitType=""
              this.form.skillLimitCondition=[]

              this.form.picLimitType=""
              this.form.picLimitCondition=[]

              this.form.wishLimitType=""
              this.form.wishLimitCondition=[]

              this.form.regMerchantLimitType=""
              this.form.regMerchantLimitCondition=[]

              this.form.creditLimitType=""
              this.form.creditLimitCondition=[]

              this.form.shareLimitType=""
              this.form.shareLimitCondition=[]

              this.$router.push({
                  path:"/netx/system/otherSet"
              })
            }
        })
    }
  },
  watch: {
  },
  created () {
  },
  mounted () {
  }
}
