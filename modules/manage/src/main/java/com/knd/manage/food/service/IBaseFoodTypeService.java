package com.knd.manage.food.service;

import com.knd.common.response.Result;
import com.knd.manage.food.entity.BaseFoodType;
import com.knd.manage.food.vo.VoSaveFoodType;
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


    Result add(String userId, VoSaveFoodType vo);

    Result edit(String userId, VoSaveFoodType vo);

    Result deleteFoodType(String userId, String id);

    Result getFoodType(String id);

    Result getFoodTypeList(String name, String currentPage);
}
