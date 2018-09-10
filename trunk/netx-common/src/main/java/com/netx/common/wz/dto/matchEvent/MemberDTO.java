package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 赛事嘉宾、工作人员
 * Created by Yawn on 2018/8/1 0001.
 */
public class MemberDTO {
    @ApiModelProperty(value = "有id值情况表示更新，没有的话表示插入")
    private String id;
    /**
     * 比赛id
     */
    @NotBlank(message = "比赛ID不能为空")
    @ApiModelProperty(value = "比赛id")
    private String matchId;
    /**
     * 用户名字
     */
    @ApiModelProperty(value = "用户ID", notes = "网值外用户的时候，手机号填写这里")
    @NotBlank(message = "请输入用户ID")
    private String userId;
    /**
     * 用户称呼
     */
    @NotBlank(message = "请填入用户称呼" )
    @ApiModelProperty(value = "用户称呼")
    private String userCall;
    /**
     * 工作人员类型：领导、嘉宾、会场工作人员
     */
    @NotBlank(message = "用户类型不能为空")
    @ApiModelProperty(value = "工作人员类型" +
            "(   0, \"发起人\"),\n" +
            "    (1, \"主持人\"),\n" +
            "    (2, \"工作人员\"),\n" +
            "    (3, \"审核人员\"),\n" +
            "    (4, \"嘉宾\"),\n" +
            "    (5, \"评委\"),\n" +
            "    (6, \"场地管理人员（这个就相当于其他人查看这个比赛的时候用的，传值的话只传0-6）\");" +
            "还有其他类型通知文聪")
    @Min(value = 0, message = "邀请人员类型错误")
    @Max(value = 6, message = "邀请人员类型错误")
    private Integer kind;

    @ApiModelProperty(value = "是否为网值用户")
    @NotNull(message = "是否网值用户不能为空")
    private Boolean isInNet;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserCall() {
        return userCall;
    }

    public void setUserCall(String userCall) {
        this.userCall = userCall;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getInNet() {
        return isInNet;
    }

    public void setInNet(Boolean inNet) {
        isInNet = inNet;
    }
}
