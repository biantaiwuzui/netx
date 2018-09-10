<template>
        <div class="list">
                <list-data
                ref='user-article'
                @onSearch="onSearch"
                @onChangeCurrentPage="onChangeCurPage"
                @onChangePageSize="onChangeCurPageSize"
                @onClickBtnDelete="onClickBtnDelete"
                @onClickBtnAdd="onClickBtnAdd"
                @onClickBtnUpdate="onClickBtnUpdate"
                :Search="search_data"
                :Pagination='pagination'
                :BtnInfo="btn_info.condition"
                :List='list'
                :FieldList='fields'>
                </list-data>
                <el-dialog title="添加兴趣标签" v-model="dialog.show" >
                        <el-form style="margin:20px;width:60%;min-width:100%"
                                label-width="150px">
                                <el-form-item label="标签名：" prop='value' style="width:600px;">
                                        <el-input v-model="add_tags.value"></el-input>
                                </el-form-item>
                                <el-form-item label="标签项" prop='typeCate' style="width:600px;">
                                        <el-input v-model="add_tags.typeCate"></el-input>
                                </el-form-item>

                                <el-form-item >
                                        <el-button type="primary" @click='onSubmit()'>确定</el-button>
                                </el-form-item>
                        </el-form>
                </el-dialog>
                <el-dialog title="修改标签" v-model="update_dialog.show" >
                        <el-form style="margin:20px;width:60%;min-width:100%"
                        label-width="150px">
                                <el-form-item label="标签名：" prop='value' style="width:600px;">
                                        <el-input v-model="update_tags.value"></el-input>
                                </el-form-item>
                                <el-form-item label="标签项" prop='typeCate' style="width:600px;">
                                        <el-input v-model="update_tags.typeCate"></el-input>
                                </el-form-item>

                                <el-form-item >
                                        <el-button type="primary" @click='onUpdateSubmit()'>确定</el-button>
                                </el-form-item>
                        </el-form>
                </el-dialog>
        </div>
</template>
<script>
  import SkillTagsJs from './skillTags.js'

  export default SkillTagsJs
</script>
<style scoped lang='less'>

</style>
