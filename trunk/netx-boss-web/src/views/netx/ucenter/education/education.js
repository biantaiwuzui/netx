// import { gbs } from 'config/settings.js'

export default {
  name: 'user-education',
  data () {
    return {
        userEducation: [],
        nickname : '',
        educationLabel : '',
        dialog:{
            show:false,
            userEducation_info:''
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
            school: {
                info: {
                    prop: 'school',
                    label: '学校名称',
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
                    label: '院系名称',
                    sortable: true
                },
                filter: {},
                style: {
                    width: '140',
                    align: 'center'
                }
            },
            speciality: {
                info: {
                    prop: 'speciality',
                    label: '专业名称',
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
                    label: '入学年份',
                    sortable: true
                },
                filter: {},
                style: {
                    width: '130',
                    align: 'center'
                }
            },
            time: {
                info: {
                    prop: 'time',
                    label: '学习年限',
                    sortable: true
                },
                filter: {},
                style: {
                    width: '110',
                    align: 'center'
                }
            },
            degree: {
                info: {
                    prop: 'degree',
                    label: '所获学位',
                    sortable: true
                },
                filter: {},
                style: {
                    width: '110',
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
                    width: '110',
                    align: 'center'
                }
            }
        }
    }
  },
  methods: {
      fetchData () {
          this.nickname = this.$route.query.nickname
          var data = {
              userId:this.$route.query.userId
          }
          if(data.userId != null){
              this.$$api_user_getUserEducation({
                  data,
                  fn: result => {
                      this.userEducation = result.userEducation.list
                      this.educationLabel = result.userEducation.educationLabel
                  }
              })
          }
      },
      onSelectUser(userEducation){
          this.dialog.show = true;
          this.dialog.userEducation_info = userEducation;
      },
      editUserEducationInfo(){
          var data={
              id:this.dialog.userEducation_info.id,
              school:this.dialog.userEducation_info.school,
              department:this.dialog.userEducation_info.department,
              speciality:this.dialog.userEducation_info.speciality,
              year:this.dialog.userEducation_info.year,
              time:this.dialog.userEducation_info.time,
              degree:this.dialog.userEducation_info.degree,
              position:this.dialog.userEducation_info.position,
              updateUserId: JSON.parse(window.localStorage.netx_userinfo).username
          }
          var comfirm = confirm("确定修改吗?")
          if(comfirm) {
              this.$$api_user_editUserEducationInfo({
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
      onEditUser(formname){
          this.dialog.userEducation_info=this.dialog.userEducation_info
          this.$refs[formname].resetFields()
      },
      onDeleteUserEducation(scope){
          this.dialog.userEducation_info = scope;
          var data={
              id:this.dialog.userEducation_info.id,
              updateUserId: JSON.parse(window.localStorage.netx_userinfo).username
          }
          var comfirm = confirm("确定修改吗?")
          if(comfirm) {
              this.$$api_user_deleteUserEducation({
                  data,
                  fn: () => {
                      this.$message('操作成功')
                      this.dialog.show = false;
                  }
              })
          }else{
              this.$message('操作取消')
              this.dialog.show = false;
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
