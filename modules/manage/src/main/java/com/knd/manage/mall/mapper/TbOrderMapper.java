package com.knd.manage.mall.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.mall.dto.OrderDto;
import com.knd.manage.mall.entity.TbOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author will
 */
public interface TbOrderMapper extends BaseMapper<TbOrder> {

    Page<OrderDto> selectPageByLike(Page<OrderDto> page, @Param("orderNo") String orderNo);

    Page<TbOrder> selectOrderPage4App(Page<TbOrder> page, @Param(Constants.WRAPPER) QueryWrapper<TbOrder> tbOrderQueryWrapper);

    @Select("select SUM(amount) as amount,b.nickName,d.filePath as headPicUrl  from tb_order a " +
            "JOIN `user` b on a.userId = b.id and b.deleted=0 " +
            "LEFT JOIN user_detail c on a.userId = c.userId and c.deleted=0 " +
            "LEFT JOIN attach d on d.id = c.headPicUrlId and d.deleted=0 " +
            "WHERE a.deleted=0 and a.`status` in (2,3,4) " +
            "GROUP BY a.userId order By amount desc limit 5")
    List<Map<String, Object>> getUserPayRankingList();
}
