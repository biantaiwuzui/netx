package com.netx.shopping.vo;

import java.util.List;

public class GetPropertyResponseVo {

    /**
     * 属性id
     */
    private String propertyId;
    /**
     * 属性名
     */
    private String propertyName;
    /**
     * 属性值列表
     */
    private List<GetValueResponseVo> valueList;

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public List<GetValueResponseVo> getValueList() {
        return valueList;
    }

    public void setValueList(List<GetValueResponseVo> valueList) {
        this.valueList = valueList;
    }
}
