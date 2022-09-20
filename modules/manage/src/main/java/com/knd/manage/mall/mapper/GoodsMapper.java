package com.knd.manage.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.mall.dto.GoodsDto;
import com.knd.manage.mall.entity.GoodsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author will
 */
public interface GoodsMapper extends BaseMapper<GoodsEntity> {
    //分页
    Page<GoodsDto> selectPageByLike(Page<GoodsDto> page, @Param("goodsName") String goodsName,@Param("goodsType") String goodsType,@Param("platform") String platform);

    //检查是否有未删除的动作使用该目标
    int selectActionCountByTargetId(String id);

    //检查是否有未删除的动作使用该部位
    int selectActionCountByPartId(String id);


    //分页
    Page<GoodsDto> selectPageByTypeLike(Page<GoodsDto> page, List<String> typeList,String goodName);
}
