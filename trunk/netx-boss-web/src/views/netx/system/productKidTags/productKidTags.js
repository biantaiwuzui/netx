// import { gbs } from 'config/settings.js'

export default {
  name: 'system-productKidTags',
  data () {
    return {
        list : [],
        pname:"",
        dialog:{
            show:false
        },
        add_tags:{
            name:""
        },
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
            delete_text: '删除',
            update_text: '修改',    
            width: 200,
            batch: false,
            add: true,
            select: false,
            update: true,
            delete: true,
            }
        },
    }
  },
  methods: {

    innerTags(){
        this.$router.push({
            path : "/netx/system/innerTags/"
        })
    },
    skillTags(){
        this.$router.push({
            path : "/netx/system/skillTags/"
        })
    },
    interestTags(){
        this.$router.push({
            path : "/netx/system/interestTags/"
        })
    },
    productTags(){
        this.$router.push({
            path : "/netx/system/productTags/"
        })
    },

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

    onSubmit(){
        var data = {
            userId : this.$store.getters.getUserinfo.username,
            pid : this.$route.query.pid,
            name : this.add_tags.name
        }
        this.$$api_system_addOrUpdateCategoryTags({
            data,
            fn: addFlag => {
                this.$message.success("添加商品子标签成功")
                this.dialog.show = false
                this.add_tags.name = ""
                this.getList()
            },
            errFn:(data) => {
                this.$message.error("添加商品子标签失败")
            }
        })
    },


    onClickBtnAdd(){
       this.dialog.show = true
    },

    onClickBtnUpdate(row){
        this.update_tags.id = row.data.id
        this.update_tags.name = row.data.name
        this.update_dialog.show = true
    },
    onClickBtnDelete(row){
        var confim = confirm("确定要删除吗")
        if(confim){
            var data = {
                id : row.data.id
            }
            this.$$api_system_deleteCategoryById({
                data,
                fn: detFlag => {
                    this.$message.success("删除商品标签成功")
                    this.getList()
                },
                errFn:(data) => {
                    this.$message.error("删除商品标签失败")
                }
            })
        }
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
            pid:this.$route.query.pid
        }
        if(this.$route.query.pid){
            this.$$api_system_selectKidTags({
                data,
                fn: result => {
                this.list = result.list
                this.pagination.total = result.total
                this.pname = this.$route.query.name
                fn && fn()
                }
            })
        }
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
