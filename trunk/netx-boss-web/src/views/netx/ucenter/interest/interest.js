// import { gbs } from 'config/settings.js'

export default {
    name: 'user-interest',
    data(){
        return {
            userInterest:[],
            nickname:'',
            interestLabel:'',
            dialog:{
                show:false,
                userInterest_info:''
            },
            dialogTwo:{
                show:false,
                userInterest_info:{
                    interestType:'',
                    interestDetail:''
                }
            },
            fields:{
                id:{
                    info:{
                        prop:'id',
                        label:'id',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        align:'center'
                    }
                },
                interestType:{
                    info:{
                        prop:'interestType',
                        label:'兴趣类别',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'200',
                        align:'center'
                    }
                },
                interestDetail:{
                    info:{
                        prop:'interestDetail',
                        label:'具体内容',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'200',
                        align:'center'
                    }
                },
                position:{
                    info:{
                        prop:'position',
                        label:'位置序号',
                        sortable:true
                    },
                    filter:{},
                    style:{
                        width:'130',
                        align:'center'
                    }
                }
            }
        }
    },
    methods:{
        onEditInterest(userInterest) {
            this.dialog.show = true;
            this.dialog.userInterest_info = userInterest;
        },
        editUserHobbyInfo(){
            var data={
                id:this.dialog.userInterest_info.id,
                interestType:this.dialog.userInterest_info.interestType,
                interestDetail:this.dialog.userInterest_info.interestDetail,
                position:this.dialog.userInterest_info.position
            }
            var comfirm = confirm("确定修改吗?")
            if(comfirm) {
                this.$$api_user_editUserHobbyInfo({
                    data,
                    fn: () => {
                        this.$message('操作成功')
                    }
                })
            }else{
                this.$message('操作取消')
            }
            this.dialog.show = false
        },
        onDeleteInterest(userInterest) {
            var confim = confirm("确定要删除吗")
            if(confim){
                var data = {
                    id:userInterest.id,
                    userId:this.$route.query.userId
                }
                this.$$api_user_deleteUserInterestDetail({
                    data,
                    fn: detFlag => {
                        this.$message.success("删除用户成功")
                    },
                    errFn:(data) => {
                        this.$message.error("删除用户失败")
                    }
                })
            }
            this.dialog.show = true
            this.dialog.userInterest_info = userInterest
        },
        onAddInterest(){
            this.dialogTwo.show = true
        },
        onSubmit(){
            var data = {
                id:"",
                userId:this.$route.query.userId,
                interestType: this.dialogTwo.userInterest_info.interestType,
                interestDetail: this.dialogTwo.userInterest_info.interestDetail
            }
            this.$$api_user_insertUserInterestDetail({
                data,
                fn: detFlag => {
                    this.$message.success("删除用户成功")
                },
                errFn:(data) => {
                    this.$message.error("删除用户失败")
                }
            })
            this.dialogTwo.show = false
        },
        reset_form () {

        },
        onEditUser2(formname){
            this.dialog.userInterest_info=this.dialog.userInterest_info
            this.$refs[formname].resetFields()
        },

        fetchData () {
            this.nickname = this.$route.query.nickname;
            this.userId = this.$route.query.userId;
            var data = {
                userId:this.$route.query.userId
            }
            if(data.userId != null){
                this.$$api_user_selectUserInterest({
                    data,
                    fn: result => {
                        this.userInterest = result.result.list;
                        this.interestLabel = result.result.interestLabel;
                    }
                })
            }
        }
    },
    watch : {
        '$route' : 'fetchData',
    },
    created () {
    },
    mounted () {
        this.fetchData()
    },
    '$route' (to, from) {
    }
}
