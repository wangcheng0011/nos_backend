package com.knd.manage.mall.service;

import com.knd.common.response.Result;
import com.knd.manage.mall.entity.AttrEntity;
import com.knd.manage.mall.entity.CategoryEntity;
import com.knd.manage.mall.request.CreateAttrRequest;
import com.knd.manage.mall.request.UpdateAttrRequest;
import com.knd.mybatis.SuperService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface ICategoryService extends SuperService<CategoryEntity> {


    Result getAttrByCategoryId(String categoryId);

    Result getCategoryList(String categoryName, String current);

    Result addAttr(UpdateAttrRequest vo);

    Result updateAttr(UpdateAttrRequest vo);

    Result deleteAttr(String id);
}
