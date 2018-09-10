<template>
  <div class="list">
    <el-col :span="24" class='actions-top'>
    </el-col>
    <div>        
        <el-form :inline="true" class="demo-form-inline">
            <el-select placeholder="状态" v-model=selectFormData>
                <el-option v-for="item in typeItems" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-form-item>
                <el-button type="primary" @click="onSubmitForm">查询</el-button>
            </el-form-item>
            <el-button type="primary" @click="add">新增设置</el-button>
        </el-form>
    </div>
    <el-table border style="width: 100%" align='center'
              :data="luckyMoney">
      <el-table-column
              :prop="fields.id.info.prop"
              :label="fields.id.info.label"
              :align="fields.id.style.align"
              :width="fields.id.style.width"
              :sortable="fields.id.info.sortable"
              v-if=false>
      </el-table-column>
      <el-table-column
              :prop="fields.sendTime.info.prop"
              :label="fields.sendTime.info.label"
              :align="fields.sendTime.style.align"
              :width="fields.sendTime.style.width"
              :sortable="fields.sendTime.info.sortable"
              >
      </el-table-column>
      <el-table-column
              :prop="fields.sendPeople.info.prop"
              :label="fields.sendPeople.info.label"
              :align="fields.sendPeople.style.align"
              :width="fields.sendPeople.style.width"
              :sortable="fields.sendPeople.info.sortable">
      </el-table-column>
      <el-table-column
              :prop="fields.sendCount.info.prop"
              :label="fields.sendCount.info.label"
              :align="fields.sendCount.style.align"
              :width="fields.sendCount.style.width"
              :sortable="fields.sendCount.info.sortable">
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
              :prop="fields.status.info.prop"
              :label="fields.status.info.label"
              :align="fields.status.style.align"
              :width="fields.status.style.width"
              :sortable="fields.status.info.sortable"
              :formatter="stautsFormat">
      </el-table-column>
    </el-table>
    <div v-if="status==2" align='center'><el-button type="primary" @click="agree">通过审核</el-button>
    <el-button type="primary" @click="disagree">拒绝通过</el-button></div>
    <div v-if="status==3" align='center'><el-button type="primary" @click="update">立刻生效</el-button></div>
    
  </div>
</template>

<script>
    import LuckyMoneyJs from './luckyMoney.js'

    export default LuckyMoneyJs
</script>
<style scoped lang='less'>

  .btm-action {
    margin-top: 20px;
    text-align: center;
  }
  .demo-form-inline {
    display: inline-block;
    float: right;
  }
  .actions-top {
    height: 46px;
  }

</style>
