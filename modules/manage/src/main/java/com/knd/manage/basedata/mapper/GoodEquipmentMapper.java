package com.knd.manage.basedata.mapper;

import com.knd.manage.basedata.entity.GoodEquipment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodEquipmentMapper {

    @Select("select a.id,a.goodsName as equipment,a.goodsDesc as remark from pms_goods a  " +
            " where a.publishStatus=1 and a.deleted=0 and a.categoryId='2'")
    List<GoodEquipment> getList();

}
