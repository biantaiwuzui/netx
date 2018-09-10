<template>
  <div class="list">
    <el-col :span="24" class='actions-top'>
      <el-form :inline="true" :model='selectData' class="demo-form-inline">
        <el-form-item>
          <el-input placeholder="昵称" v-model='selectData.nickname'></el-input>
        </el-form-item>
        <el-form-item>
          <el-input placeholder="网号" v-model='selectData.userNumber'></el-input>
        </el-form-item>
        <el-form-item>
          <el-input placeholder="手机号" v-model='selectData.mobile'></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click='onSelectData'>查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <list-data
      ref='list-data'
      @onClickBtnDelete="onClickBtnDelete"
      @onClickBtnBatchDelete="onClickBtnBatchDelete"
      @onClickBtnUpdate="onClickBtnUpdate"
      @onClickBtnSelect="onClickBtnSelect"
      @onChangeCurrentPage="onChangeCurPage"
      @onChangePageSize="onChangePageSize"
      :List='userList'
      :Pagination="paginations"
      :BtnInfo="btn_info"
      :FieldList='fields'>
    </list-data>

    <el-dialog title="用户信息" v-model="dialog.show" >
      <el-form style="margin:20px;width:60%;min-width:100%"
               label-width="150px"
               :model="dialog.user_info">
        <el-row align="center" style="margin-left: 250px">
          <el-button
                  type="primary"
                  @click='selectUserDetail(dialog.user_info.id,dialog.user_info.nickname)'>用户详细
          </el-button>
          <el-button
                  type="primary"
                  @click='selectUserEducation(dialog.user_info.id,dialog.user_info.nickname)'>教育背景
          </el-button>
        </el-row>
        <el-row align="center" style="margin-left: 250px;margin-top: 30px">
          <el-button
                  type="primary"
                  @click='selectUserProfession(dialog.user_info.id,dialog.user_info.nickname)'>工作经历
          </el-button>
          <el-button
                  type="primary"
                  @click='selectUserInterest(dialog.user_info.id,dialog.user_info.nickname)'>兴趣爱好
          </el-button>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  import ListJs from './List.js'

  export default ListJs
</script>
<style scoped lang='less'>
  .demo-form-inline {
    display: inline-block;
    float: right;
  }

  .btm-action {
    margin-top: 20px;
    text-align: center;
  }

  .actions-top {
    height: 46px;
  }

  .pagination {
    display: inline-block;
  }
  .edit-form {
    width: 500px;
  }
  .form .form-left {
    width:50%; padding-left:1rem; padding-right:1rem; padding-top:1rem;
  }
  .form .form-right {
    width:50%; padding-left:1rem; padding-right:1rem; padding-top:1rem;
  }
</style>
