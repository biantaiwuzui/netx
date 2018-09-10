package com.netx.ucenter.service.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FriendServiceProvider {

    @Autowired
    private FriendsService friendsService;

    @Autowired
    private UserFriendService userFriendService;

    public FriendsService getFriendsService() {
        return friendsService;
    }

    public UserFriendService getUserFriendService() {
        return userFriendService;
    }
}
