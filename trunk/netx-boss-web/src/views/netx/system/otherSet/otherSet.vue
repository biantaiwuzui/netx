<template>
        <div class="list">
                <list-data
                        ref='user-otherSet'
                        @onClickBtnAdd="onClickBtnAdd"
                        @onClickBtnDelete="onClickBtnDelete"
                        @onClickBtnSelect="onClickBtnSelect"
                        :BtnInfo="btn_info.condition"
                        :List='list'
                        :FieldList='fields'>
                </list-data>

                <el-dialog title="详情" v-model="dialog.show" >
                        <div style="margin-left:230px">
                                <el-button
                                        type="primary"
                                        @click='showSkillLimitCondition()'>发布技能限制类别
                                </el-button>
                                <el-button
                                        type="primary"
                                        @click='showPicLimitCondition()'>发布图文限制类别
                                </el-button><br><br>
                                <el-button
                                        type="primary"
                                        @click='showWishLimitCondition()'>发布心愿限制类别
                                </el-button>
                                <el-button
                                        type="primary"
                                        @click='showRegMerchantLimitCondition()'>注册商家限制类别
                                </el-button><br><br>
                                <el-button
                                        type="primary"
                                        @click='showCreditLimitCondition()'>发行网币限制类别
                                </el-button>
                                <el-button
                                        type="primary"
                                        @click='showShareLimitCondition()'>赠送礼物限制类别
                                </el-button>
                        </div>        
                </el-dialog>
                <el-dialog title="发布需求活动技能限制条件" v-model="skillDialog.show" >
                        <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px" >
                                <el-form-item label="">
                                        <el-checkbox-group v-model="skillLimitCondition">
                                                <el-checkbox label=0 name="skillLimitCondition" disabled>
                                                        信用值不低于
                                                </el-checkbox>
                                                <el-input-number size="small" :min="1" v-model="skillLimitPoint"  disabled></el-input-number>
                                                        分<br>
                                                <el-checkbox label=1 name="skillLimitCondition"  disabled>通过手机验证</el-checkbox><br>
                                                <el-checkbox label=2 name="skillLimitCondition"  disabled>通过身份验证</el-checkbox><br>
                                                <el-checkbox label=3 name="skillLimitCondition"  disabled>通过视频验证</el-checkbox>
                                        </el-checkbox-group>
                                </el-form-item>
                                <el-button
                                        type="primary"
                                        @click='skillSubmit()'
                                        style="margin-left:300px">确定
                                </el-button>
                        </el-form>
                </el-dialog>
                <el-dialog title="发布图文音视限制条件" v-model="picDialog.show" >
                        <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px">
                                <el-form-item label="">
                                        <el-checkbox-group v-model="picLimitCondition">
                                                <el-checkbox label=0 name="picLimitCondition"  disabled>
                                                        信用值不低于
                                                </el-checkbox>
                                                <el-input-number size="small" :min="1" v-model="picLimitPoint"  disabled></el-input-number>
                                                        分<br>
                                                <el-checkbox label=1 name="picLimitCondition"  disabled>通过手机验证</el-checkbox><br>
                                                <el-checkbox label=2 name="picLimitCondition"  disabled>通过身份验证</el-checkbox><br>
                                                <el-checkbox label=3 name="picLimitCondition"  disabled>通过视频验证</el-checkbox>
                                        </el-checkbox-group>
                                </el-form-item>
                                <el-button
                                        type="primary"
                                        @click='picSubmit()'
                                        style="margin-left:300px">确定
                                </el-button>
                        </el-form>
                </el-dialog>
                <el-dialog title="发布心愿限制条件" v-model="wishDialog.show" >
                        <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px">
                                <el-form-item label="">
                                        <el-checkbox-group v-model="wishLimitCondition">
                                                <el-checkbox label=0 name="wishLimitCondition"  disabled>
                                                        信用值不低于
                                                </el-checkbox>
                                                <el-input-number size="small" :min="1" v-model="wishLimitPoint"  disabled></el-input-number>
                                                        分<br>
                                                <el-checkbox label=1 name="wishLimitCondition"  disabled>通过手机验证</el-checkbox><br>
                                                <el-checkbox label=2 name="wishLimitCondition"  disabled>通过身份验证</el-checkbox><br>
                                                <el-checkbox label=3 name="wishLimitCondition"  disabled>通过视频验证</el-checkbox>
                                        </el-checkbox-group>
                                </el-form-item>
                                <el-button
                                        type="primary"
                                        @click='wishSubmit()'
                                        style="margin-left:300px">确定
                                </el-button>
                        </el-form>
                </el-dialog>
                <el-dialog title="商家注册限制条件" v-model="regMerchantDialog.show" >
                        <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px">
                                <el-form-item label="">
                                        <el-checkbox-group v-model="regMerchantLimitCondition">
                                                <el-checkbox label=0 name="regMerchantLimitCondition"  disabled>
                                                        信用值不低于
                                                </el-checkbox>
                                                <el-input-number size="small" :min="1" v-model="regMerchantLimitPoint"  disabled></el-input-number>
                                                        分<br>
                                                <el-checkbox label=1 name="regMerchantLimitCondition"  disabled>通过手机验证</el-checkbox><br>
                                                <el-checkbox label=2 name="regMerchantLimitCondition"  disabled>通过身份验证</el-checkbox><br>
                                                <el-checkbox label=3 name="regMerchantLimitCondition"  disabled>通过视频验证</el-checkbox>
                                        </el-checkbox-group>
                                </el-form-item>
                                <el-button
                                        type="primary"
                                        @click='regMerchantSubmit()'
                                        style="margin-left:300px">确定
                                </el-button>
                        </el-form>
                </el-dialog>
                <el-dialog title="发行网币限制条件" v-model="creditDialog.show" >
                        <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px">
                                <el-form-item label="">
                                        <el-checkbox-group v-model="creditLimitCondition">
                                                <el-checkbox label=0 name="creditLimitCondition"  disabled>
                                                        信用值不低于
                                                </el-checkbox>
                                                <el-input-number size="small" :min="1" v-model="creditLimitPoint"  disabled></el-input-number>
                                                        分<br>
                                                <el-checkbox label=1 name="creditLimitCondition"  disabled>通过手机验证</el-checkbox><br>
                                                <el-checkbox label=2 name="creditLimitCondition"  disabled>通过身份验证</el-checkbox><br>
                                                <el-checkbox label=3 name="creditLimitCondition"  disabled>通过视频验证</el-checkbox><br>
                                                <el-checkbox label=4 name="creditLimitCondition"  disabled>
                                                        上月业绩不低于
                                                </el-checkbox>
                                                <el-input-number size="small" :min="1" v-model="creditLimitMoreThen"  disabled></el-input-number><br>
                                                <el-checkbox label=5 name="creditLimitCondition"  disabled>
                                                        上期发行网信余额小于
                                                </el-checkbox>
                                                <el-input-number size="small" :min="1" v-model="creditLimitBalance"  disabled></el-input-number><br>
                                                <el-checkbox label=6 name="creditLimitCondition"  disabled>达到了上期网币发型时的业绩增长承诺</el-checkbox>
                                        </el-checkbox-group>
                                </el-form-item>
                                <el-button
                                        type="primary"
                                        @click='creditSubmit()'
                                        style="margin-left:300px">确定
                                </el-button>
                        </el-form>
                </el-dialog>
                <el-dialog title="礼物邀请分享限制条件" v-model="shareDialog.show" >
                        <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px">
                                <el-form-item label="">
                                        <el-checkbox-group v-model="shareLimitCondition">
                                                <el-checkbox label=0 name="shareLimitCondition"  disabled>
                                                        信用值不低于
                                                </el-checkbox>
                                                <el-input-number size="small" :min="1" v-model="shareLimitPoint"  disabled></el-input-number>
                                                        分<br>
                                                <el-checkbox label=1 name="shareLimitCondition"  disabled>通过手机验证</el-checkbox><br>
                                                <el-checkbox label=2 name="shareLimitCondition"  disabled>通过身份验证</el-checkbox><br>
                                                <el-checkbox label=3 name="shareLimitCondition"  disabled>通过视频验证</el-checkbox>
                                        </el-checkbox-group>
                                </el-form-item>
                                <el-button
                                        type="primary"
                                        @click='shareSubmit()'
                                        style="margin-left:300px">确定
                                </el-button>
                        </el-form>
                </el-dialog>                
        </div>
</template>
<script>
  import OtherSetJs from './otherSet.js'

  export default OtherSetJs
</script>
<style scoped lang='less'>

</style>
