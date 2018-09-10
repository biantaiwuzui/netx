<template>
    <div class="List">
        <el-col>
            <strong>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共有{{paginations.total}}条心愿提现申请
            </strong>
        </el-col>
        <el-form :inline="true" :model="selectFormData" class="demo-form-inline">
            <el-form-item>
                <el-input placeholder="用户网号" v-model="selectFormData.userNumber"></el-input>
            </el-form-item>
            <el-select placeholder="提现状态" v-model="selectFormData.status">
                <el-option v-for="item in statusItems" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-form-item>
                <el-button type="primary" @click="onSubmitForm">查询</el-button>
            </el-form-item>
        </el-form>
        <el-table border style="width: 100%;" align='center' :data="selectData">
            <el-table-column
                    :prop="fields.id.info.prop"
                    :label="fields.id.info.label"
                    :width="fields.id.style.width"
                    :align="fields.id.style.align"
                    :sortable="fields.id.info.sortable" v-if="false">
            </el-table-column>
            <el-table-column
                    :prop="fields.wishApplyId.info.prop"
                    :label="fields.wishApplyId.info.label"
                    :width="fields.wishApplyId.style.width"
                    :align="fields.wishApplyId.style.align"
                    :sortable="fields.wishApplyId.info.sortable" v-if="false">
            </el-table-column>
            <el-table-column
                    :prop="fields.userNumber.info.prop"
                    :label="fields.userNumber.info.label"
                    :width="fields.userNumber.style.width"
                    :align="fields.userNumber.style.align"
                    :sortable="fields.userNumber.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.nickname.info.prop"
                    :label="fields.nickname.info.label"
                    :width="fields.nickname.style.width"
                    :align="fields.nickname.style.align"
                    :sortable="fields.nickname.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.accountName.info.prop"
                    :label="fields.accountName.info.label"
                    :width="fields.accountName.style.width"
                    :align="fields.accountName.style.align"
                    :sortable="fields.accountName.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.depositBank.info.prop"
                    :label="fields.depositBank.info.label"
                    :width="fields.depositBank.style.width"
                    :align="fields.depositBank.style.align"
                    :sortable="fields.depositBank.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.account.info.prop"
                    :label="fields.account.info.label"
                    :width="fields.account.style.width"
                    :align="fields.account.style.align"
                    :sortable="fields.account.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.mobile.info.prop"
                    :label="fields.mobile.info.label"
                    :width="fields.mobile.style.width"
                    :align="fields.mobile.style.align"
                    :sortable="fields.mobile.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.amount.info.prop"
                    :label="fields.amount.info.label"
                    :width="fields.amount.style.width"
                    :align="fields.amount.style.align"
                    :sortable="fields.amount.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.status.info.prop"
                    :label="fields.status.info.label"
                    :width="fields.status.style.width"
                    :align="fields.status.style.align"
                    :sortable="fields.status.info.sortable"
                    :formatter="fields.status.info.formatter">
            </el-table-column>
            <el-table-column
                    :prop="fields.updateTime.info.prop"
                    :label="fields.updateTime.info.label"
                    :width="fields.updateTime.style.width"
                    :align="fields.updateTime.style.align"
                    :sortable="fields.updateTime.info.sortable"
                    :formatter="dateFormat">
            </el-table-column>
            <el-table-column
                    label="操作"
                    style="width:10%"
                    align="center"
                    :context="_self">
                <template scope='scope'>
                    <el-button
                            type="info"
                            icon='edit'
                            size="mini"
                            @click='successRefuse(scope.row)' v-if="scope.row.status == 1">提现成功</el-button>
                    <el-button
                            type="warning"
                            icon='edit'
                            size="mini"
                            @click='failRefuse(scope.row)' v-if="scope.row.status == 1">提现失败</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-col :span="24" class="btm-action">
            <el-pagination
                    v-if="paginations.total>0"
                    class="pagination"
                    :page-size="paginations.page_size"
                    :page-sizes="paginations.page_sizes"
                    :current-page="paginations.current_page"
                    :total="paginations.total"
                    :layout="paginations.layout"
                    @current-change="onChangeCurrentPage"
                    @size-change="onChangePageSize">
            </el-pagination>
        </el-col>
        <el-dialog class="el-dialog__title" title="确认提现成功了吗？" align="center" v-model="successDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="successDialog" ref='refForm'>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="id" v-model="successDialog.show_info.id"></el-input>
                </el-form-item>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="wishApplyId" v-model="successDialog.show_info.wishApplyId"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="successSubmit()">确认</el-button> &nbsp;&nbsp;&nbsp;<el-button @click="successRefuseReset('refForm')">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog title="提现失败" align="center" v-model="failDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="failDialog" :rules="fail_rules" ref='refForm'>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="id" v-model="failDialog.show_info.id"></el-input>
                </el-form-item>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="wishApplyId" v-model="failDialog.show_info.wishApplyId"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入提现失败的理由" prop="reason" v-model="failDialog.onSelectDataInfo.reason"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="failRefuseReset('refForm')">重置</el-button> &nbsp;&nbsp;&nbsp; <el-button @click="failSubmit()">确认</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>
<script>
    import ListJs from './wish.js'

    export default ListJs
</script>
<style scoped type="less">
    .List{
        width: 94%;
        align:center;
        margin:0;
    }
    .demo-form-inline {
        display: inline-block;
        float: right;
    }
    .btm-action {
        margin-top: 20px;
        text-align: center;
    }
    .el-dialog__title {
        font-size: 26px;
    }

</style>

