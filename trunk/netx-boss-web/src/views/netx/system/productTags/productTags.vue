<template>
        <div class="list">
                <list-data
                @onChangeCurrentPage="onChangeCurPage"
                @onChangePageSize="onChangeCurPageSize"
                @onClickBtnUpdate="onClickBtnUpdate"
                ref='user-article'
                :Pagination='pagination'
                :BtnInfo="btn_info.condition"
                :List='list'
                :FieldList='fields'>
                </list-data>
                <el-dialog title="修改商品标签" v-model="update_dialog.show" >
                        <el-form style="margin:20px;width:60%;min-width:100%"
                        label-width="150px">
                                <el-form-item label="标签名：" prop='name' style="width:600px;">
                                        <el-input v-model="update_tags.name"></el-input>
                                </el-form-item>
                                <el-form-item >
                                        <el-button type="primary" @click='onUpdateSubmit()'>确定</el-button>
                                </el-form-item>
                        </el-form>
                </el-dialog>
        </div>
</template>
<script>
  import ProductTagsJs from './productTags.js'

  export default ProductTagsJs
</script>
<style scoped lang='less'>

</style>
