<template>
  <div class="list">
    <el-col :span="24" class='actions-top'>
      <strong>
        &nbsp;&nbsp;&nbsp;&nbsp;昵称：{{ nickname }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        文化教育概况：{{ educationLabel }}
      </strong>
    </el-col>
    <el-table border style="width: 100%" align='center'
    :data="userEducation">
      <el-table-column
              :prop="fields.id.info.prop"
              :label="fields.id.info.label"
              :align="fields.id.style.align"
              :width="fields.id.style.width"
              :sortable="fields.id.info.sortable"
              v-if=false>
      </el-table-column>
      <el-table-column
              :prop="fields.school.info.prop"
              :label="fields.school.info.label"
              :align="fields.school.style.align"
              :width="fields.school.style.width"
              :sortable="fields.school.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.department.info.prop"
              :label="fields.department.info.label"
              :align="fields.department.style.align"
              :width="fields.department.style.width"
              :sortable="fields.department.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.speciality.info.prop"
              :label="fields.speciality.info.label"
              :align="fields.speciality.style.align"
              :width="fields.speciality.style.width"
              :sortable="fields.speciality.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.year.info.prop"
              :label="fields.year.info.label"
              :align="fields.year.style.align"
              :width="fields.year.style.width"
              :sortable="fields.year.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.time.info.prop"
              :label="fields.time.info.label"
              :align="fields.time.style.align"
              :width="fields.time.style.width"
              :sortable="fields.time.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.degree.info.prop"
              :label="fields.degree.info.label"
              :align="fields.degree.style.align"
              :width="fields.degree.style.width"
              :sortable="fields.degree.info.sortable">
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
              :context="_self">
        <template scope='scope'>
          <el-button
                  type="info"
                  icon='view'
                  size="mini"
                  @click='onSelectUser(scope.row)'></el-button>
          <el-button
                  type="info"
                  icon='edit'
                  size="mini"
                  @click='onSelectUser(scope.row)'></el-button>
          <el-button
                  type="danger"
                  icon='delete'
                  size="mini"
                  @click='onDeleteUserEducation(scope.row)'></el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="用户兴趣爱好信息" v-model="dialog.show">
      <el-form style="margin:20px;width:60%;min-width:100%"
               label-width="150px"
               :model="dialog.userEducation_info" ref="editFrom">
        <el-col :span="14" align='left'>
          <el-form-item label="用户昵称：" prop='nickname' style='width: 600px'>
            <span v-html="nickname" style="width: 350px; display:inline-block;" ></span><span style="color: red;font-style:oblique;font-size: 3px;text-align: left;">不可修改</span>
          </el-form-item>
          <el-form-item label="信息ID：" prop='id' style="width:600px;">
            <span v-html="dialog.userEducation_info.id" style="width: 350px; display:inline-block;" ></span><span style="color: red;font-style:oblique;font-size: 3px;text-align: left;">不可修改</span>
          </el-form-item>
          <el-form-item label="学校：" prop='school' style="width:600px;">
            <el-input v-model="dialog.userEducation_info.school"></el-input>
          </el-form-item>
          <el-form-item label="院系名称：" prop='department' style="width:600px;">
            <el-input v-model="dialog.userEducation_info.department"></el-input>
          </el-form-item>
          <el-form-item label="专业名称：" prop='speciality' style="width:600px;">
            <el-input v-model="dialog.userEducation_info.speciality"></el-input>
          </el-form-item>
          <el-form-item label="入学年份：" prop='year' style="width:600px;">
            <el-input v-model="dialog.userEducation_info.year"></el-input>
          </el-form-item>
          <el-form-item label="学习年限：" prop='time' style="width:600px;">
            <el-input v-model="dialog.userEducation_info.time"></el-input>
          </el-form-item>
          <el-form-item label="所获学位：" prop='degree' style="width:600px;">
            <el-input v-model="dialog.userEducation_info.degree"></el-input>
          </el-form-item>
          <el-form-item label="位置序号：" prop='position' style="width:600px;">
            <el-input v-model="dialog.userEducation_info.position"></el-input>
          </el-form-item>
          <el-form-item >
            <el-button type="primary" @click='editUserEducationInfo'>修改</el-button>
            <el-button @click="onEditUser('editFrom')">重置</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
  import EducationJs from './education.js'

  export default EducationJs
</script>
<style scoped lang='less'>
  .actions-top {
    height: 46px;
  }
</style>
