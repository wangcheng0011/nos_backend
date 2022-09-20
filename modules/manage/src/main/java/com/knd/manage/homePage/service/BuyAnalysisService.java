package com.knd.manage.homePage.service;

import com.knd.common.response.Result;

import java.util.List;
import java.util.Map;

/**
 * @author zm
 */
public interface BuyAnalysisService {

    /**
     * 查询各难度课程数量
     * @return
     */
    Result queryDiffCourseNum();

    /**
     * 查询各分类课程比例
     * @return
     */
    Result queryTypeCourseNum();

    /**
     * 查询课程,系列课程，自由训练，训练计划数量与日增长
     * @return
     */
    Result queryCourseByTypeNum(String beginDate,String endDate);

    /**
     * 查询减脂/塑性/增肌/系列课程数量与日期增长
     * @return
     */
    Result queryCourseByTargetNum(String beginDate,String endDate);

    /**
     * 查询各类型用户数量
     * @return
     */
    Result queryDiffUserNum();

    /**
     * 查询各类型用户,课程，计划数量与增长
     * @return
     */
    Result queryDiffUserDeviceCoursePlanNum();

    /**
     * 查询vip会员比例
     * @return
     */
    Map<String,Double> getPercentOfVip();

    /**
     * 查询性别比例
     * @return
     */
    Map<String,Double> getGenderPercent();


    /**
     * 查询年龄比例
     * @return
     */
    List<Map<String,Double>> queryAgePercent();


    /**
     * 查询周会员增长详情
     * @return
     */
    Map<String,Object> queryUserGrowthOfWeeks(String beginDate,String endDate);

    /**
     * 查询日会员增长详情
     * @return
     */
    List<Map<String, Object>> queryUserGrowthOfDay(String beginDate,String endDate);

    /**
     * 查询配件购买量
     * @return
     */
    List<Map<String,Integer>> getPartsPurchase();

    /**
     * 查询全部的设备位置信息
     * @return
     */
    Result queryEquipmentAddress(String type);

    /**
     * 查询用户消费排行榜
     * @return
     */
    List<Map<String,Object>> queryUserPayRanking();


    List<Map<String, Object>> getFaultAnalysis();

    /**
     * 设备分析
     * @return
     */
    Result getDeviceAnalysis();

    List<Map<String, Object>> getViewRecords(String beginDate, String endDate);

    /**
     * 配件使用分析
     * @author will
     * @date 2021/9/2 16:05
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getPartsUsageAnalysis();

    /**
     * 训练难度对应得数量
     * @return
     */
    List<Map<String, Object>> getTrainDifficultNum();

    /**
     * 器材使用频率
     * @param beginDate
     * @param endDate
     * @return
     */
    Result queryEquipmentUseFrequency(String beginDate,String endDate);

    /**
     * 获取设备状态数量
     *
     * @return
     */
    Map<String, Object> getEquipmentStatusNum();
}
