<template>
  <div class="list">
    <el-col :span="24" class='actions-top' align='right'>
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
          <el-select placeholder="认证类型" v-model="selectData.verifyType">
              <el-option v-for="item in verifyTypeItems" :label="item.name" :value="item.id"></el-option>
          </el-select>
        <el-select placeholder="认证状态" v-model="selectData.status">
          <el-option v-for="item in statusItems" :label="item.name" :value="item.id"></el-option>
        </el-select>
        <el-form-item>
          <el-button type="primary" @click='onSelectData'>查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <el-table border style="width: 100%" align='center'
              :data="verifyList">
      <el-table-column
              :prop="fields.userId.info.prop"
              :label="fields.userId.info.label"
              :align="fields.userId.style.align"
              :width="fields.userId.style.width"
              :sortable="fields.userId.info.sortable"
              v-if=false>
      </el-table-column>
      <el-table-column
              :prop="fields.nickName.info.prop"
              :label="fields.nickName.info.label"
              :align="fields.nickName.style.align"
              :width="fields.nickName.style.width"
              :sortable="fields.nickName.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.userNumber.info.prop"
              :label="fields.userNumber.info.label"
              :align="fields.userNumber.style.align"
              :width="fields.userNumber.style.width"
              :sortable="fields.userNumber.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.mobile.info.prop"
              :label="fields.mobile.info.label"
              :align="fields.mobile.style.align"
              :width="fields.mobile.style.width"
              :sortable="fields.mobile.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.lv.info.prop"
              :label="fields.lv.info.label"
              :align="fields.lv.style.align"
              :width="fields.lv.style.width"
              :sortable="fields.lv.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.sex.info.prop"
              :label="fields.sex.info.label"
              :align="fields.sex.style.align"
              :width="fields.sex.style.width"
              :sortable="fields.sex.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.age.info.prop"
              :label="fields.age.info.label"
              :align="fields.age.style.align"
              :width="fields.age.style.width"
              :sortable="fields.age.info.sortable">
      </el-table-column>

      <el-table-column
              :prop="fields.status.info.prop"
              :label="fields.status.info.label"
              :align="fields.status.style.align"
              :width="fields.status.style.width"
              :sortable="fields.status.info.sortable"
              :formatter="fields.status.info.formatter">
      </el-table-column>
      <el-table-column
              :prop="fields.verifyType.info.prop"
              :label="fields.verifyType.info.label"
              :align="fields.verifyType.style.align"
              :width="fields.verifyType.style.width"
              :sortable="fields.verifyType.info.sortable"
              :formatter="fields.verifyType.info.formatter">
      </el-table-column>
      <el-table-column
              :prop="fields.reason.info.prop"
              :label="fields.reason.info.label"
              :align="fields.reason.style.align"
              :width="fields.reason.style.width"
              :sortable="fields.reason.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.admin.info.prop"
              :label="fields.admin.info.label"
              :align="fields.admin.style.align"
              :width="fields.admin.style.width"
              :sortable="fields.admin.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.createTime.info.prop"
              :label="fields.createTime.info.label"
              :align="fields.createTime.style.align"
              :width="fields.createTime.style.width"
              :sortable="fields.createTime.info.sortable"
              :formatter="dateFormat">
      </el-table-column>
      <el-table-column
              label="操作"
              :width="140"
              :context="_self"
              align='center'>
        <template scope='scope'>
          <el-button
                  type="info"
                  icon='view'
                  size="mini"
                  @click='onEditUser(scope.row)' v-if="scope.row.status == 0"></el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-col :span="24" class='btm-action'>
      <el-pagination
              v-if='paginations.total>0'
              class='pagination'
              :page-sizes="paginations.page_sizes"
              :page-size="paginations.page_size"
              :layout="paginations.layout"
              :total="paginations.total"
              :current-page='paginations.current_page'
              @current-change='onChangeCurrentPage'
              @size-change='onChangePageSize'>
      </el-pagination>
    </el-col>
  </div>
</template>

<script>
    import VerifyJs from './verify.js'

    export default VerifyJs
</script>
<style scoped lang='less'>

  .btm-action {
    margin-top: 20px;
    text-align: center;
  }

  .actions-top {
    height: 46px;
  }

</style>
