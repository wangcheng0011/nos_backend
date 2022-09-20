package com.knd.manage.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.mall.dto.GoodsDto;
import com.knd.manage.mall.entity.AttrEntity;
import com.knd.manage.mall.entity.CategoryEntity;
import org.apache.ibatis.annotations.Param;


/**
 * @author will
 */
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
    //分页
//    Page<CategoryEntity> selectPageByLike(Page<CategoryEntity> page, @Param("categoryName") String categoryName);


}
