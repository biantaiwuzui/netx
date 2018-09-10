<template>
    <div class="List">
        <el-col>
            <strong>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共有{{paginations.total}}个类目
            </strong>
        </el-col>
        <el-form :inline="true" :model="selectFormData" class="demo-form-inline">
            <el-form-item>
                <el-input placeholder="类目名称" v-model="selectFormData.name"></el-input>
            </el-form-item>
            <el-select placeholder="类目级别" v-model="selectFormData.parentId">
                <el-option v-for="item in parentIdItems" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-select placeholder="类目状态" v-model="selectFormData.deleted">
                <el-option v-for="item in deletedItems" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-form-item>
                <el-button type="primary" @click="onSubmitForm">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addRefuse('0')">新增一级类目</el-button>
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
                    :prop="fields.name.info.prop"
                    :label="fields.name.info.label"
                    :width="fields.name.style.width"
                    :align="fields.name.style.align"
                    :sortable="fields.name.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.parentId.info.prop"
                    :label="fields.parentId.info.label"
                    :width="fields.parentId.style.width"
                    :align="fields.parentId.style.align"
                    :sortable="fields.parentId.info.sortable"
                    :formatter="fields.parentId.info.formatter">
            </el-table-column>
            <el-table-column
                    :prop="fields.usedCount.info.prop"
                    :label="fields.usedCount.info.label"
                    :width="fields.usedCount.style.width"
                    :align="fields.usedCount.style.align"
                    :sortable="fields.usedCount.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.priority.info.prop"
                    :label="fields.priority.info.label"
                    :width="fields.priority.style.width"
                    :align="fields.priority.style.align"
                    :sortable="fields.priority.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.py.info.prop"
                    :label="fields.py.info.label"
                    :width="fields.py.style.width"
                    :align="fields.py.style.align"
                    :sortable="fields.py.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.icon.info.prop"
                    :label="fields.icon.info.label"
                    :width="fields.icon.style.width"
                    :align="fields.icon.style.align"
                    :sortable="fields.icon.info.sortable">
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
                            @click='kidCategorySubmitForm(scope.row.id)' v-if="scope.row.parentId == '0'">查看二级类目</el-button>
                    <el-button
                            type="info"
                            icon='edit'
                            size="mini"
                            @click='addRefuse(scope.row.id)' v-if="scope.row.parentId == '0'">新增二级类目</el-button>
                    <el-button
                            type="warning"
                            icon='edit'
                            size="mini"
                            @click='upRefuse(scope.row)' v-if="scope.row.deleted == 1">启用类目</el-button>
                    <el-button
                            type="danger"
                            icon='delete'
                            size="mini"
                            @click='downRefuse(scope.row)' v-if="scope.row.deleted == 0">禁用类目</el-button>
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
        <el-dialog title="确定禁用该类目" align="center" v-model="downDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="downDialog" ref='refForm'>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="id" v-model="downDialog.show_info.id"></el-input>
                </el-form-item>
                <el-form-item>
                    <h2>
                        {{ downDialog.onSelectDataInfo.name }}
                    </h2>
                </el-form-item>
                <el-form-item>
                    <el-button @click="downRefuseReset()">取消</el-button> &nbsp;&nbsp;&nbsp; <el-button @click="downSubmit()">确认</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog title="确定重新启用该类目" align="center" v-model="upDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="upDialog" ref='refForm'>
                <el-form-item v-if="false">
                    <el-input readonly type="primary" prop="id" v-model="upDialog.show_info.id"></el-input>
                </el-form-item>
                <el-form-item>
                    <h2>
                        {{ upDialog.onSelectDataInfo.name }}
                    </h2>
                </el-form-item>
                <el-form-item>
                    <el-button @click="upRefuseReset('refForm')">取消</el-button> &nbsp;&nbsp;&nbsp; <el-button @click="upSubmit()">确认</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog title="添加商品类目" v-model="addDialog.show"
                   style='margin:20px;width:60%;min-width:100%'
                   label-width='150'>
            <el-form :model="addDialog" ref='refForm'>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入类目名" prop="name" v-model="addDialog.onSelectDataInfo.name"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="请输入优先级" prop="password" v-model="addDialog.onSelectDataInfo.priority"></el-input>
                </el-form-item>
                <el-select placeholder="类目类型" v-model="addDialog.onSelectDataInfo.icon">
                    <el-option v-for="item in iconItems" :label="item.name" :value="item.id"></el-option>
                </el-select>
                <el-form-item align="center">
                    <el-button @click="addRefuseReset('refForm')">重置</el-button> &nbsp;&nbsp;&nbsp; <el-button @click="addSubmit()">添加</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

    </div>
</template>
<script>
    import ListJs from './categoryList.js'

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

