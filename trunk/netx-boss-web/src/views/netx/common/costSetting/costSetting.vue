<template>
    <div class="list">
        <list-data
                ref='common-costSetting'
                @onClickBtnAdd="onClickBtnAdd"
                :BtnInfo="btn_info.condition"
                :List='list'
                :FieldList='fields'>
                <span slot="header-after">
                    <el-button
                            type="primary"
                            @click='show()'>查看当前版本
                    </el-button>
                </span>
        </list-data>
        <el-dialog title="添加费用设置" v-model="dialog.show" >
                        <div style="margin-left:230px">
                                分成：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.sharedFee"></el-input-number><br><br>
                                点击费用：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.clickFee"></el-input-number><br><br>
                                提现手续费：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.withdrawFee"></el-input-number><br><br>
                                网币发行费：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.creditIssueFee"></el-input-number><br><br>
                                网币竞购系数：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.creditFundsInterest"></el-input-number><br><br>
                                网币资金利息：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.creditInst"></el-input-number><br><br>
                                网币报名认购费用：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.creditSubscribeFee"></el-input-number><br><br>
                                图文发布押金：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.picAndVoicePublishDeposit"></el-input-number><br><br>
                                违规图文点击费用：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.violationClickFee"></el-input-number><br><br>
                                销售收入分成：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.salerSharedFee"></el-input-number><br><br>
                                心愿资金管理费：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.wishCapitalManageFee"></el-input-number><br><br>
                                注册商家管理费：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.shopManagerFee"></el-input-number><br><br>
                                注册商家管理费有效期：
                                <el-radio-group v-model="addData.shopManagerFeeLimitDate">
                                    <el-radio :label="0">终身有效</el-radio>
                                    <el-radio :label="1">一年</el-radio>
                                    <el-radio :label="3">三年</el-radio>
                                </el-radio-group>
                                <el-button
                                        type="primary"
                                        @click='add()'
                                        style="margin-left:110px">确定
                                </el-button>
                        </div>        
        </el-dialog>
        <el-dialog title="当前版本费用设置" v-model="showDialog.show" >
                        <div style="margin-left:230px">
                                分成：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.sharedFee"></el-input-number><br><br>
                                点击费用：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.clickFee"></el-input-number><br><br>
                                提现手续费：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.withdrawFee"></el-input-number><br><br>
                                网币发行费：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.creditIssueFee"></el-input-number><br><br>
                                网币竞购系数：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.creditFundsInterest"></el-input-number><br><br>
                                网币资金利息：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.creditInst"></el-input-number><br><br>
                                网币报名认购费用：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.creditSubscribeFee"></el-input-number><br><br>
                                图文发布押金：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.picAndVoicePublishDeposit"></el-input-number><br><br>
                                违规图文点击费用：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.violationClickFee"></el-input-number><br><br>
                                销售收入分成：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.salerSharedFee"></el-input-number><br><br>
                                心愿资金管理费：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.wishCapitalManageFee"></el-input-number><br><br>
                                注册商家管理费：<el-input-number size="small" :min="0" :step="0.2" v-model="addData.shopManagerFee"></el-input-number><br><br>
                                注册商家管理费有效期：
                                <el-radio-group v-model="addData.shopManagerFeeLimitDate">
                                    <el-radio :label="0">终身有效</el-radio>
                                    <el-radio :label="1">一年</el-radio>
                                    <el-radio :label="3">三年</el-radio>
                                </el-radio-group>
                                <el-button
                                        type="primary"
                                        @click='closeShow()'
                                        style="margin-left:110px">确定
                                </el-button>
                        </div>        
        </el-dialog>
    </div>
</template>
<script>
    import CostSettingJs from './costSetting.js'

    export default CostSettingJs
</script>

<style lang="less" scoped>
    .List{
        width:96%;
        margin-left: auto;
        margin-top: 100px;
        margin-right: auto;
    }
    .right{
        width:90%;
    }
    .dialog{
        width:80%;
    }
</style>