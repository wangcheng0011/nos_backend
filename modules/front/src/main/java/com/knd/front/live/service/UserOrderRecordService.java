package com.knd.front.live.service;

import com.knd.common.response.Result;
import com.knd.front.live.entity.UserOrderRecordEntity;

import java.time.LocalDateTime;

/**
 * 用户时间预约记录表：包含小组，训练计划，教练课程预约
 * @author zm
 */
public interface UserOrderRecordService {

    /**
     * 获取用户预约记录列表
     * @param userId:预约用户id
     * @return
     */
    Result getRecordList(String userId,String current);

    /**
     * 保存预约记录
     * @return
     * @param userId:预约用户id
     * @param orderType:预约类别0课前咨询 1私教课程 2团课直播 3小组 4训练计划
     * @param orderName:预约名称
     * @param content:消息内容
     * @param orderTime:预约时间
     * @param relevancyId:关联id
     */
    Result save(String userId,String orderType, String orderName,String content, LocalDateTime orderTime,String relevancyId);

    /**
     * 移除预约记录
     * @param userId:预约用户id
     * @param relevancyId:关联id
     * @return
     */
    Result remove(String userId,String relevancyId);

    /**
     * 读取记录
     * @param id:主键id
     * @return
     */
    Result read(String id,String userId);
}
