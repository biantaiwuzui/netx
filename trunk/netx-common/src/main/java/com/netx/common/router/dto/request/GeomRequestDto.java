package com.netx.common.router.dto.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 经纬度Request类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-16
 */
public class GeomRequestDto {
    @Override
    public String toString() {
        return "GeomRequestDto{" +
                "id='" + id + '\'' +
                ", currentPage=" + currentPage +
                ", tagName='" + tagName + '\'' +
                ", size=" + size +
                '}';
    }

    @ApiModelProperty("业务id")
    private String id;

    @ApiModelProperty("当前页")
    private int currentPage;

    @ApiModelProperty("图文标签name")
    private String tagName;

    @ApiModelProperty("当前获取数据量")
    private int size;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
