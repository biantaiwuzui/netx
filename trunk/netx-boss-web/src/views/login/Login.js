export default {
  name: 'login',
  data () {
    return {
      winSize: {
        width: '',
        height: ''
      },

      formOffset: {
        position: 'absolute',
        left: '',
        top: ''
      },

      login_actions: {
        disabled: false
      },
        register : false,

      data: {
        username: '',
        password: ''
        // token: ''
      },

      rule_data: {
        userName: [{
          validator: (rule, value, callback) =>{
            if (value === '') {
              callback(new Error('请输入用户名'))
            } else {
              if (/^[a-zA-Z0-9_-]{4,16}$/.test(value)) {
                callback()
              } else {
                callback(new Error('用户名至少4位,由大小写字母和数字,-,_组成'))
              }
            }
          },
          trigger: 'blur'
        }],
        password: [{
          validator: (rule, value, callback) => {
            if (value === '') {
              callback(new Error('请输入密码'))
            } else {
              if (!(/^[a-zA-Z0-9_-]{6,16}$/.test(value))) {
                callback(new Error('密码至少6位,由大小写字母和数字,-,_组成'))
              } else {
                if (this.register === true) {
                  if (this.data.repassword !== '') {
                    this.$refs.data.validateField('repassword')
                  }
                }
                callback()
              }
            }
          },
          trigger: 'blur'
        }]
      }
    }
  },
  methods: {
    setSize () {
      this.winSize.width = this.$$lib_$(window).width() + 'px'
      this.winSize.height = this.$$lib_$(window).height() + 'px'

      this.formOffset.left = (parseInt(this.winSize.width) / 2 - 175) + 'px'
      this.formOffset.top = (parseInt(this.winSize.height) / 2 - 178) + 'px'
    },

    onLogin (ref, type) {
      this.$refs[ref].validate((valid) => {
        if (valid) {
            this.login_actions.disabled = true
            // 如果记住密码，提交的信息包括真实token，密码则是假的
            // 服务端登录验证优先级：用户名必须，其次先取token，不存在时再取密码
            this.$$api_user_login({
            data: this[ref],
                fn: data => {
                    var userinfo = {
                       token: data.token.access_token,
                       username:this[ref].username 
                      }
                    this.$store.dispatch('update_userinfo',{
                        userinfo: userinfo 
                    }).then(() => {
                        this.login_actions.disabled = false
                        this.$router.push('/netx/ucenter/list')
                    })
                },
                errFn:(data) => {
                    console.log(data)
                    this.login_actions.disabled = false
                    this.$message.error("账号或者密码错误")
                },
            tokenFlag: true
          })
        }
      })
    },

    resetForm (ref) {
      this.$refs[ref].resetFields()
    }
  },
  created () {
    this.setSize()
    this.$$lib_$(window).resize(() => {
      this.setSize()
    })
  }
}