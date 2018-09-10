<template>
        <div class="form">
        <el-form ref="form" :model="form" label-width="280px" style="margin:20px;width:60%;min-width:600px;">
                 <el-form-item label="心愿限制类别">
                        <el-select v-model="form.wishLimitType" placeholder="请选择" @change="wishLimitType">
                                <el-option label="人员名单" value=0></el-option>
                                <el-option label="限制条件" value=1></el-option>
                        </el-select>  
                </el-form-item>
                <el-form-item label="图文音视限制类别">
                        <el-select v-model="form.picLimitType" placeholder="请选择" @change="picLimitType">
                                <el-option label="人员名单" value=0></el-option>
                                <el-option label="限制条件" value=1></el-option>
                        </el-select>
                </el-form-item>
                <el-form-item label="商家注册限制类别">
                        <el-select v-model="form.regMerchantLimitType" placeholder="请选择" @change="regMerchantLimitType">
                                <el-option label="人员名单" value=0></el-option>
                                <el-option label="限制条件" value=1></el-option>
                        </el-select>
                </el-form-item>
                <el-form-item label="需求技能活动限制类别">
                        <el-select v-model="form.skillLimitType" @change="skillLimitType">
                                <el-option label="人员名单" value=0></el-option>
                                <el-option label="限制条件" value=1></el-option>
                        </el-select>
                </el-form-item>
                <el-form-item label="发行网币注册限制类别">
                        <el-select v-model="form.creditLimitType" placeholder="请选择" @change="creditLimitType">
                                <el-option label="人员名单" value=0></el-option>
                                <el-option label="限制条件" value=1></el-option>
                        </el-select>
                </el-form-item>
                <el-form-item label="礼物邀请分享的限制类别">
                        <el-select v-model="form.shareLimitType" placeholder="请选择" @change="shareLimitType">
                                <el-option label="人员名单" value=0></el-option>
                                <el-option label="限制条件" value=1></el-option>
                        </el-select>
                </el-form-item>
        </el-form>
        <el-button
                type="primary"
                @click='submit()'
                style="margin-left:530px">提交
        </el-button>
        <el-dialog title="需求活动技能限制条件" v-model="skillDialog.show" >
                <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px" :model="form">
                        <el-form-item label="">
                                <el-checkbox-group v-model="form.skillLimitCondition">
                                        <el-checkbox label=0 name="skillLimitCondition">
                                                信用值不低于
                                        </el-checkbox>
                                        <el-input-number size="small" :min="1" v-model="form.skillLimitPoint"></el-input-number>
                                                分<br>
                                        <el-checkbox label=1 name="skillLimitCondition">通过手机验证</el-checkbox><br>
                                        <el-checkbox label=2 name="skillLimitCondition">通过身份验证</el-checkbox><br>
                                        <el-checkbox label=3 name="skillLimitCondition">通过视频验证</el-checkbox>
                                </el-checkbox-group>
                        </el-form-item>
                        <el-button
                                type="primary"
                                @click='skillSubmit()'
                                style="margin-left:300px">确定
                        </el-button>
                </el-form>
        </el-dialog>
        <el-dialog title="图文音视限制条件" v-model="picDialog.show" >
                <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px" :model="form">
                        <el-form-item label="">
                                <el-checkbox-group v-model="form.picLimitCondition">
                                        <el-checkbox label=0 name="picLimitCondition">
                                                信用值不低于
                                        </el-checkbox>
                                        <el-input-number size="small" :min="1" v-model="form.picLimitPoint"></el-input-number>
                                                分<br>
                                        <el-checkbox label=1 name="picLimitCondition">通过手机验证</el-checkbox><br>
                                        <el-checkbox label=2 name="picLimitCondition">通过身份验证</el-checkbox><br>
                                        <el-checkbox label=3 name="picLimitCondition">通过视频验证</el-checkbox>
                                </el-checkbox-group>
                        </el-form-item>
                        <el-button
                                type="primary"
                                @click='picSubmit()'
                                style="margin-left:300px">确定
                        </el-button>
                </el-form>
        </el-dialog>
        <el-dialog title="心愿限制条件" v-model="wishDialog.show" >
                <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px" :model="form">
                        <el-form-item label="">
                                <el-checkbox-group v-model="form.wishLimitCondition">
                                        <el-checkbox label=0 name="wishLimitCondition">
                                                信用值不低于
                                        </el-checkbox>
                                        <el-input-number size="small" :min="1" v-model="form.wishLimitPoint"></el-input-number>
                                                分<br>
                                        <el-checkbox label=1 name="wishLimitCondition">通过手机验证</el-checkbox><br>
                                        <el-checkbox label=2 name="wishLimitCondition">通过身份验证</el-checkbox><br>
                                        <el-checkbox label=3 name="wishLimitCondition">通过视频验证</el-checkbox>
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
                <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px" :model="form">
                        <el-form-item label="">
                                <el-checkbox-group v-model="form.regMerchantLimitCondition">
                                        <el-checkbox label=0 name="regMerchantLimitCondition">
                                                信用值不低于
                                        </el-checkbox>
                                        <el-input-number size="small" :min="1" v-model="form.regMerchantLimitPoint"></el-input-number>
                                                分<br>
                                        <el-checkbox label=1 name="regMerchantLimitCondition">通过手机验证</el-checkbox><br>
                                        <el-checkbox label=2 name="regMerchantLimitCondition">通过身份验证</el-checkbox><br>
                                        <el-checkbox label=3 name="regMerchantLimitCondition">通过视频验证</el-checkbox>
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
                <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px" :model="form">
                        <el-form-item label="">
                                <el-checkbox-group v-model="form.creditLimitCondition">
                                        <el-checkbox label=0 name="creditLimitCondition">
                                                信用值不低于
                                        </el-checkbox>
                                        <el-input-number size="small" :min="1" v-model="form.creditLimitPoint"></el-input-number>
                                                分<br>
                                        <el-checkbox label=1 name="creditLimitCondition">通过手机验证</el-checkbox><br>
                                        <el-checkbox label=2 name="creditLimitCondition">通过身份验证</el-checkbox><br>
                                        <el-checkbox label=3 name="creditLimitCondition">通过视频验证</el-checkbox><br>
                                        <el-checkbox label=4 name="creditLimitCondition">
                                                上月业绩不低于
                                        </el-checkbox>
                                        <el-input-number size="small" :min="1" v-model="form.creditLimitMoreThen"></el-input-number><br>
                                        <el-checkbox label=5 name="creditLimitCondition">
                                                上期发行网信余额小于
                                        </el-checkbox>
                                        <el-input-number size="small" :min="1" v-model="form.creditLimitBalance"></el-input-number><br>
                                        <el-checkbox label=6 name="creditLimitCondition">达到了上期网币发型时的业绩增长承诺</el-checkbox>
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
                <el-form style="margin:20px;width:60%;min-width:100%" label-width="150px" :model="form">
                        <el-form-item label="">
                                <el-checkbox-group v-model="form.shareLimitCondition">
                                        <el-checkbox label=0 name="shareLimitCondition">
                                                信用值不低于
                                        </el-checkbox>
                                        <el-input-number size="small" :min="1" v-model="form.shareLimitPoint"></el-input-number>
                                                分<br>
                                        <el-checkbox label=1 name="shareLimitCondition">通过手机验证</el-checkbox><br>
                                        <el-checkbox label=2 name="shareLimitCondition">通过身份验证</el-checkbox><br>
                                        <el-checkbox label=3 name="shareLimitCondition">通过视频验证</el-checkbox>
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
  import AddOtherSetJs from './addOtherSet.js'

  export default AddOtherSetJs
</script>
<style scoped lang='less'>

</style>
