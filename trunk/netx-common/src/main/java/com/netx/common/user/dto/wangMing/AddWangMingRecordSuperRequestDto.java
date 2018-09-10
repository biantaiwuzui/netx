package com.netx.common.user.dto.wangMing;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 更新网名并添加相应的流水表记录要求传递的数据（作为其他网名dto的父类，方便维护）
 * @author 李卓
 */
public class AddWangMingRecordSuperRequestDto {

    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty("用户id")
    private String userId;

    @NotBlank(message = "关联模型不能为空，使用模型的类名，如：user,UserProfile等")
    @ApiModelProperty("关联模型，使用模型的类名，如：user,UserProfile等")
    private String relatableType;

    @NotBlank(message = "关联主键不能为空，若没有关联主键，则设为0")
    @ApiModelProperty("关联主键：关联具体得分的uuid，没有就是0")
    private String relatableId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRelatableType() {
        return relatableType;
    }

    public void setRelatableType(String relatableType) {
        this.relatableType = relatableType;
    }

    public String getRelatableId() {
        return relatableId;
    }

    public void setRelatableId(String relatableId) {
        this.relatableId = relatableId;
    }

    @Override
    public String toString() {
        return "AddWangMingSuperRequestDto{" +
                "userId='" + userId + '\'' +
                ", relatableType='" + relatableType + '\'' +
                ", relatableId='" + relatableId + '\'' +
                '}';
    }
}
