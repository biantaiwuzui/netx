<template>
  <div class="form">
    <el-form label-width="100px" style="margin:20px;width:90%;min-width:600px;"
             :model="userProfile"
             ref='refForm'>
        <el-form-item label="用户ID：" prop='userId' style="width:600px;" v-if=false>
          <el-input v-model="userProfile.userId"></el-input>
        </el-form-item>
        <el-form-item label="用户昵称：" prop='nickname' style="width:600px;">
          <span v-html="nickname"></span>
        </el-form-item>
        <el-form-item label="家乡：" prop='homeTown' style="width:600px;">
          <el-input v-model="userProfile.homeTown"></el-input>
        </el-form-item>
        <el-form-item label="常驻：" prop='oftenIn' style="width:600px;">
          <el-input v-model="userProfile.oftenIn"></el-input>
        </el-form-item>
        <el-form-item label="去过：" prop='alreadyTo' style="width:600px;">
          <el-input v-model="userProfile.alreadyTo"></el-input>
        </el-form-item>
        <el-form-item label="想去：" prop='wantTo' style="width:600px;">
          <el-input v-model="userProfile.wantTo"></el-input>
        </el-form-item>
        <el-form-item label="地址：" prop='address' style="width:600px;">
          <el-input v-model="userProfile.address"></el-input>
        </el-form-item>
        <el-form-item label="介绍：" prop='introduce' style="width:600px;">
          <el-input v-model="userProfile.introduce"></el-input>
        </el-form-item>
        <el-form-item label="性格：" prop='disposition' style="width:600px;">
          <el-input v-model="userProfile.disposition"></el-input>
        </el-form-item>
        <el-form-item label="外貌：" prop='appearance' style="width:600px;">
          <el-input v-model="userProfile.appearance"></el-input>
        </el-form-item>
        <el-form-item label="收入：" prop='income' style="width:600px;">
          <el-input v-model="userProfile.income"></el-input>
        </el-form-item>
        <el-form-item label="最大工资：" prop='maxIncome' style="width:600px;">
          <el-input v-model="userProfile.maxIncome"></el-input>
        </el-form-item>
        <el-form-item label="情感：" prop='emotion' style="width:600px;">
          <el-input v-model="userProfile.emotion"></el-input>
        </el-form-item>
        <el-form-item label="身高：" prop='height' style="width:600px;">
          <el-input v-model="userProfile.height"></el-input>
        </el-form-item>
        <el-form-item label="体重：" prop='weight' style="width:600px;">
          <el-input v-model="userProfile.weight"></el-input>
        </el-form-item>
        <el-form-item label="民族：" prop='nation' style="width:600px;">
          <el-input v-model="userProfile.nation"></el-input>
        </el-form-item>
        <el-form-item label="属相：" prop='animalSigns' style="width:600px;">
          <el-input v-model="userProfile.animalSigns"></el-input>
        </el-form-item>
        <el-form-item label="星座：" prop='starSign' style="width:600px;">
          <el-input v-model="userProfile.starSign"></el-input>
        </el-form-item>
        <el-form-item label="血型：" prop='bloodType' style="width:600px;">
            <el-input v-model="userProfile.bloodType"></el-input>
        </el-form-item>
        <el-form-item >
            <el-button type="primary" @click='onSubmit'>修改</el-button>
            <el-button @click='fetchData'>重置</el-button>
        </el-form-item>
        <el-form-item label="照片详情：" prop='description' style="width:600px;">
            <!--<el-input v-model="userProfile.description"></el-input>-->
            <span v-html="userProfile.description"></span>
        </el-form-item>
    </el-form>
  </div>
</template>
<script>
  import DetailJs from './detail.js'

  export default DetailJs
</script>
<style scoped lang='less'>

</style>
