package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * 更新演出状态
 * Created by Yawn on 2018/8/2 0002.
 */
public class MatchAppearanceDTO {
    @ApiModelProperty(value = "根据ID更新演出次序")
    @NotBlank(message = "id必填")
    private String id;
    /**
     * 演出次序
     */
    @ApiModelProperty(value = "演出次序")
    private Integer appearanceOrder;
    /**
     * 演出时间
     */
    @ApiModelProperty(value = "演出时间")
    private Date performanceTime;
    /**
     * 演出状态
     */
    @ApiModelProperty(value = "演出状态")
    private Integer appearanceStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAppearanceOrder() {
        return appearanceOrder;
    }

    public void setAppearanceOrder(Integer appearanceOrder) {
        this.appearanceOrder = appearanceOrder;
    }

    public Date getPerformanceTime() {
        return performanceTime;
    }

    public void setPerformanceTime(Date performanceTime) {
        this.performanceTime = performanceTime;
    }

    public Integer getAppearanceStatus() {
        return appearanceStatus;
    }

    public void setAppearanceStatus(Integer appearanceStatus) {
        this.appearanceStatus = appearanceStatus;
    }
}
