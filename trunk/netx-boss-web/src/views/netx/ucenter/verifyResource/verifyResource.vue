<template>
  <div class="list">
    <el-col :span="24" class='actions-top'>
      <strong>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;昵称：{{nickName}}
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;网号：{{userNumber}}
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Lv{{lv}}
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{sex}}
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{age}}岁
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font v-if=name>真实姓名：{{name}}</font>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font v-if=idCar>身份证号：{{idCar}}</font>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <el-button
                type="primary"
                @click='agree'>通过验证
      </el-button>
      <el-button
                type="primary"
                @click='disagree'>拒接通过
      </el-button>
      </strong>
    </el-col>
    <div class="car" v-if=houseResource>
      <el-table style="width: 100%" align='center'
                :data="houseResource">
        <el-table-column
                prop="url"
                label="房产验证图片"
                sortable
                width="320">
          <template slot-scope="scope">
            <img  :src="scope.row.url" alt="" style="width: 320px;height: 150px" @click=bigImg(scope.row.url)>
          </template>
        </el-table-column>
      </el-table>
   </div> 
   <div class="idCar" v-if=idCardResource>
      <el-table style="width: 100%" align='center'
                :data="idCardResource">
        <el-table-column
                prop="url"
                label="身份验证图片"
                sortable
                width="320">
          <template slot-scope="scope">
            <img  :src="scope.row.url" alt="" style="width: 320px;height: auto" @click=bigImg(scope.row.url)>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="car" v-if=degreeResource>
      <el-table style="width: 100%" align='center'
                :data="degreeResource">
        <el-table-column
                prop="url"
                label="学历验证图片"
                sortable
                width="320">
          <template slot-scope="scope">
            <img  :src="scope.row.url" alt="" style="width: 320px;height: auto" @click=bigImg(scope.row.url)>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="car" v-if=carResource>
      <el-table style="width: 100%" align='center'
                :data="carResource">
        <el-table-column
                prop="url"
                label="车辆验证图片"
                sortable
                width="320">
          <template slot-scope="scope">
            <img  :src="scope.row.url" alt="" style="width: 320px;height: auto" @click=bigImg(scope.row.url)>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="通过认证" v-model="dialog.show">
      <form-data
      :FieldList='fields'
      :DefaultValue="default_value"
      @onSubmit='onSubmit'></form-data>
    </el-dialog>
    <el-dialog title="拒接通过" v-model="disagreedialog.show">
       <form-data
      :FieldList='disagreeFields'
      :DefaultValue="default_value"
      @onSubmit='disagreOnSubmit'></form-data>
    </el-dialog>
    <el-dialog v-model="bigImgdialog.show" style="top:20%">
      <img  :src=imgUrl alt="" style="width: 100%;height: auto">
    </el-dialog>
  </div>

</template>

<script>
    import VerifyResourceJs from './verifyResource.js'

    export default VerifyResourceJs
</script>
<style scoped lang='less'>
  .actions-top {
    height: 46px;
  }

  .idCar {
    float: left;
    width: 25%;
  }

  .car {
    float: left;
    width: 25%;
  }
</style>
