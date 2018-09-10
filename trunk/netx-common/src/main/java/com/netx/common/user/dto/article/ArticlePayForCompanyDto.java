package com.netx.common.user.dto.article;

import com.netx.common.user.enums.PayTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


public class ArticlePayForCompanyDto {

    @NotBlank(message = "事件id不能为空")
    @ApiModelProperty(value = "事件id（例如：如果是咨讯支付押金，则为咨讯id）", required = true)
    private String typeId;

    @NotNull(message = "冻结金额不能为空")
    @ApiModelProperty(value = "冻结金额", required = true)
    private BigDecimal amount;

    @ApiModelProperty(value = "交易方式【零钱：PT_NONE；微信：PT_WECHAT；支付宝：PT_ALI】",required = true)
    @NotNull(message = "支付方式不能为空")
    private PayTypeEnum tradeType;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PayTypeEnum getTradeType() {
        return tradeType;
    }

    public void setTradeType(PayTypeEnum tradeType) {
        this.tradeType = tradeType;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
