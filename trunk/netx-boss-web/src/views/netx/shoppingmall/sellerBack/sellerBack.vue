<template>
  <div class="list">
    <list-data
      ref='user-sellerWhite'
      @onChangeCurrentPage="onChangeCurPage"
      @onChangePageSize="onChangeCurPageSize"
      :Pagination='pagination'
      :BtnInfo="btn_info.condition"
      :List='list'
      :FieldList='fields'>
      </list-data>
      <el-dialog title="解除原因" v-model="overDialog.show" >
                        <el-form style="margin:20px;width:60%;min-width:100%"
                        label-width="150px">
                                <el-form-item label="解除原因" prop='value' style="width:600px;">
                                        <el-input v-model="overBackData.overReason"></el-input>
                                </el-form-item>
                                <el-form-item >
                                        <el-button type="primary" @click='overReasonSubmit()'>确定</el-button>
                                </el-form-item>
                        </el-form>
                </el-dialog>
  </div>
</template>
<script>
  import SellerBackJs from './sellerBack.js'

  export default SellerBackJs
</script>
<style scoped lang='less'>

</style>
