<template>
    <div class="List">
        <el-col>
            <strong>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共有{{paginations.total}}条建议
            </strong>
        </el-col>
        <el-form :inline="true" :model='selectData' class="demo-form-inline">
            <el-button type="primary" @click='addPasswordCheck'>后台直接加活动分</el-button>
        </el-form>
        <el-form :inline="true" :model='selectData' class="demo-form-inline">
            <el-button type="primary" @click='addSuggest'>后台添加建议</el-button>
            <el-form-item>
                <el-input placeholder="手机号码/网号" v-model='selectData.numCheck'></el-input>
            </el-form-item>
            <el-select v-model="dialog.info.operateType" placeholder="操作类型">
                <el-option v-for="item in items" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-form-item>
                <el-button type="primary" @click='getList'>查询</el-button>
            </el-form-item>

        </el-form>
        <el-table border style="width: 100%" align='center'
                  :data="suggestPassList">
            <el-table-column
                    :prop="fields.userId.info.prop"
                    :label="fields.userId.info.label"
                    :width="fields.userId.style.width"
                    :align="fields.userId.style.align"
                    :sortable="fields.userId.info.sortable" v-if="false">
            </el-table-column>
            <el-table-column
                    :prop="fields.id.info.prop"
                    :label="fields.id.info.label"
                    :width="fields.id.style.width"
                    :align="fields.id.style.align"
                    :sortable="fields.id.info.sortable" v-if="false">
            </el-table-column>
            <el-table-column
                    :prop="fields.nickname.info.prop"
                    :label="fields.nickname.info.label"
                    :width="fields.nickname.style.width"
                    :align="fields.nickname.style.align"
                    :sortable="fields.nickname.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.suggest.info.prop"
                    :label="fields.suggest.info.label"
                    :width="fields.suggest.style.width"
                    :align="fields.suggest.style.align"
                    :sortable="fields.suggest.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.result.info.prop"
                    :label="fields.result.info.label"
                    :width="fields.result.style.width"
                    :align="fields.result.style.align"
                    :sortable="fields.result.info.sortable">
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
                    :prop="fields.userNumber.info.prop"
                    :label="fields.userNumber.info.label"
                    :width="fields.userNumber.style.width"
                    :align="fields.userNumber.style.align"
                    :sortable="fields.userNumber.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.isEffective.info.prop"
                    :label="fields.isEffective.info.label"
                    :width="fields.isEffective.style.width"
                    :align="fields.isEffective.style.align"
                    :sortable="fields.isEffective.info.sortable"
                    :formatter="fields.isEffective.info.formatter">
            </el-table-column>
            <el-table-column
                    :prop="fields.credit.info.prop"
                    :label="fields.credit.info.label"
                    :width="fields.credit.style.width"
                    :align="fields.credit.style.align"
                    :sortable="fields.credit.info.sortable">
            </el-table-column>
            <el-table-column
                    :prop="fields.auditUserName.info.prop"
                    :label="fields.auditUserName.info.label"
                    :width="fields.auditUserName.style.width"
                    :align="fields.auditUserName.style.align"
                    :sortable="fields.auditUserName.info.sortable">
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
        <el-dialog title="建议审批操作" v-model="dialog.show">
            <el-form style='margin:20px;width:60%;min-width:100%'
                     label-width='150' :model="dialog.show_info" >
                <el-form-item v-if="false">
                    <<el-input readonly placeholder="用户id" prop="userId" v-model="dialog.show_info.userId"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input readonly placeholder="昵称" prop="nickName" v-model="dialog.show_info.nickname"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input readonly placeholder="网号" prop="userNumber" v-model="dialog.show_info.userNumber"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="primary" placeholder="审批描述" prop="reason" v-model="dialog.info.reason"></el-input>
                </el-form-item>
                <el-form-item style="display:none">
                    <el-input readonly placeholder="时间" prop="createTime" v-model="dialog.show_info.createTime"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-select placeholder="操作类型" v-model="dialog1.info.operateType">
                        <el-option v-for="item in items1" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="suggest">提交</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
       <el-dialog title="后台添加建议" v-model="dialog1.show">
           <el-form style='margin:20px;width:60%;min-width:100%' label-width='150' :model="dialog1.info">
            <el-form-item>
                <el-input type="primary" placeholder="用户网号" prop="userNumber" v-model="dialog1.info.userNumber"></el-input>
            </el-form-item>
               <el-form-item>
                   <el-input type="primary" placeholder="用户手机" prop="userNumber" v-model="dialog1.info.mobile"></el-input>
               </el-form-item>
               <el-form-item>
                   <el-input type="primary" placeholder="用户建议" prop="suggest" v-model="dialog1.info.suggest"></el-input>
               </el-form-item>
            <el-form-item>
                <el-input type="primary" placeholder="审批描述" prop="reason" v-model="dialog1.info.reason"></el-input>
            </el-form-item>
            <el-form-item>
                <el-select placeholder="操作类型" v-model="dialog1.info.operateType">
                    <el-option v-for="item in items1" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="suggestPass">提交</el-button>
            </el-form-item>
        </el-form>
       </el-dialog>
       <el-dialog title="管理员加分密码" v-model="pwdlog.show">
            <el-form style='margin:20px;width:60%;min-width:100%' label-width='150' :model="dialog1.info">
                 <el-form-item>
                      <el-input type="password"  :maxlength="20" style="width:350px;" placeholder="密码" prop="password" v-model="pwdlog.info.password"></el-input>
                  </el-form-item>
                  <el-form-item>
                      <el-button type="primary" @click="passwordCheck">提交</el-button>
                 </el-form-item>
            </el-form>
         </el-dialog>
        <el-dialog title="后台加分" v-model="dialog2.show">
            <el-form style='margin:20px;width:60%;min-width:100%' label-width='150' :model="dialog1.info">
                <el-form-item>
                    <el-input type="primary" placeholder="用户手机" prop="userNumber" v-model="dialog2.info.mobile"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addScore">提交</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>
<script>
    import SuggestListJs from './suggestPassList'

    export default SuggestListJs
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

