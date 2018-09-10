// import { gbs } from 'config/settings.js'

export default {
  name: 'system-productTags',
  data () {
    return {
        list : [],
        update_tags: {
            id: "",
            name: "",
        },
        update_dialog:{
            show:false
        },
        fields: [{
                  key: 'name',
                  label: '标签名',
                  width: '200'
              }, {
                  key: 'updateTime',
                  label: '修改时间',
                  width: '200',
                  formatter: (item) => {
                    var date = new Date()
                    date.setTime(item.updateTime)
                    return date.getUTCFullYear()+"-"+date.getUTCMonth()+"-"+date.getUTCDate()
                  }
              }],
        pagination: {
            current_page: 1,
            total: 30,
            page_size: 10,
            page_sizes: [10, 15, 20, 30],
            layout: 'total, sizes, prev, pager, next, jumper'
        },

        btn_info: {
            condition: {
            width: 200,
            batch: false,
            add: false,
            select: false,
            update: true,
            delete: false,
            list: [{
                text: '查看子标签', 
                type: 'primary', 
                fn: (opts) => {
                    this.$router.push({
                        path : "/netx/system/productKidTags/",
                        query: {
                            pid : opts.data.id,
                            name : opts.data.name
                        }
                    })
                }
            }]
            }
        },
    }
  },
  methods: {


    onUpdateSubmit(){
        var data = {
            id : this.update_tags.id,
            name : this.update_tags.name,
            userId : this.$store.getters.getUserinfo.username,
        }
        this.$$api_system_addOrUpdateCategoryTags({
            data,
            fn: detFlag => {
                this.$message.success("修改商品标签成功")
                this.update_dialog.show = false
                this.update_tags.name = ""
                this.getList()
            },
            errFn:(data) => {
                this.$message.error("修改商品标签失败")
            }
        })
    },

    onClickBtnUpdate(row){
        this.update_tags.id = row.data.id
        this.update_tags.name = row.data.name
        this.update_dialog.show = true
    },

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
        }

        this.$$api_system_selectProductTags({
            data,
            fn: result => {
            this.list = result.list
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
