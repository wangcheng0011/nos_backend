package com.knd.manage.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.mall.dto.GoodsDto;
import com.knd.manage.mall.entity.AttrEntity;
import org.apache.ibatis.annotations.Param;


/**
 * @author will
 */
public interface AttrMapper extends BaseMapper<AttrEntity> {
    //分页
    Page<GoodsDto> selectPageByLike(Page<GoodsDto> page, @Param("goodsName") String goodsName);


}
