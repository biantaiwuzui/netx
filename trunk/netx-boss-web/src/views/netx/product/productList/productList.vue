<template>
    <div class="List">
        <el-col>
            <strong>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共有{{paginations.total}}个商品
            </strong>
        </el-col>
        <el-form :inline="true" :model="selectFormData" class="demo-form-inline">
            <el-form-item>
                <el-input placeholder="商家名称" v-model="selectFormData.merchantName"></el-input>
            </el-form-item>
            <el-form-item>
                <el-input placeholder="商品名称" v-model="selectFormData.productName"></el-input>
            </el-form-item>
            <el-select placeholder="商品状态" v-model="selectFormData.onlineStatus">
                <el-option v-for="item in onlineStatusItems" :label="item.name" :value="item.id"></el-option>
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
                    :prop="fields.nickName.info.prop"
                    :label="fields.nickName.info.label"
                    :width="fields.nickName.style.width"
                    :align="fields.nickName.style.align"
                    :sortable="fields.nickName.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.merchantName.info.prop"
                    :label="fields.merchantName.info.label"
                    :width="fields.merchantName.style.width"
                    :align="fields.merchantName.style.align"
                    :sortable="fields.merchantName.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.name.info.prop"
                    :label="fields.name.info.label"
                    :width="fields.name.style.width"
                    :align="fields.name.style.align"
                    :sortable="fields.name.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.characteristic.info.prop"
                    :label="fields.characteristic.info.label"
                    :width="fields.characteristic.style.width"
                    :align="fields.characteristic.style.align"
                    :sortable="fields.characteristic.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.delivery.info.prop"
                    :label="fields.delivery.info.label"
                    :width="fields.delivery.style.width"
                    :align="fields.delivery.style.align"
                    :sortable="fields.delivery.info.sortable"
                    :formatter="fields.delivery.info.formatter">
            </el-table-column>
            <el-table-column
                    :prop="fields.onlineStatus.info.prop"
                    :label="fields.onlineStatus.info.label"
                    :width="fields.onlineStatus.style.width"
                    :align="fields.onlineStatus.style.align"
                    :sortable="fields.onlineStatus.info.sortable"
                    :formatter="fields.onlineStatus.info.formatter">
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
                            @click='upRefuse(scope.row)' v-if="up(scope.row.onlineStatus)">重新上架</el-button>
                    <el-button
                            type="warning"
                            icon='edit'
                            size="mini"
                            @click='downRefuse(scope.row)' v-if="down(scope.row.onlineStatus)">强制下架</el-button>
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
        <el-dialog title="强制下架" align="center" v-model="downDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="downDialog" :rules="down_rules" ref='refForm'>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="id" v-model="downDialog.show_info.id"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入强制下架的理由" prop="reason" v-model="downDialog.onSelectDataInfo.reason"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="downRefuseReset('refForm')">重置</el-button> &nbsp;&nbsp;&nbsp; <el-button @click="downSubmit()">下架</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog title="重新上架" align="center" v-model="upDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="upDialog" :rules="up_rules" ref='refForm'>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="id" v-model="upDialog.show_info.id"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入重新上架的理由" prop="reason" v-model="upDialog.onSelectDataInfo.reason"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="upRefuseReset('refForm')">重置</el-button> &nbsp;&nbsp;&nbsp; <el-button @click="upSubmit()">上架</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>
<script>
    import ListJs from './productList.js'

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

