export default {
    name: 'user-verifyResource',
    data () {
        return {
            idNumber:"",
            videoResource: [],
            idCardResource: [],
            carResource: [],
            houseResource: [],
            degreeResource: [],
            nickName:"",
            userNumber:"",
            lv:"",
            sex:"",
            age:"",
            userVerifyIds: [],
            userVerifyId: "",
            imgUrl: "",
            name: "",
            idCar: "",

            dialog:{
                show:false
            },
            disagreedialog:{
                show:false
            },
            bigImgdialog:{
                show:false
            },
            default_value: {
                input: "",
                userVerify: [],
                disagreeUserVerify: ""
            },
            disagreeFields: [
                {
                    label: '认证对象',
                    key: 'disagreeUserVerify',
                    type: 'radio',
                    desc: 'radio',
                    list: [{
                        text: '房产认证',
                        value: 'houseResource'
                    }, {
                        text: '身份认证',
                        value: 'idCardResource'
                    }, {
                        text: '学历认证',
                        value: 'degreeResource'
                    }, {
                        text: '车辆认证',
                        value: 'carResource'}],
                    events: {
                      change: ({value, info}) => {
                        this.$message('当前选中的是：' + info)
                      }
                    }
                },
                {
                    label: '拒接理由',
                    key: 'input',
                    desc: '请输入理由...',
                }
            ],
            fields: [
                {
                label: '认证对象：',
                type: 'checkbox',
                key: 'userVerify',
                list: [{
                    text: '房产认证',
                    value: 'houseResource'
                }, {
                    text: '身份认证',
                    value: 'idCardResource'
                }, {
                    text: '学历认证',
                    value: 'degreeResource'
                }, {
                    text: '车辆认证',
                    value: 'carResource'}],  
                events: {
                    change: ({info, value}) => {
                        this.$message('当前选中的值是：' + info.join(','))
                    }
                } 
            }]
        }
    },
    methods: {

        getVerify () {

            var data = {
                userId: this.$route.query.userId
            }
            if(data.userId != null){
                this.$$api_user_selectUserVerify({
                    data,
                    fn: result => {
                        this.videoResource = result.result.videoResource
                        this.idCardResource = result.result.idCardResource
                        this.carResource = result.result.carResource
                        this.houseResource = result.result.houseResource
                        this.degreeResource = result.result.degreeResource
                        this.idNumber = result.result.idNumber
                        this.nickName = result.result.nickName
                        this.userNumber = result.result.userNumber
                        this.lv = result.result.lv
                        this.sex = result.result.sex
                        this.age = result.result.age
                        this.getNameAndIdCar()
                    }
                })
            } 
        },

        agree () {
            this.dialog.show = true
        },
        disagree () {
              this.disagreedialog.show = true
        },
        onSubmit () {
            this.getUserVerifyIds()
            var userVerifyIds = this.userVerifyIds.join(',');
            var data = {
                reason: "",
                status: 1 ,
                updateUserId: this.$store.getters.getUserinfo.username,                
                userId: this.$route.query.userId,
                userVerifyId: userVerifyIds
            }
            this.$$api_user_operateUserVerify({
                data,
                fn: detFlag => {
                    this.$message.success("通过认证成功")
                },
                errFn:(data) => {
                    this.$message.error("通过认证失败")
                }
            })
            this.dialog.show = false
        },
        disagreOnSubmit () {
            this.getUserVerifyId()
            var data = {
                reason: this.default_value.input,
                status: 2 ,
                updateUserId: this.$store.getters.getUserinfo.username,                
                userId: this.$route.query.userId,
                userVerifyId: this.userVerifyId
            }
            this.$$api_user_operateUserVerify({
                data,
                fn: detFlag => {
                    this.$message.success("拒接通过成功")
                },
                errFn:(data) => {
                    this.$message.error("拒接通过失败")
                }
            })
            this.disagreedialog.show = false
        },
        getUserVerifyIds () {
            for (var i=0;i<this.default_value.userVerify.length;i++)
            {
                if (this.default_value.userVerify[i] === "idCardResource" && this.idCardResource != null){
                    this.userVerifyIds.push(this.idCardResource[0].userVerifyId)
                }
                if (this.default_value.userVerify[i] === "carResource" && this.carResource != null){
                    this.userVerifyIds.push(this.carResource[0].userVerifyId)
                }
                if (this.default_value.userVerify[i] === "houseResource" && this.houseResource != null){
                    this.userVerifyIds.push(this.houseResource[0].userVerifyId)
                }
                if (this.default_value.userVerify[i] === "degreeResource" && this.degreeResource != null){
                    this.userVerifyIds.push(this.degreeResource[0].userVerifyId)
                }
            }
        },
        getUserVerifyId () {
            if (this.default_value.disagreeUserVerify === "idCardResource" && this.idCardResource != null){
                this.userVerifyId = this.idCardResource[0].userVerifyId
            }
            if (this.default_value.disagreeUserVerify === "carResource" && this.carResource != null){
                this.userVerifyId = this.carResource[0].userVerifyId
            }
            if (this.default_value.disagreeUserVerify === "houseResource" && this.houseResource != null){
                this.userVerifyId = this.houseResource[0].userVerifyId
            }
            if (this.default_value.disagreeUserVerify === "degreeResource" && this.degreeResource != null){
                this.userVerifyId = this.degreeResource[0].userVerifyId
            }
        },
        bigImg (url) {
            this.bigImgdialog.show = true
            this.imgUrl = url
        },
        getNameAndIdCar () {
            if (this.idNumber != null){
                var array = this.idNumber.split(',')
                this.name = array[0]
                this.idCar = array[1]
            }
        }
    },
    watch : {
        '$route' : 'getVerify'
    },
    mounted () {
        this.getVerify({
        })
    },
    '$route' (to, from) {
    }
}
