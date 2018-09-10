<template>
  <div class="list">
    <el-col :span="24" class='actions-top'>
      <strong>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;昵称：{{nickname}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        职业概况：{{professionLabel}}
      </strong>
    </el-col>
    <el-table border style="width: 100%" align='center'
    :data="userProfession">
      <el-table-column
              :prop="fields.id.info.prop"
              :label="fields.id.info.label"
              :align="fields.id.style.align"
              :width="fields.id.style.width"
              :sortable="fields.id.info.sortable"
              v-if=false>
      </el-table-column>
      <el-table-column
              :prop="fields.company.info.prop"
              :label="fields.company.info.label"
              :align="fields.company.style.align"
              :width="fields.company.style.width"
              :sortable="fields.company.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.department.info.prop"
              :label="fields.department.info.label"
              :align="fields.department.style.align"
              :width="fields.department.style.width"
              :sortable="fields.department.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.topProfession.info.prop"
              :label="fields.topProfession.info.label"
              :align="fields.topProfession.style.align"
              :width="fields.topProfession.style.width"
              :sortable="fields.topProfession.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.year.info.prop"
              :label="fields.year.info.label"
              :align="fields.year.style.align"
              :width="fields.year.style.width"
              :sortable="fields.year.info.sortable">
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
                  @click='onEditUser(scope.row)'></el-button>
          <el-button
                  type="danger"
                  icon='delete'
                  size="mini"
                  @click='onDeleteUser(scope.row)'></el-button>
        </template>
      </el-table-column>
    </el-table>
    <div align="center">
      <el-button
                type="primary"
                @click='onAddProfession'>增加
      </el-button>
    </div>
    <el-dialog title="用户工作经验信息" v-model="dialog.show">
            <el-form style="margin:20px;width:60%;min-width:100%"
                     label-width="150px"
                     :model="dialog.userProfession_info"
                      ref="editFrom">
              <el-col :span="14" align='left'>
                <el-form-item label="信息ID：" prop='id' style="width:600px;">
                  <span v-html="dialog.userProfession_info.id" style="width: 350px; display:inline-block;" ></span><span style="color: red;font-style:oblique;font-size: 3px;text-align: left;">不可修改</span>
                </el-form-item>
                <el-form-item label="用户昵称：" prop='nickname' style='width: 600px'>
                  <span v-html="nickname" style="width: 350px;display:inline-block;" ></span><span style="color: red;font-style:oblique;font-size: 3px;text-align: left;">不可修改</span>
                </el-form-item>
                <el-form-item label="位置序号：" prop='position' style="width:600px;">
                  <span v-html="dialog.userProfession_info.position"  style="width: 350px;display:inline-block;"></span><span style="color: red;font-style:oblique;font-size: 3px;text-align: left;">不可修改</span>
                </el-form-item>
                <el-form-item label="单位全称：" prop='company' style="width:600px;">
                  <el-input v-model="dialog.userProfession_info.company" style="width: 400px;"></el-input>
                </el-form-item>
                <el-form-item label="部门：" prop='department' style="width:600px;">
                  <el-input v-model="dialog.userProfession_info.department" style="width: 400px;"></el-input>
                </el-form-item>
                <el-form-item label="最高职位：" prop='topProfession' style="width:600px;">
                  <el-input v-model="dialog.userProfession_info.topProfession" style="width: 400px;"></el-input>
                </el-form-item>
                <el-form-item label="入职年份：" prop='year' style="width:600px;">
                  <el-input v-model="dialog.userProfession_info.year" style="width: 400px;"></el-input>
                </el-form-item>
                <el-form-item >
                  <el-button type="primary" @click='editUserWorkExperience'>修改</el-button>
                  <el-button @click="onEditUser1('editFrom')">重置</el-button>
                </el-form-item>
        </el-col>
      </el-form>
    </el-dialog>
    <el-dialog title="增加用户工作经验信息" v-model="dialog2.show">
      <el-form style="margin:20px;width:60%;min-width:100%"
               label-width="150px"
               :model="dialog2.userProfession_info">
        <el-col :span="14" align='center'>
          <el-form-item label="用户昵称：" prop='nickname' style=''>
            <el-input v-model="nickname" readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="单位全称：" prop='company' style="width:600px;">
            <el-input v-model="dialog2.userProfession_info.company"></el-input>
          </el-form-item>
          <el-form-item label="部门：" prop='department' style="width:600px;">
            <el-input v-model="dialog2.userProfession_info.department"></el-input>
          </el-form-item>
          <el-form-item label="最高职位：" prop='topProfession' style="width:600px;">
            <el-input v-model="dialog2.userProfession_info.topProfession"></el-input>
          </el-form-item>
          <el-form-item label="入职年份：" prop='year' style="width:600px;">
            <el-input v-model="dialog2.userProfession_info.year"></el-input>
          </el-form-item>
          <el-form-item >
            <el-button type="primary" @click='onSubmit'>提交</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
  import ProfessionJs from './profession.js'

  export default ProfessionJs
</script>
<style scoped lang='less'>
  .actions-top {
    height: 46px;
  }
</style>
