<template>
  <div class="list">
        <el-col :span="24" class='actions-top'>
                <el-form :inline="true" class="demo-form-inline">
                        <el-form-item>
                        <el-input placeholder="商家" v-model='name'></el-input>
                        </el-form-item>
                        <el-form-item>
                        <el-button type="primary" @click='onSelectData'>查询</el-button>
                        </el-form-item>
                </el-form>
         </el-col>
        <list-data
                ref='user-sellerWhite'
                @onChangeCurrentPage="onChangeCurPage"
                @onChangePageSize="onChangeCurPageSize"
                @onClickBtnAdd="onClickBtnAdd"
                :Pagination='pagination'
                :BtnInfo="btn_info.condition"
                :List='list'
                :FieldList='fields'>
        </list-data>
      <el-dialog title="拉黑原因" v-model="backDialog.show" >
                <el-form style="margin:20px;width:60%;min-width:100%"
                label-width="150px">
                        <el-form-item label="拉黑原因" prop='value' style="width:600px;">
                                <el-input v-model="defriendData.backReason"></el-input>
                        </el-form-item>
                        <el-form-item >
                                <el-button type="primary" @click='backReasonSubmit()'>确定</el-button>
                        </el-form-item>
                </el-form>
        </el-dialog>
  </div>
</template>
<script>
  import SellerWhiteJs from './sellerWhite.js'

  export default SellerWhiteJs
</script>
<style scoped lang='less'>
  .demo-form-inline {
    display: inline-block;
    float: right;
  }
</style>
