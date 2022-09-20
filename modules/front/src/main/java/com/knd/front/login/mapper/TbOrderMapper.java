package com.knd.front.login.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.entity.TbOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author will
 */
public interface TbOrderMapper extends BaseMapper<TbOrder> {

    @Select("select a.* from tb_order a " +
            "JOIN tb_order_item b on a.id = b.tbOrderId  " +
            "LEFT JOIN pms_goods c on b.goodsId = c.id and c.deleted=0 " +
            "LEFT JOIN course_head d on b.goodsId = d.id and d.deleted=0 and d.releaseFlag =1 " +
            "LEFT JOIN lb_user_coach_time e on b.goodsId = e.id and e.deleted=0 " +
            "LEFT JOIN lb_user_coach_course e1 on e.coachCourseId = e1.id and e1.deleted = 0 " +
            "LEFT JOIN vip_menu f on b.goodsId = f.id and f.deleted=0" +
            "  ${ew.customSqlSegment}  ")
    List<TbOrder> getOrderList(Page<TbOrder> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
