<template>
  <div class="list">
    <el-col :span="24" class='actions-top'>
      <strong>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;昵称：{{nickname}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        兴趣爱好概况：{{interestLabel}}
      </strong>
    </el-col>
    <el-table border style="width: 100%" align='center'
    :data="userInterest">
      <el-table-column
              :prop="fields.id.info.prop"
              :label="fields.id.info.label"
              :align="fields.id.style.align"
              :width="fields.id.style.width"
              :sortable="fields.id.info.sortable"
              v-if=false>
      </el-table-column>
      <el-table-column
              :prop="fields.interestType.info.prop"
              :label="fields.interestType.info.label"
              :align="fields.interestType.style.align"
              :width="fields.interestType.style.width"
              :sortable="fields.interestType.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.interestDetail.info.prop"
              :label="fields.interestDetail.info.label"
              :align="fields.interestDetail.style.align"
              :width="fields.interestDetail.style.width"
              :sortable="fields.interestDetail.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.position.info.prop"
              :label="fields.position.info.label"
              :align="fields.position.style.align"
              :width="fields.position.style.width"
              :sortable="fields.position.info.sortable">
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
                  @click='onEditInterest(scope.row)'></el-button>
          <el-button
                  type="danger"
                  icon='delete'
                  size="mini"
                  @click='onDeleteInterest(scope.row)'></el-button>
        </template>
      </el-table-column>
    </el-table>
    <div align="center">
      <el-button
                type="primary"
                @click='onAddInterest'>增加
      </el-button>
    </div>
    <el-dialog title="用户兴趣爱好信息" v-model="dialog.show">
      <el-form style="margin:20px;width:60%;min-width:100%"
               label-width="150px"
               :model="dialog.userInterest_info" ref="editFrom">
        <el-col :span="14" align='left'>
          <el-form-item label="用户昵称：" prop='nickname' style='width: 600px'>
            <span v-html="nickname" style="width: 350px; display:inline-block;" ></span><span style="color: red;font-style:oblique;font-size: 3px;text-align: left;">不可修改</span>
          </el-form-item>
          <el-form-item label="信息ID：" prop='id' style="width:600px;">
            <span v-html="dialog.userInterest_info.id" style="width: 350px; display:inline-block;" ></span><span style="color: red;font-style:oblique;font-size: 3px;text-align: left;">不可修改</span>
          </el-form-item>
          <el-form-item label="兴趣类别：" prop='company' style="width:600px;">
            <el-input v-model="dialog.userInterest_info.interestType" ></el-input>
          </el-form-item>
          <el-form-item label="具体内容：" prop='department' style="width:600px;">
            <el-input v-model="dialog.userInterest_info.interestDetail" ></el-input>
          </el-form-item>
          <el-form-item label="位置序号：" prop='position' style="width:600px;">
            <el-input v-model="dialog.userInterest_info.position" ></el-input>
          </el-form-item>
          <el-form-item >
          <el-button type="primary" @click='editUserHobbyInfo'>修改</el-button>
          <el-button @click="onEditUser2('editFrom')">重置</el-button>
        </el-form-item>
        </el-col>
      </el-form>
    </el-dialog>
    <el-dialog title="增加用户兴趣爱好信息" v-model="dialogTwo.show">
      <el-form style="margin:20px;width:60%;min-width:100%"
               label-width="150px"
               :model="dialogTwo.userInterest_info">
        <el-col :span="14" align='center'>
          <el-form-item label="用户昵称：" prop='nickname' style=''>
            <el-input v-model="nickname" readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="兴趣类别：" prop='company' style="width:600px;">
            <el-input v-model="dialogTwo.userInterest_info.interestType"></el-input>
          </el-form-item>
          <el-form-item label="具体内容：" prop='department' style="width:600px;">
            <el-input v-model="dialogTwo.userInterest_info.interestDetail"></el-input>
          </el-form-item>
          <el-form-item >
            <el-button type="primary" @click='onSubmit'>提交</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-dialog>
  
  </div>
</template>
<script type="text/javascript">
  import InterestJs from './interest.js'

  export default InterestJs
</script>
<style scoped lang='less'>
  .actions-top {
    height: 46px;
  }
</style>
