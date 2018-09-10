export default {
  name: 'user-list',
  data () {
    return {
        userList: [],

        // 需要给分页组件传的信息
        paginations: {
            current_page: 1,
            total: 0,
            page_size: 10,
            page_sizes: [10, 20, 30],
            layout: 'total, sizes, prev, pager, next, jumper'
        },
        dialog:{
          show:false,
          user_info:""
        },
        selectData: {
            nickname: "",
            userNumber: "",
            mobile: ""
        },
      btn_info: {
        add: false,
        batch_delete_text: '批量删除'
      },
      
        fields: [{
          key: 'nickname',
          label: '用户昵称'
        }, {
          key: 'userNumber',
          label: '网号'
        }, {
          key: 'mobile',
          label: '用户手机',
        }, {
          key: 'birthday',
          label: '生日',
          formatter: (item) => {
            var date = new Date();
            date.setTime(item.birthday)
            return date.getFullYear() + "-" + (date.getUTCMonth()+1) + "-" + date.getUTCDate();
          }
        }, {
          key: 'score',
          label: '总积分',
        }, {
          key: 'credit',
          label: '总信用',
        }, {
          key: 'value',
          label: '总身价',
        }, {
          key: 'contribution',
          label: '总贡献',
        }], 
    }
  },
  methods: {

    /**
     * 格式化状态
     */

    /**
     * 改变页码和当前页时需要拼装的路径方法
     * @param {string} field 参数字段名
     * @param {string} value 参数字段值
     */
    setPath (field, value) {
      var path = this.$route.path
      var query = Object.assign({}, this.$route.query)

      if (typeof field === 'object') {
        query = field
      } else {
        query[field] = value
      }
    },

    /**
     * 改变当前页事件
     * @param  {number} page 当前页码
     */
    onChangeCurPage (page) {
      this.getList({
        page,
        fn: () => {
          this.setPath('page', page)
        }
      })
    },

    /**
     * 改变每页显示数量事件
     * @param  {number} size 当前每页显示数量
     */
    onChangePageSize (pageSize) {
      this.getList({
        pageSize,
        fn: () => {
          this.setPath('page_size', pageSize)
        }
      })
    },

    /**
     * 获取订单列表
     * @param  {number} options.page      当前页码，切换页码时用
     * @param  {number} options.page_size 每页显示数量，改变每页数量时用
     * @param  {function} options.fn                            } 获取列表后的回调函数
     * @author  lcx
     */
    getList ({
               page,
               pageSize,
               nicknameSelect,
               mobileSelect,
               userNumberSelect,
               where,
               fn
             } = {}) {
      var query = this.$route.query
      this.paginations.current_page = page || parseInt(query.currentPage) || 1
      this.paginations.page_size = pageSize || parseInt(query.size) || this.paginations.page_size
      this.selectData.nickname = nicknameSelect || this.selectData.nickname
      this.selectData.mobile = mobileSelect || this.selectData.mobile
      this.selectData.userNumber = userNumberSelect || this.selectData.userNumber

      var data = {
        currentPage: this.paginations.current_page,
        size: this.paginations.page_size,
        nickname:this.selectData.nickname,
        mobile:this.selectData.mobile,
        userNumber:this.selectData.userNumber
      }

      if (where) {
        data = Object.assign(data, where || {})
      }

      this.$$api_user_selectUser({
        data,
        fn: userData => {
          this.userList = userData.data.userList
          this.paginations.total = userData.data.count
        }
      })
      //TODO
      if(this.$route.query.batch){//batch从添加其他设置传过来 
        this.btn_info.batch_delete_text = query.batch
      }
    },
      onSelectData () {
          this.getList({
              nicknameSelect:this.selectData.nickname,
              mobileSelect:this.selectData.mobile,
              userNumberSelect:this.selectData.userNumber,
              fn: () => {
                  this.setPath('nickname', this.selectData.nickname),
                  this.setPath('mobile', this.selectData.mobile),
                  this.setPath('userNumber', this.selectData.userNumber)
              }
          })
      },
      onClickBtnSelect(user) {
          this.dialog.show = true
          this.dialog.user_info = user.data
      },
      onClickBtnUpdate(user){
        this.$router.push({
            name : "编辑用户",
            query : user.data
        })
      },
      onClickBtnBatchDelete ({ids, datas}) {
        //TODO
        this.$confirm('删除的数据：' + ids.join(','), '确认删除？').then(() => {
          this.$$api_article_deleteArticle({
            data: {
              id: ids.join(',')
            },
            fn: data => {
              this.onGetList()
            }
          })
        })
      },
      onClickBtnDelete(user){
          var confim = confirm("确定要删除吗")
          if(confim){
              var data = {
                  userId : user.data.id
              }
              this.$$api_user_deleteUserById({
                  data,
                  fn: detFlag => {
                      this.$message.success("删除用户成功")
                  },
                  errFn:(data) => {
                      this.$message.error("删除用户失败")
                  }
              })
          }
      },
      selectUserDetail(userId,nickname) {
          this.dialog.show = false
          this.$router.push({
              path : "/netx/ucenter/detail/",
              query:{
                  userId: userId,
                  nickname: nickname
              }
          })
      },
      selectUserEducation(userId,nickname) {
          this.dialog.show = false
          this.$router.push({
              path:"/netx/ucenter/education",
              query:{
                  userId: userId,
                  nickname: nickname
              }
          })
      },
      selectUserProfession(userId,nickname) {
          this.dialog.show = false
          this.$router.push({
              path:"/netx/ucenter/profession",
              query:{
                  userId: userId,
                  nickname: nickname
              }
          })
      },
      selectUserInterest(userId,nickname) {
          this.dialog.show = false
          this.$router.push({
              path:"/netx/ucenter/interest",
              query:{
                  userId: userId,
                  nickname: nickname
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
