<template>
        <div class="list">
                <el-row style="margin:10px">
                        <el-button
                                type="primary"
                                @click='innerTags()'>内置标签
                        </el-button>
                        <el-button
                                type="primary"
                                @click='skillTags()'>技能标签
                        </el-button>
                        <el-button
                                type="primary"
                                @click='interestTags()'>兴趣标签
                        </el-button>
                        <el-button
                                type="primary"
                                @click='productTags()'>{{pname}}
                        </el-button>
                </el-row>
                <list-data
                @onChangeCurrentPage="onChangeCurPage"
                @onChangePageSize="onChangeCurPageSize"
                @onClickBtnUpdate="onClickBtnUpdate"
                @onClickBtnAdd="onClickBtnAdd"
                @onClickBtnDelete="onClickBtnDelete"
                ref='user-article'
                :Pagination='pagination'
                :BtnInfo="btn_info.condition"
                :List='list'
                :FieldList='fields'>
                </list-data>
                <el-dialog title="添加商品标签" v-model="dialog.show" >
                        <el-form style="margin:20px;width:60%;min-width:100%"
                                label-width="150px">
                                <el-form-item label="标签名：" prop='name' style="width:600px;">
                                        <el-input v-model="add_tags.name"></el-input>
                                </el-form-item>
                               <el-form-item >
                                        <el-button type="primary" @click='onSubmit()'>确定</el-button>
                                </el-form-item>
                        </el-form>
                </el-dialog>
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
  import ProductKidTagsJs from './productKidTags.js'

  export default ProductKidTagsJs
</script>
<style scoped lang='less'>

</style>
