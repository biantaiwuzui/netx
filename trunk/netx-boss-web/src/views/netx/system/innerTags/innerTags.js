// import { gbs } from 'config/settings.js'

export default {
  name: 'system-innerTags',
  data () {
    return {
        list : [],
        selectData: {
            value: "",
            typeCate: ""
        },
        update_tags: {
            id: "",
            value: "",
            typeCate: ""
        },
        add_tags:{
            value:"",
            typeCate:""
        },
        dialog:{
            show:false
        },
        update_dialog:{
            show:false
        },
        search_data: {
            setting: {
            inline: true
            },
            fields: [{
            key: 'search',
            type: 'select',
            multiple: false,
            list: [{
                value: 'value',
                text: '标签名'
            }, {
                value: 'typeCate',
                text: '标签项'
            }],
            desc: '请选择',
            label: ''
            }, {
            label: '',
            key: 'input',
            desc: '请输入标题'
            }]
        },
        types: {
        '0': '内置标签',
        '1': '技能标签',
        '2': '兴趣标签',
        '3': '商品可选规格'
        },
        fields: [{
            key: 'tpye',
            label: '类型',
            width: '280',
            formatter: (item) => {
                return this.types[item.type]
            },
            filter_list: [{
                text: '内置标签',
                value: 0
                },{
                text: '技能标签',
                value: 1
              }, {
                text: '兴趣标签',
                value: 2
              }, {
                text: '商品可选规格',
                value: 3
              }],
              filter_method: (value, item) => {
                return item.type === value
              },
              filter_multiple: false
                },{
                    key: 'value',
                    label: '标签名',
                    width: '280'
                }, {
                    key: 'typeCate',
                    label: '标签项',
                    width: '100',
                }, {
                    key: 'py',
                    label: '拼音',
                    width: '100'
                }, {
                    key: 'catePrivate',
                    label: '是否私有',
                    width: '100',
                    formatter: (item) => {
                        return item.catePrivate === 1 ? '是' : '否'
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
            add: true,
            select: false,
            update: true,
            delete: true,
            }
        }
    }
  },
  methods: {

    onUpdateSubmit(){
        var data = {
            id : this.update_tags.id,
            value : this.update_tags.value,
            typeCate : this.update_tags.typeCate
        }
        this.$$api_system_updateTags({
            data,
            fn: detFlag => {
                this.$message.success("修改标签成功")
                this.update_dialog.show = false
                this.update_tags.value = ""
                this.update_tags.typeCate = ""
                this.getList()
            },
            errFn:(data) => {
                this.$message.error("修改标签失败")
            }
        })
    },

    onSubmit(){
        var data = {
            value : this.add_tags.value,
            typeCate : this.add_tags.typeCate,
            isBoss:"1",
            type : 0
        }
        this.$$api_system_addTags({
            data,
            fn: addFlag => {
                this.$message.success("添加标签成功")
                this.dialog.show = false
                this.add_tags.value = ""
                this.add_tags.typeCate = ""
                this.getList()
            },
            errFn:(data) => {
                this.$message.error("添加标签失败")
            }
        })
    },

    onClickBtnUpdate(row){
        this.update_tags.id = row.data.id
        this.update_tags.value = row.data.value
        this.update_tags.typeCate = row.data.typeCate
        this.update_dialog.show = true
    },

    onClickBtnAdd(){
        this.dialog.show = true
    },

    onClickBtnDelete(row) {
        var confim = confirm("确定要删除吗")
        if(confim){
            var data = {
                id : row.data.id
            }
            this.$$api_system_deleteTagsById({
                data,
                fn: detFlag => {
                    this.$message.success("删除标签成功")
                    this.getList()
                },
                errFn:(data) => {
                    this.$message.error("删除标签失败")
                }
            })
        }
    },

    onSearch (data) {
        if(data.data.search == 'value'){
            this.selectData.typeCate = ""
            this.selectData.value = data.data.input
        }
        if(data.data.search == 'typeCate'){
            this.selectData.value = ""
            this.selectData.typeCate = data.data.input
        }
        this.getList({
            valueSelect : this.selectData.value,
            typeCateSelect : this.selectData.typeCate,
            fn: () => {
                this.setPath('value', this.selectData.nickname),
                this.setPath('typeCate', this.selectData.typeCate)
            }
        })
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
        valueSelect,
        typeCateSelect,
        fn
              } = {}) {
      var query = this.$route.query

      this.pagination.current_page = page || parseInt(query.page) || 1
      this.pagination.page_size = pageSize || parseInt(query.page_size) || this.pagination.page_size
      this.selectData.value = valueSelect || parseInt(query.value) || this.selectData.value
      this.selectData.typeCate = typeCateSelect || parseInt(query.typeCate) || this.selectData.typeCate


      var data = {
          currentPage: this.pagination.current_page,
          size: this.pagination.page_size,
          value:this.selectData.value,
          typeCate:this.selectData.typeCate,
          type:0
      }

      this.$$api_system_selectTags({
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
