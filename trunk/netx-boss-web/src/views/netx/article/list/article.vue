<template>
  <div class="list">
    <el-form :inline="true" :model="selectFormData" class="demo-form-inline">
      <el-form-item>
        <el-input placeholder="图文标题" v-model="selectFormData.title"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input placeholder="网号" v-model="selectFormData.userNumber"></el-input>
      </el-form-item>
      <el-select placeholder="图文类型" v-model="selectFormData.advertorialType">
        <el-option v-for="item in advertorialTypeItems" :label="item.name" :value="item.id"></el-option>
      </el-select>
      <el-select placeholder="图文状态" v-model="selectFormData.statusCode">
        <el-option v-for="item in statusCodeItems" :label="item.name" :value="item.id"></el-option>
      </el-select>
      <el-form-item>
        <el-button type="primary" @click="onSubmitForm">查询</el-button>
      </el-form-item>
    </el-form>
    <list-data
      ref='user-article'
      @onChangeCurrentPage="onChangeCurPage"
      @onChangePageSize="onChangeCurPageSize"
      @onClickBtnDelete="onClickBtnDelete"
      :Pagination='pagination'
      :BtnInfo="btn_info.condition"
      :List='articles'
      :Expand="expand"
      :FieldList='fields'>
        <template
          slot-scope="scope"
          slot="expand">
          <div style="font-weight: bold;">内容详情：</div>
          <div style="font-size:14px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span v-html="scope.data.content"></span></div>
        </template>
      </list-data>
      <el-dialog title="列入/解除异常" v-model="dialog.show" >
        <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px">
          <el-form-item label="原因：" prop='reason' style="width:600px;">
            <el-input v-model="reason"></el-input>
          </el-form-item>

          <el-form-item >
              <el-button type="primary" @click='onSubmit()'>确定</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
         <el-dialog title="图文状态" v-model="status.show" >
            <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px">
              <el-row align="center" style="margin-left: 250px">
                <el-button
                        type="warning"
                        @click='inclusionException()'>列为异常
                </el-button>
                <el-button
                        type="primary"
                        @click='undoException()'>解除异常
                </el-button>
                <el-button
                        type="danger"
                        @click='statusReview()'>待审核
                </el-button>
              </el-row>
            </el-form>
          </el-dialog>
  </div>
</template>
<script>
  import ArticleJs from './article.js'

  export default ArticleJs
</script>
<style scoped lang='less'>
  .demo-form-inline {
    display: inline-block;
    float: right;
  }

</style>
