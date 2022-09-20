package com.knd.front.food.service;

import com.knd.common.response.Result;
import com.knd.front.food.entity.BaseFoodType;
import com.knd.front.food.vo.VoSaveFoodType;
import com.knd.mybatis.SuperService;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface IBaseFoodTypeService extends SuperService<BaseFoodType> {


    Result add(VoSaveFoodType vo);

    Result edit(VoSaveFoodType vo);

    Result deleteFoodType(String userId, String id);

    Result getFoodType(String id);

    Result getFoodTypeList(String name, String currentPage);
}
