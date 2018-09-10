<template>
    <div class="List">
        <el-col>
            <strong>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共有{{paginations.total}}个管理员
            </strong>
        </el-col>
        <el-form :inline="true" :model="selectFormData" class="demo-form-inline">
            <el-form-item>
                <el-input placeholder="登录名" v-model="selectFormData.userName"></el-input>
            </el-form-item>
            <el-form-item>
                <el-input placeholder="姓名" v-model="selectFormData.realName"></el-input>
            </el-form-item>
            <el-select placeholder="状态" v-model="selectFormData.deleted">
                <el-option v-for="item in deletedItems" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-form-item>
                <el-button type="primary" @click="onSubmitForm">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addRefuse()">新增管理员</el-button>
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
                    :prop="fields.userName.info.prop"
                    :label="fields.userName.info.label"
                    :width="fields.userName.style.width"
                    :align="fields.userName.style.align"
                    :sortable="fields.userName.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.realName.info.prop"
                    :label="fields.realName.info.label"
                    :width="fields.realName.style.width"
                    :align="fields.realName.style.align"
                    :sortable="fields.realName.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.mobile.info.prop"
                    :label="fields.mobile.info.label"
                    :width="fields.mobile.style.width"
                    :align="fields.mobile.style.align"
                    :sortable="fields.mobile.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.superAdmin.info.prop"
                    :label="fields.superAdmin.info.label"
                    :width="fields.superAdmin.style.width"
                    :align="fields.superAdmin.style.align"
                    :sortable="fields.superAdmin.info.sortable"
                    :formatter="fields.superAdmin.info.formatter">
            </el-table-column>
            <el-table-column
                    :prop="fields.deleted.info.prop"
                    :label="fields.deleted.info.label"
                    :width="fields.deleted.style.width"
                    :align="fields.deleted.style.align"
                    :sortable="fields.deleted.info.sortable"
                    :formatter="fields.deleted.info.formatter">
            </el-table-column>
            <el-table-column
                    :prop="fields.reason.info.prop"
                    :label="fields.reason.info.label"
                    :width="fields.reason.style.width"
                    :align="fields.reason.style.align"
                    :sortable="fields.reason.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.createUserName.info.prop"
                    :label="fields.createUserName.info.label"
                    :width="fields.createUserName.style.width"
                    :align="fields.createUserName.style.align"
                    :sortable="fields.createUserName.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.updateUserName.info.prop"
                    :label="fields.updateUserName.info.label"
                    :width="fields.updateUserName.style.width"
                    :align="fields.updateUserName.style.align"
                    :sortable="fields.updateUserName.info.sortable">
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
                            icon='edit'
                            size="mini"
                            @click='upRefuse(scope.row)' v-if="scope.row.deleted == 1 && !scope.row.superAdmin">恢复</el-button>
                    <el-button
                            type="danger"
                            icon='edit'
                            size="mini"
                            @click='downRefuse(scope.row)' v-if="scope.row.deleted == 0 && !scope.row.superAdmin">禁用</el-button>
                    <el-button
                            type="warning"
                            icon='edit'
                            size="mini"
                            @click='resetRefuse(scope.row)' v-if="!scope.row.superAdmin">重置密码</el-button>
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
        <el-dialog title="禁用管理员账号" align="center" v-model="downDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="downDialog" :rules="down_rules" ref='refForm'>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="id" v-model="downDialog.show_info.id"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入禁用管理员账号的理由" prop="reason" v-model="downDialog.onSelectDataInfo.reason"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="downRefuseReset('refForm')">重置</el-button> &nbsp;&nbsp;&nbsp; <el-button @click="downSubmit()">禁用</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog title="恢复管理员账号" align="center" v-model="upDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="upDialog" :rules="up_rules" ref='refForm'>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="id" v-model="upDialog.show_info.id"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入恢复管理员账号的理由" prop="reason" v-model="upDialog.onSelectDataInfo.reason"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="upRefuseReset('refForm')">重置</el-button> &nbsp;&nbsp;&nbsp; <el-button @click="upSubmit()">恢复</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog title="添加管理员账号" v-model="addDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="addDialog" ref='refForm'>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入登录名" prop="userName" v-model="addDialog.onSelectDataInfo.userName"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入登录密码" prop="password" v-model="addDialog.onSelectDataInfo.password"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入真实姓名" prop="realName" v-model="addDialog.onSelectDataInfo.realName"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入手机号码" prop="mobile" v-model="addDialog.onSelectDataInfo.mobile"></el-input>
                </el-form-item>
                <el-select placeholder="是否超级管理员" v-model="addDialog.onSelectDataInfo.superAdmin">
                    <el-option v-for="item in superAdminItems" :label="item.name" :value="item.id"></el-option>
                </el-select>
                <el-form-item align="center">
                    <el-button @click="addRefuseReset('refForm')">重置</el-button> &nbsp;&nbsp;&nbsp; <el-button @click="addSubmit()">添加</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog title="重置管理员密码" align="center" v-model="resetDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="resetDialog"  ref='refForm'>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="userName" v-model="resetDialog.show_info.userName"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="密码" prop="password" v-model="resetDialog.onSelectDataInfo.password"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="resetRefuseReset('refForm')">重置</el-button> &nbsp;&nbsp;&nbsp; <el-button @click="resetSubmit()">确认</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

    </div>
</template>
<script>
    import ListJs from './adminList.js'

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

