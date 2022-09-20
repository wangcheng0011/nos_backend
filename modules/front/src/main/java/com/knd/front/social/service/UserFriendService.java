package com.knd.front.social.service;

import com.knd.common.response.Result;

public interface UserFriendService {

    /**
     * 获取好友列表
     * @param userId
     * @return
     */
    Result getFriendsList(String current,String userId);

    /**
     * 获取粉丝列表
     * @param userId
     * @return
     */
    Result getFanList(String current,String userId);

    /**
     * 获取关注列表
     * @param userId
     * @return
     */
    Result getFollowList(String current,String userId);

}
