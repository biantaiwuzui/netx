package com.netx.common.vo.friends;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-8-28
 */
public class SelectByNumOrPhoneRequestDto {
    @NotNull
    private int type;
    @NotBlank
    private String value;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SelectByNumOrPhoneRequest{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
