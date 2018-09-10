package com.netx.worth.vo;

import com.netx.worth.enums.MatchAttendType;
import io.swagger.annotations.ApiModelProperty;

/**
 * 扫码出席成功接口
 * Created by Yawn on 2018/9/1 0001.
 */
public class MatchAttendVO {
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "头像")
    private String headImage;
    @ApiModelProperty(value = "参赛者类型", notes = "PARTICIPANT(1, \"参赛者\"),\n" +
            "    AUDNENT(2, \"观众\");")
    private String matchAttendType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getMatchAttendType() {
        return matchAttendType;
    }

    public void setMatchAttendType(String matchAttendType) {
        this.matchAttendType = matchAttendType;
    }
}
