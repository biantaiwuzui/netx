package com.netx.common.vo.common;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Create by wongloong on 17-8-26
 */
public class QueryWzCommonAddFriendRequest extends PageRequestDto {
    @NotBlank
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "QueryWzCommonAddFriendRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
