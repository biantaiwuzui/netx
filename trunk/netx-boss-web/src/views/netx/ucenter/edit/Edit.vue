<template>
  <div class="form">
    <el-form label-width="100px" style="margin:20px;width:90%;min-width:600px;"
             :model="form_data"
             :rules="form_rules"
             ref='refForm'>
      <el-form-item label="用户ID：" prop='id' style="width:600px;" v-if=false>
        <el-input v-model="form_data.id"></el-input>
      </el-form-item>
      <el-form-item label="用户昵称：" prop='nickname' style="width:600px;">
        <el-input v-model="form_data.nickname"></el-input>
      </el-form-item>
      <el-form-item label="用户网号：" prop='userNumber' style="width:600px;">
        <el-input v-model="form_data.userNumber"></el-input>
      </el-form-item>
      <el-form-item label="姓名：" prop='realName' style="width:600px;">
        <el-input v-model="form_data.realName"></el-input>
      </el-form-item>
      <el-form-item label="性别：" prop='sex' style="width:600px;">
        <el-radio v-model="form_data.sex" label="1">男</el-radio>
        <el-radio v-model="form_data.sex" label="0">女</el-radio>
      </el-form-item>
      <el-form-item label="是否登录：" prop='login' style="width:600px;">
        <el-radio v-model="form_data.login" label="1">是</el-radio>
        <el-radio v-model="form_data.login" label="0">否</el-radio>
      </el-form-item>
      <el-form-item label="是否管理员：" prop='adminUser' style="width:600px;">
        <el-radio v-model="form_data.adminUser" label="1">是</el-radio>
        <el-radio v-model="form_data.adminUser" label="0">否</el-radio>
      </el-form-item>
      <el-form-item label="手机号：" prop='mobile' style="width:600px;">
        <el-input v-model="form_data.mobile"></el-input>
      </el-form-item>
      <el-form-item label="角色：" prop='role' style="width:600px;">
        <el-input v-model="form_data.role"></el-input>
      </el-form-item>
      <el-form-item label="教育概述：" prop='educationLabel' style="width:600px;">
        <el-input v-model="form_data.educationLabel"></el-input>
      </el-form-item>
      <el-form-item label="工作概述：" prop='professionLabel' style="width:600px;">
        <el-input v-model="form_data.professionLabel"></el-input>
      </el-form-item>
      <el-form-item label="兴趣概述：" prop='interestLabel' style="width:600px;">
        <el-input v-model="form_data.interestLabel"></el-input>
      </el-form-item>
      <el-form-item label="用户等级：" prop='lv' style="width:600px;">
        <el-input v-model="form_data.lv"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click='onSubmit("refForm")'>立即{{form_data.id ? '修改' : '添加'}}</el-button>
        <el-button @click='reset_form("form_data")'>重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
  import EditJs from './Edit.js'

  export default EditJs
</script>
<style scoped>

</style>
