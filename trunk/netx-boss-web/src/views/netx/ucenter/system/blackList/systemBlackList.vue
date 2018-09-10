<template>
    <div class="List">
        <el-form :inline="true" :model='selectData' class="demo-form-inline">
            <el-form-item>
                <el-input placeholder="网号" v-model='selectData.userNumber'></el-input>
            </el-form-item>
            <el-select v-model="selectData.operateType" placeholder="操作类型">
                <el-option v-for="item in items" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-form-item>
                <el-button type="primary" @click='getList(1,10)'>查询</el-button>
            </el-form-item>
        </el-form>
        <el-table border style="width: 100%" align='center'
            :data="systemBlackList">
            <el-table-column
                :prop="fields.id.info.prop"
                :label="fields.id.info.label"
                :width="fields.id.style.width"
                :align="fields.id.style.align"
                :sortable="fields.id.info.sortable" v-if="false">
            </el-table-column>
            <el-table-column
                    :prop="fields.nickName.info.prop"
                    :label="fields.nickName.info.label"
                    :width="fields.nickName.style.width"
                    :align="fields.nickName.style.align"
                    :sortable="fields.nickName.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.lv.info.prop"
                    :label="fields.lv.info.label"
                    :width="fields.lv.style.width"
                    :align="fields.lv.style.align"
                    :sortable="fields.lv.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.sex.info.prop"
                    :label="fields.sex.info.label"
                    :width="fields.sex.style.width"
                    :align="fields.sex.style.align"
                    :sortable="fields.sex.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.age.info.prop"
                    :label="fields.age.info.label"
                    :width="fields.age.style.width"
                    :align="fields.age.style.align"
                    :sortable="fields.age.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.credit.info.prop"
                    :label="fields.credit.info.label"
                    :width="fields.credit.style.width"
                    :align="fields.credit.style.align"
                    :sortable="fields.credit.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.userNumber.info.prop"
                    :label="fields.userNumber.info.label"
                    :width="fields.userNumber.style.width"
                    :align="fields.userNumber.style.align"
                    :sortable="fields.userNumber.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.reason.info.prop"
                    :label="fields.reason.info.label"
                    :width="fields.reason.style.width"
                    :align="fields.reason.style.align"
                    :sortable="fields.reason.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.operateUserNickname.info.prop"
                    :label="fields.operateUserNickname.info.label"
                    :width="fields.operateUserNickname.style.width"
                    :align="fields.operateUserNickname.style.align"
                    :sortable="fields.operateUserNickname.info.sortable">
            </el-table-column>
            <el-table-column label="操作"
                    :width="100"
                    :content="_self">
                <template scope="scope">
                    <el-button
                            type="info"
                            icon="edit"
                            size="mini"
                            @click="onEdit(scope.row)">
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-col :span="24" class='btm-action'>
            <el-pagination
                    v-if='paginations.total>0'
                    class='pagination'
                    :page-sizes="paginations.page_sizes"
                    :page-size="paginations.page_size"
                    :layout="paginations.layout"
                    :total="paginations.total"
                    :current-page='paginations.current_page'
                    @current-change='onChangeCurrentPage'
                    @size-change='onChangePageSize'>
            </el-pagination>
        </el-col>
        <el-dialog title="黑名单操作" v-model="dialog.show">
            <el-form style='margin:20px;width:60%;min-width:100%'
                     label-width='150' :model="dialog.show_info">
                <el-form-item v-if="false">
                    <el-input  placeholder="用户id" prop="id" v-model="dialog.show_info.id"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input  placeholder="昵称" prop="nickName" v-model="dialog.show_info.nickName"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input  placeholder="网号" prop="userNumber" v-model="dialog.show_info.userNumber"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input placeholder="释放的原因" prop="reason" v-model="dialog.info.reason"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-select placeholder="操作类型" v-model="dialog.info.operateType">
                        <el-option v-for="item in items" :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="operatorBlacklist">提交</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>
<script>
    import SystemBlackListJs from './systemBlackList'

    export default SystemBlackListJs
</script>
<style scoped type="less">
    .List{
        width:100%;
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