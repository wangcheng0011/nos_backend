package com.knd.manage.food.service;

import com.knd.common.response.Result;
import com.knd.manage.food.entity.Food;
import com.knd.manage.food.vo.VoSaveFood;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface IFoodService extends SuperService<Food> {


    Result add(VoSaveFood vo);

    Result edit(VoSaveFood vo);

    Result deleteFood(String userId, String id);

    Result getFood(String id);

    Result getFoodList(String name, String current);
}
