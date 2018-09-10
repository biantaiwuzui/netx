import { userInfo } from "os";

// import { gbs } from 'config/settings.js'

export default {
  name: 'sellerWhite-list',
  data () {
    return {
      name:"",
      list: [],
      defriendData:{
        backReason:"",
        id:"",
        status:2
      },
      backDialog:{
        show:false
      },
      fields: [
          {
            key: 'name',
            label: '名称',
          }, {
            key: 'nickname',
            label: '注册者昵称',
          },{
            key: 'lv',
            label: '注册者等级'
          },{
            key: 'credit',
            label: '信用'
          },{
            key: 'provinceCode',
            label: '省份',
          }, {
            key: 'cityCode',
            label: '市',
          }, {
            key: 'expireTime',
            label: '过期时间',
            formatter: (item) => {
              if(!item.expireTime){
                return ""
              }
              if(item.expireTime==-1){
                return "终身有效"
              }else{
                var date = new Date();
                date.setTime(item.expireTime)
                return date.getFullYear() + "-" + (date.getUTCMonth()+1) + "-" + date.getUTCDate();
              }
            }
          }, {
            key: 'visitCount',
            label: '访问量',
          }, {
            key: 'achievementTotal',
            label: '业绩',
          }, {
            key: 'status',
            label: '状态',
            formatter: (item) => {
              return item.status == 1 ? '正常' : '拉黑'
            }
            }, {
            key: 'payStatus',
            label: '状态',
            formatter: (item) => {
              if(item.payStatus==0){
                return "已缴费"
              }else if(item.payStatus==1){
                return "待缴费"
              }else if(item.payStatus==2){
                return "待续费"
              }
            }
            }
        ],
      pagination: {
        current_page: 1,
        total: 30,
        page_size: 10,
        page_sizes: [10, 20, 30],
        layout: 'total, sizes, prev, pager, next, jumper'
      },
      btn_info: {
        condition: {
          width: 200,
          batch: false,
          add: true,
          add_text:"报表",
          select: false,
          update: false,
          delete: false,
          list: [{
            text: '拉黑', // 按钮文本
            type: 'warning', // 按钮类型，遵循elementUI的几种按钮类型
            // 如果不传按钮，默认会触发onClickBtn方法，传了则不会调用onClickBtn方法
            fn: (opts) => {
              this.defriendData.id=opts.data.id
              this.backDialog.show=true
            }
          }]
        }
      }
    }
  },
  methods: {

      onSelectData(){
        this.getList()
      },

      backReasonSubmit(){
        var data = this.defriendData
        this.$$api_shoppingmall_defriend({
            data,
            fn: result => {
              this.$message.success("拉黑成功")
              this.backDialog.show=false
              this.getList()
              this.defriendData.backReason=""
            }
        })
      },

      /**生成报表 */
      onClickBtnAdd(){
        window.location.href='http://localhost:8090/netx/business/seller/export'
      },
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
      onSubmit(){
        var data = {
          articleId : this.article_info.id,
          reason : this.reason  
        }
        if(this.type == 1){
          if(this.article_info.statusCode == 1){
            this.$message("已经是异常状态")
          }else{
            this.$$api_article_pushException({
              data,
              fn: result => {
                this.dialog.show = false
                this.reason = ""
                console.log(result)
              }
            })
          }    
        }else if(this.type == 2){
          if(this.article_info.statusCode == 0){
            this.$message("该咨询不是异常状态")
          }else{
            this.$$api_article_popException({
              data,
              fn: result => {
                this.dialog.show = false
                this.reason = ""
                console.log(result)
              }
            })
          }
        }
        
        
      },

      /**
       * 改变每页显示数量事件
       * @param  {number} size 当前每页显示数量
       */
      onChangeCurPageSize (pageSize) {
        this.getList({
          pageSize,
          fn: () => {
            this.setPath('page_size', pageSize)
          }
        })
      },

      getList ({
          page,
          pageSize,
          fn
                } = {}) {
        var query = this.$route.query

        this.pagination.current_page = page || parseInt(query.page) || 1
        this.pagination.page_size = pageSize || parseInt(query.page_size) || this.pagination.page_size

        var data = {
            currentPage: this.pagination.current_page,
            size: this.pagination.page_size,
            status:1,
            name:this.name
        }

        this.$$api_shoppingmall_getSellerList({
            data,
            fn: result => {
              this.list = result.records
              this.pagination.total = result.total
              fn && fn()
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
