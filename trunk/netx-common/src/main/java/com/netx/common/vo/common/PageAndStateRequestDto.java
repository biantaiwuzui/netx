package com.netx.common.vo.common;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class PageAndStateRequestDto extends PageRequestDto {

    @ApiModelProperty("模块类型")
    private MessageTypeEnum netxType;

//    @ApiModelProperty("信息类型")
//    private PushMessageDocTypeEnum typeEnum;
    @ApiModelProperty(value = "信息类型 = ['BossSuggest', 'GoodsOrderDetail', 'CurrencyDetial', 'LuckyMoneyDetail', 'ENROLLAPPLY', 'ADDCUSTOMERAGENT', 'MEETINGDETAIL', 'WZ_INVOCATION', 'WZ_GIFTDETAIL', 'WZ_SKILLDETAIL', 'WZ_DEMANDDETAIL', 'WZ_WISHDETAIL', 'ADD_FRIEND', 'SELLER_DETAIL', " +
            "'ARTICLE_DETAIL', 'ARTICLE_PAY_DETAIL', 'PRODUCT_DETAIL', " +
            "'USER_DETAIL', 'DEAL_CUSTOMERAGENT', 'PRODUCT_INVENTORY_DETAIL'" +
            ", 'WZ_MATCH', 'WZ_WALLET'," +
            "'WZ_MATCH_MEMBERS','WZ_MATCH_REASON']" )
    private List<PushMessageDocTypeEnum> typeEnums;

    public List<PushMessageDocTypeEnum> getTypeEnums() {
        return typeEnums;
    }

    public void setTypeEnums(List<PushMessageDocTypeEnum> typeEnums) {
        this.typeEnums = typeEnums;
    }

//    public PushMessageDocTypeEnum getTypeEnum() {
//        return typeEnum;
//    }
//
//    public void setTypeEnum(PushMessageDocTypeEnum typeEnum) {
//        this.typeEnum = typeEnum;
//    }

    public MessageTypeEnum getNetxType() {
        return netxType;
    }

    public void setNetxType(MessageTypeEnum netxType) {
        this.netxType = netxType;
    }
}
