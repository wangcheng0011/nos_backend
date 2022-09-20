package com.knd.manage.homePage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.homePage.entity.AmapDataEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface AmapDataMapper extends BaseMapper<AmapDataEntity> {

    //查询每个省/市/区的设备数量
    @Select("select ${column} as adcode,count(1) as count from amap_data GROUP BY ${column}")
    List<Map<String,Object>> getCountByColumn(@Param("column") String column);
}
