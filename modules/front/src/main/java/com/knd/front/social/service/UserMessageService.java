package com.knd.front.social.service;

import com.knd.common.response.Result;

/**
 * 用户消息列表
 */
public interface UserMessageService {


    /**
     * 获取全部消息列表
     * @param current
     * @param userId
     * @return
     */
    Result getAllMessage(String current,String userId);
    /**
     *  查询评论信息列表
     * @param userId
     * @return
     */
    Result getComment(String current,String userId);

    /**
     * 查询被@消息列表
     * @param userId
     * @return
     */
    Result getBeCall(String current,String userId);

    /**
     * 被赞信息列表
     * @param userId
     * @return
     */
    Result getBePraised(String current,String userId);
}
