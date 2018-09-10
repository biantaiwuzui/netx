<template>
  <div class="form" align='center'>
    <el-form label-width="100px" style="margin:20px;width:90%;min-width:600px;" :model="list" >
        <el-form-item >
          第一波红包时间：
        <el-time-select
            v-model="list.sendTime1"
            :picker-options="{
              start: '00:00',
              step: '00:30',
              end: '24:00'
            }"
            placeholder="选择时间">
        </el-time-select>
        </el-form-item>
        <el-form-item >
          发放的比例：
        <template>
          <el-input-number v-model="list.sendCount1" :min="1" :max="100" label="描述文字"></el-input-number>
        </template>
        </el-form-item>
        <el-form-item >
          人数的比例：
        <template>
          <el-input-number v-model="list.sendPeople1" :min="1" :max="100" label="描述文字"></el-input-number>
        </template>
        </el-form-item>
        <el-form-item >
          第二波红包时间：
        <el-time-select
            v-model="list.sendTime2"
            :picker-options="{
              start: '00:00',
              step: '00:30',
              end: '24:00'
            }"
            placeholder="选择时间">
        </el-time-select>
        </el-form-item>
        <el-form-item >
          发放的比例：
        <template>
          <el-input-number v-model="list.sendCount2" :min="1" :max="100" label="描述文字"></el-input-number>
        </template>
        </el-form-item>
        <el-form-item >
          人数的比例：
        <template>
          <el-input-number v-model="list.sendPeople2" :min="1" :max="100" label="描述文字"></el-input-number>
        </template>
        </el-form-item>
        <el-form-item >
          第三波红包时间：
        <el-time-select
            v-model="list.sendTime3"
            :picker-options="{
              start: '00:00',
              step: '00:30',
              end: '24:00'
            }"
            placeholder="选择时间">
        </el-time-select>
        </el-form-item>
        <el-form-item >
          发放的比例：
        <template>
          <el-input-number v-model="list.sendCount3" :min="1" :max="100" label="描述文字"></el-input-number>
        </template>
        </el-form-item>
        <el-form-item >
          人数的比例：
        <template>
          <el-input-number v-model="list.sendPeople3" :min="1" :max="100" label="描述文字"></el-input-number>
        </template>
        </el-form-item>
        <el-form-item >
          第四波红包时间：
        <el-time-select
            v-model="list.sendTime4"
            :picker-options="{
              start: '00:00',
              step: '00:30',
              end: '24:00'
            }"
            placeholder="选择时间">
        </el-time-select>
        </el-form-item>
        <el-form-item >
          发放的比例：
        <template>
          <el-input-number v-model="list.sendCount4" :min="1" :max="100" label="描述文字"></el-input-number>
        </template>
        </el-form-item>
        <el-form-item >
          人数的比例：
        <template>
          <el-input-number v-model="list.sendPeople4" :min="1" :max="100" label="描述文字"></el-input-number>
        </template>
        </el-form-item>
        <el-form-item >
          第五波红包时间：
        <el-time-select
            v-model="list.sendTime5"
            :picker-options="{
              start: '00:00',
              step: '00:30',
              end: '24:00'
            }"
            placeholder="选择时间">
        </el-time-select>
        </el-form-item>
        <el-form-item >
          发放的比例：
        <template>
          <el-input-number v-model="list.sendCount5" :min="1" :max="100" label="描述文字"></el-input-number>
        </template>
        </el-form-item>
        <el-form-item >
          人数的比例：
        <template>
          <el-input-number v-model="list.sendPeople5" :min="1" :max="100" label="描述文字"></el-input-number>
        </template>
        </el-form-item>

        <el-form-item >
          <el-button type="primary" @click='onSubmit'>提交</el-button>
        </el-form-item>
    </el-form>
  </div>
</template>

<script>
    import AddLuckyMoneyJs from './addLuckyMoney.js'

    export default AddLuckyMoneyJs
</script>
<style scoped>

</style>
