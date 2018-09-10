package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;

/**
 * Create by allen on 17-9-9
 */
@ApiModel
public class WetherExistEvaluateReponseDto{

    /**
     * 是否存在
     */
    private boolean wetherExist;

    /**
     * 评论次数
     */
    private int count;

    public boolean isWetherExist() {
        return wetherExist;
    }

    public void setWetherExist(boolean wetherExist) {
        this.wetherExist = wetherExist;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
