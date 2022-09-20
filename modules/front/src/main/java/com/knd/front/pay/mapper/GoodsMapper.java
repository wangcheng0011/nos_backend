package com.knd.front.pay.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.entity.GoodsEntity;
import com.knd.front.pay.dto.GoodsDto;

import java.util.List;


/**
 * @author will
 */
public interface GoodsMapper extends BaseMapper<GoodsEntity> {
    //分页
    Page<GoodsDto> selectPageByLike(Page<GoodsDto> page, List<String> typeList,String goodName);



}
