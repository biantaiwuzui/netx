package com.netx.ucenter.vo.response;


import com.netx.common.user.model.UserSynopsisData;
import io.swagger.annotations.ApiModelProperty;

public class SelectUserCommonResponse{
    @ApiModelProperty("用户基本信息")
    private UserSynopsisData synopsisData;

    @ApiModelProperty("业务id")
    private String id;

    public UserSynopsisData getSynopsisData() {
        return synopsisData;
    }

    public void setSynopsisData(UserSynopsisData synopsisData) {
        this.synopsisData = synopsisData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "SelectUserBlackResponse{" +
                "synopsisData=" + synopsisData +
                ", id='" + id + '\'' +
                '}';
    }
}
