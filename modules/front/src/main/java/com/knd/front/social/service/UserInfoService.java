package com.knd.front.social.service;

import com.knd.common.response.Result;

public interface UserInfoService {

    /**
     * 获取对方用户个人数据
     * @param userId
     * @param friendId
     * @return
     */
    Result getOtherInfo(String userId, String friendId);

    /**
     * 获取记录
     * @param userId
     * @return
     */
    Result getRecord(String userId);

    /**
     * 获取相册
     * @param userId
     * @return
     */
    Result getAlbum(String userId);

    /**
     * 获取动态
     * @param userId
     * @return
     */
    Result getMoment(String current,String userId,String friendId);

    /**
     * 获取单条动态
     * @param momentId
     * @return
     */
    Result getOneMoment(String userId,String momentId);

    /**
     * 获取课程
     * @param userId
     * @return
     */
    Result getCourse(String userId);
}
