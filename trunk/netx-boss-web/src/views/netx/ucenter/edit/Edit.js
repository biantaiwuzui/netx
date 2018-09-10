// import { gbs } from 'config/settings.js'

export default {
  name: 'edit-user',
  data () {
    return {
      aa:"",
      form_data: {},
      form_rules: {
        name: [{
          required: true,
          message: '订单名称不能为空！',
          trigger: 'blur'
        }],
        status: [{
          required: true,
          message: '文章分类不能为空！',
          trigger: 'change'
        }]
      }
    }
  },
  methods: {
    fetchData(){
      this.form_data = this.$route.query
      var sex = this.form_data.sex
        if(sex == "男"){
            this.form_data.sex = "1"
        }else if(sex == "女"){
            this.form_data.sex = "0"
        }
      var login = this.form_data.login
        if(login){
            this.form_data.login = "1"
        }else if(!login){
            this.form_data.login = "0"
        }
        var adminUser = this.form_data.adminUser
        if(login){
            this.form_data.adminUser = "1"
        }else if(!login){
            this.form_data.adminUser = "0"
        }

    },

    /**
     * 提交表单
     * @param  {string} formName 表单名称
     */
    onSubmit (formName) {
      var ref = this.$refs[formName]
      ref.validate((valid) => {
        if (valid) {
          // console.log(this.form_data);
          this.$$api_user_editUser({
            data: this.form_data,
            fn: data => {
              this.$router.push('/netx/ucenter/list')
            }
          })
        }
      })
    },
    reset_form (form) {
      this.$refs[form].resetFields()
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
