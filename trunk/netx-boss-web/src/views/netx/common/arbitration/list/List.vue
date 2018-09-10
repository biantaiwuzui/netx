<template>
    <div class="List">
        <el-col>
            <strong>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;此处共有{{total}}个仲裁项
            </strong>
        </el-col>
        <el-form :inline="true" :model="selectFormData" class="demo-form-inline">
            <el-form-item>
                <el-input placeholder="用户昵称" v-model="selectFormData.nickname"></el-input>
            </el-form-item>
            <el-select placeholder="仲裁状态" v-model="selectFormData.statusCode">
                <el-option v-for="item in statusCodeItems" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-select placeholder="仲裁类型" v-model="selectFormData.type">
                <el-option v-for="item in typeItems" :label="item.name" :value="item.id"></el-option>
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
                :prop="fields.fromNickname.info.prop"
                :label="fields.fromNickname.info.label"
                :width="fields.fromNickname.style.width"
                :align="fields.fromNickname.style.align"
                :sortable="fields.fromNickname.info.sortable">
            </el-table-column>
            <el-table-column
                :prop="fields.toNickname.info.prop"
                :label="fields.toNickname.info.label"
                :width="fields.toNickname.style.width"
                :align="fields.toNickname.style.align"
                :sortable="fields.toNickname.info.sortable">
            </el-table-column>
            <el-table-column
                :prop="fields.statusCode.info.prop"
                :label="fields.statusCode.info.label"
                :width="fields.statusCode.style.width"
                :align="fields.statusCode.style.align"
                :sortable="fields.statusCode.info.sortable"
                :formatter="fields.statusCode.info.formatter">
            </el-table-column>
            <el-table-column
                :prop="fields.typeName.info.prop"
                :label="fields.typeName.info.label"
                :width="fields.typeName.style.width"
                :align="fields.typeName.style.align"
                :sortable="fields.typeName.info.sortable"
                :formatter="fields.typeName.info.formatter">
            </el-table-column>
            <el-table-column
                :prop="fields.typeId.info.prop"
                :label="fields.typeId.info.label"
                :width="fields.typeId.style.width"
                :align="fields.typeId.style.align"
                :sortable="fields.typeId.info.sortable" v-if="false">
            </el-table-column>
            <el-table-column
                    :prop="fields.theme.info.prop"
                    :label="fields.theme.info.label"
                    :width="fields.theme.style.width"
                    :align="fields.theme.style.align"
                    :sortable="fields.theme.info.sortable">
            </el-table-column>
             <el-table-column
                :prop="fields.reason.info.prop"
                :label="fields.reason.info.label"
                :width="fields.reason.style.width"
                :align="fields.reason.style.align"
                :sortable="fields.reason.info.sortable">
            </el-table-column>
            <el-table-column
                :prop="fields.fromUserId.info.prop"
                :label="fields.fromUserId.info.label"
                :width="fields.fromUserId.style.width"
                :align="fields.fromUserId.style.align"
                :sortable="fields.fromUserId.info.sortable" v-if="false">
            </el-table-column>
            <el-table-column
                :prop="fields.toUserId.info.prop"
                :label="fields.toUserId.info.label"
                :width="fields.toUserId.style.width"
                :align="fields.toUserId.style.align"
                :sortable="fields.toUserId.info.sortable" v-if="false">
            </el-table-column>
            <el-table-column
                :prop="fields.createTime.info.prop"
                :label="fields.createTime.info.label"
                :width="fields.createTime.style.width"
                :align="fields.createTime.style.align"
                :sortable="fields.createTime.info.sortable"
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
                            icon='view'
                            size="mini"
                            @click='onAccept(scope.row)'>去处理</el-button>
                    <el-button
                            type="info"
                            icon='edit'
                            size="mini"
                            @click='onRefuse(scope.row)'>拒绝受理</el-button>
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
        <el-dialog title="拒绝受理" v-model="refuseDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="refuseDialog" :rules="refuse_rules" ref='refForm'>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="id" v-model="refuseDialog.show_info.id"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入拒绝受理的理由" prop="reason" v-model="refuseDialog.onSelectDataInfo.reason"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="onRefuseReset('refForm')">重置</el-button> &nbsp;&nbsp;&nbsp; <el-button @click="refuseSubmit()">提交</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>
<script>
    import ListJs from './List.js'

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

</style>

