package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel(description = "添加投诉的Dto")
public class ArbitrationAddComplaintRequestDto {

    @ApiModelProperty("被投诉者的网号，type=2时，必填")
    private String  toUserNetworkNum;


    @ApiModelProperty("投诉的主题")
    @NotBlank(message = "投诉的主题不能为空")
    private String theme;

    @NotBlank(message = "投诉或者申诉的理由不能为空")
    @ApiModelProperty(value = "投诉或者申诉的理由,必填")
    private String reason;

    @ApiModelProperty("投诉的类型:\n" +
            "1.商品订单投诉类型(订单相关)\n" +
            "2.网号其他类型(言行举止相关)\n"+
            "3.需求投诉\n"+
            "4.心愿投诉\n"+
            "5.技能投诉\n" +
            "6.活动投诉"
    )
    @NotNull(message = "type投诉类型不能为空")
    private Integer type = 2;

    @ApiModelProperty("投诉的事件ID:例如如果type是订单的话,那typeId就是这个订单ID号，心愿就是心愿id，需求的话就是需求id，技能的是技能id，必填")
    private String typeId;

    @ApiModelProperty(value = "投诉者用户id，可填可不填，填就传用户id，不填也要传该用户Token值")
    private String fromUserId;

    @ApiModelProperty("被投诉者用户id，如果该投诉类型是除了type=2之外，该值为商品订单的商家发起人，需求，心愿，技能的发起者之一，根据你类型自己判断")
    private String toUserId;

    @ApiModelProperty(value = "图片上传后的名字,已，隔开，如果要上传图片，就把上传图片返回的字符串赋值给改字段")
    private String url;

    public String getTypeId() { return typeId; }

    public void setTypeId(String typeId) { this.typeId = typeId; }

    public Integer getType() { return type; }

    public void setType(Integer type) { this.type = type; }

    public String getToUserNetworkNum() {
        return toUserNetworkNum==null?null:toUserNetworkNum.trim();
    }

    public void setToUserNetworkNum(String toUserNetworkNum) {
        this.toUserNetworkNum = toUserNetworkNum;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getReason() {
        return reason==null?null:reason.trim();
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFromUserId() {
        return fromUserId==null?null:fromUserId.trim();
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}
