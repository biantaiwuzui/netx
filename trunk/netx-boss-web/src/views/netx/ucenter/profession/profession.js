// import { gbs } from 'config/settings.js'

export default {
  name: 'user-profession',
  data () {
    return {
        userProfession: [],
        nickname : '',
        professionLabel:'',
        userProfession_info:'',
        // 需要给分页组件传的信息
        paginations: {
            current_page: 1,
            page_size: 10,
            total: 0,
            page_sizes: [10, 20, 30],
            layout: 'total,sizes, prev, pager, next, jumper'
        },
        dialog:{
            show:false,
            userProfession_info:""
        },
        dialog1:{
            show:false
        },
        dialog2:{
            show:false,
            userProfession_info:{
                company : "",
                department : "",
                topProfession : "",
                year : ""
            }
        },
        fields: {
            id: {
                info: {
                    prop: 'id',
                    label: 'id',
                    sortable: true
                },
                filter: {},
                style: {
                    align: 'center'
                }
            },
            company: {
                info: {
                    prop: 'company',
                    label: '单位全称',
                    sortable: true
                },
                filter: {},
                style: {
                    width: '160',
                    align: 'center'
                }
            },
            department: {
                info: {
                    prop: 'department',
                    label: '部门',
                    sortable: true
                },
                filter: {},
                style: {
                    width: '140',
                    align: 'center'
                }
            },
            topProfession: {
                info: {
                    prop: 'topProfession',
                    label: '最高职位',
                    sortable: true
                },
                filter: {},
                style: {
                    width: '140',
                    align: 'center'
                }
            },
            year: {
                info: {
                    prop: 'year',
                    label: '入职年份',
                    sortable: true
                },
                filter: {},
                style: {
                    width: '130',
                    align: 'center'
                }
            },
            position: {
                info: {
                    prop: 'position',
                    label: '位置序号',
                    sortable: true
                },
                filter: {},
                style: {
                    width: '130',
                    align: 'center'
                }
            }
        }
    }
  }
  ,
  methods: {
      onSubmit(){
          var data = {
              id:"",
              userId:this.$route.query.userId,
              company: this.dialog2.userProfession_info.company,
              department: this.dialog2.userProfession_info.department,
              topProfession: this.dialog2.userProfession_info.topProfession,
              year: this.dialog2.userProfession_info.year
          }
          this.$$api_user_insertUserProfessionDetail({
              data,
              fn: detFlag => {
                this.$message.success("增加用户成功")
            },
            errFn:(data) => {
                this.$message.error("增加用户失败")
            }
          })
          this.dialog2.show = false
      },
      onEditUser(userProfession) {
          this.dialog.show = true
          this.userProfession_info = userProfession
          this.dialog.userProfession_info = userProfession
      },
      onEditUser1(formname){
          this.dialog.userProfession_info=this.userProfession_info
          this.$refs[formname].resetFields()
      },
      editUserWorkExperience(){
          var data={
              id:this.userProfession_info.id,
              company:this.userProfession_info.company,
              department:this.userProfession_info.department,
              topProfession:this.userProfession_info.topProfession,
              year:this.userProfession_info.year,
              updateUserId: JSON.parse(window.localStorage.netx_userinfo).username
          }
          var comfirm = confirm("确定修改吗?")
          if(comfirm) {
              this.$$api_user_editUserWorkExperience({
                  data,
                  fn: () => {
                      this.$message('操作成功')
                      this.fetchData ()
                  }
              })
          }else{
              this.$message('操作取消')
              this.fetchData ()
          }
      },

      onDeleteUser(userProfession) {
          var confim = confirm("确定要删除吗")
          if(confim){
            var data = {
                id:userProfession.id
            }
            this.$$api_user_deleteUserProfessionDetail({
                data,
                fn: detFlag => {
                    this.$message.success("删除用户成功")
                    this.fetchData ()
                },
                errFn:(data) => {
                    this.$message.error("删除用户失败")
                    this.fetchData ()
                }
            })
          }
      },
      onAddProfession(){
          this.dialog2.show = true
      },
      fetchData () {
          this.nickname = this.$route.query.nickname

          var data = {
              userId:this.$route.query.userId,
              currentPage: this.paginations.current_page,
              size: this.paginations.page_size
          }
          if(data.userId != null){
              this.$$api_user_getUserProfession({
                  data,
                  fn: result => {
                      this.userProfession = result.result.list
                      this.professionLabel = result.result.professionLabel
                  }
              })
          }
      }
  },
    watch : {
        '$route' : 'fetchData'
    },
    created () {
    },
    mounted () {
        this.fetchData()
    },
    '$route' (to, from) {
    }
}
