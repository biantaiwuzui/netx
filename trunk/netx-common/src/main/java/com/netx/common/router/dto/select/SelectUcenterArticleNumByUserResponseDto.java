package com.netx.common.router.dto.select;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 用户图文、音视数量统计Response类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class SelectUcenterArticleNumByUserResponseDto {
    @ApiModelProperty("图文数量")
    private int imageWriteNum;

    @ApiModelProperty("音视数量")
    private int vedioNum;

    public int getImageWriteNum() {
        return imageWriteNum;
    }

    public void setImageWriteNum(int imageWriteNum) {
        this.imageWriteNum = imageWriteNum;
    }

    public int getVedioNum() {
        return vedioNum;
    }

    public void setVedioNum(int vedioNum) {
        this.vedioNum = vedioNum;
    }

    @Override
    public String toString() {
        return "SelectUcenterArticleNumByUserResponseDto{" +
                "imageWriteNum=" + imageWriteNum +
                ", vedioNum=" + vedioNum +
                '}';
    }
}
