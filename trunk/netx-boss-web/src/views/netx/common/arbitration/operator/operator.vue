<template>
    <div class="List" align="center">
        <div class="top-div">
               <h1>仲裁详情信息</h1>
        </div>
        <div>
            <div class="base-div">
                <table cellpadding="0" cellspacing="0" border="1" style="border:1px solid #666;">
                    <thead>
                        <tr>
                            <th>基本信息</th>
                            <th>投诉方</th>
                            <th>辩方</th>
                        </tr>
                    </thead>
                    <tbody align="center">
                        <tr>
                            <td><h3>昵称</h3></td>
                            <td><div>{{ dataInfo.fromNickname }}</div></td>
                            <td><div>{{ dataInfo.toNickname }}</div></td>
                        </tr>
                        <tr>
                            <td ><h3>性别</h3></td>
                            <td><div>{{ dataInfo.fromUserSex }}</div></td>
                            <td><div>{{ dataInfo.toUserLevel }}</div></td>
                        </tr>
                        <tr>
                            <td><h3>信用值</h3></td>
                            <td><div>{{ dataInfo.fromUserCreditValue }}</div></td>
                            <td><div>{{ dataInfo.toUserCreditValue }}</div></td>
                        </tr>
                         <tr>
                             <td><h3>辩解</h3></td>
                             <td><div>{{ dataInfo.reason }}</div></td>
                             <td><div>{{ dataInfo.descriptions }}</div></td>
                         </tr>
                        <tr>
                            <td><h3>证据图片</h3></td>
                            <td>
                                <div>
                                    <el-table  :data="fromSrcUrl">
                                        <el-table-column prop="url">
                                            <template slot-scope="scope">
                                                <img  :src="scope.row.url" style="width: 320px;height: 150px">
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                </div>
                            </td>
                            <td>
                                <div>
                                    <el-table :data="appealSrcUrl">
                                        <el-table-column prop="url">
                                            <template slot-scope="scope">
                                                <img  :src="scope.row.url" style="width: 320px;height: 150px">
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
                &nbsp;&nbsp;&nbsp;<el-button @click="openDialog">受理</el-button> &nbsp;&nbsp;&nbsp;
            </div>
            <el-dialog class="operateForm" align="center" v-model="dialogInfo.show">
                <el-form :inline="true" :model="onSelectData" ref="refForm" :rules="rules_form">
                    <table cellpadding="0" cellspacing="0"  style="border:1px solid #666;text-align:center" border="1">
                        <tr><!--扣信用 -->
                            <td>扣信用值</td>
                            <td>
                                <el-radio-group v-model="onSelectData.CreditRefund">
                                    <el-radio :label="0">投诉人</el-radio>
                                    <el-radio :label="1">被投诉人</el-radio>
                                    <el-radio :label="2">都不选</el-radio>
                                </el-radio-group>
                            </td>
                        </tr>
                        <tr>
                            <td>信用值</td>
                            <td>
                                <el-input prop="UserCreditPointValue" v-model="onSelectData.UserCreditPointValue" placeholder="请输入信用值内容"></el-input>
                            </td>
                        </tr>
                        <tr>
                            <td>理由</td>
                            <td>
                                <el-input prop="UserCreditPointReason" type="textarea" placeholder="请输入你的理由" v-model="onSelectData.UserCreditPointReason"></el-input>
                            </td>
                        </tr>
                        <tr><!--退款 -->
                            <td>退款选择</td>
                            <td>
                                <el-radio :label="true" v-model="onSelectData.refundRadioButton">同意</el-radio>
                                <el-radio :label="false" v-model="onSelectData.refundRadioButton">不同意</el-radio>
                            </td>
                        </tr>
                        <tr>
                            <td>理由</td>
                            <td>
                                <el-input prop="refundArbitrateReason" type="textarea" placeholder="请输入你的理由" v-model="onSelectData.refundArbitrateReason"></el-input>
                            </td>
                        </tr>
                        <tr><!--退货 -->
                            <td>退货选择</td>
                            <td>
                                <el-radio :label="true" v-model="onSelectData.returnRadioButton">同意</el-radio>
                                <el-radio :label="false" v-model="onSelectData.returnRadioButton">不同意</el-radio>
                            </td>
                        </tr>
                        <tr>
                            <td>理由</td>
                            <td>
                                <el-input prop="returnArbitrateReason" type="textarea" placeholder="请输入你的理由" v-model="onSelectData.returnArbitrateReason"></el-input>
                            </td>
                        </tr>
                        <tr><!--活动退款 -->
                            <td>活动退款选择</td>
                            <td>
                                <el-radio :label="1" v-model="onSelectData.subReleaseFrozenMoneyRefund">同意</el-radio>
                                <el-radio :label="0" v-model="onSelectData.subReleaseFrozenMoneyRefund">不同意</el-radio>
                            </td>
                        </tr>
                        <tr>
                            <td>理由</td>
                            <td>
                                <el-input prop="subReleaseReleaseFrozenMoneyReason" type="textarea" placeholder="请输入你的理由" v-model="onSelectData.subReleaseReleaseFrozenMoneyReason"></el-input>
                            </td>
                        </tr>
                    </table>
                    <el-button type="primary" @click="reset_form('onSelectData')">重置</el-button>&nbsp;&nbsp;&nbsp; <el-button type="primary" @click="onSubmit('refForm')">提交</el-button>
                </el-form>
            </el-dialog>
        </div>
    </div>
</template>
<script>
  import OperatorJs from './operator.js'

  export default OperatorJs
</script>
<style lang="less" scoped>
    .top-div{
        color: #FF0000;
        text-align: center;
        width: 90%;
        height:150px;
    }
    td div{
        width: 400px;
        height:auto;
        table-layout : auto;
        overflow-x:hidden;
        word-wrap:break-word;
    }

</style>