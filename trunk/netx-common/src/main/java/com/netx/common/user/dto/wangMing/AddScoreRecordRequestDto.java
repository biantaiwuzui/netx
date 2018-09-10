package com.netx.common.user.dto.wangMing;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AddScoreRecordRequestDto extends AddWangMingRecordSuperRequestDto {

    /*@NotNull(message = "本笔积分不能为空")
    @ApiModelProperty("本笔积分，收入支持用正负号标识")
    private BigDecimal Score;*/

    @Min(value = 1, message = "code最小值不能小于0")
    @Max(value = 16, message = "code最大值不能大于16")
    @NotNull(message = "积分相关项目的 key 值不能为空")
    @ApiModelProperty("对应积分相关项目的 key 值" +
            "1：资料完善度每10%<br>" +
            "2：连续登录1-4天<br>" +
            "3：连续登录5-10天<br>" +
            "4：连续登录11-29天<br>" +
            "5：连续登录30天及以上<br>" +
            "6：每获得5个赞<br>" +
            "7：每发布1条信息<br>" +
            "8：每完成1个交易<br>" +
            "9：每被人关注5次<br>" +
            "10：每关注他人5次<br>" +
            "11：每个正分评价<br>" +
            "12：每个负分评价<br>" +
            "13：信用每降低1分<br>" +
            "14：超过10天没登录<br>" +
            "15：邀请好友完成注册<br>" +
            "16：成功分享链接")
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    /*public BigDecimal getScore() {
        return Score;
    }

    public void setScore(BigDecimal score) {
        Score = score;
    }*/
}
