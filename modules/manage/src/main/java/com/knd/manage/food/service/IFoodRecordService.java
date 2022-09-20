package com.knd.manage.food.service;

import com.knd.common.response.Result;
import com.knd.manage.food.entity.FoodRecord;
import com.knd.manage.food.vo.VoQueryFoodRecord;
import com.knd.manage.food.vo.VoSaveFoodRecord;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface IFoodRecordService extends SuperService<FoodRecord> {


    Result add( VoSaveFoodRecord vo);

    Result edit(VoSaveFoodRecord vo);

    Result deleteFoodRecord(String userId, String id);

    Result getFoodRecord(String id);

    Result getFoodRecordList(VoQueryFoodRecord voQueryFoodRecord, String current);
}
