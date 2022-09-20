package com.knd.manage.equip.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.knd.manage.equip.entity.EquipmentReportInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface EquipmentReportInfoMapper extends BaseMapper<EquipmentReportInfo> {

    @Select("   select str_to_date(turnOnTime,'%Y-%m-%d') as time,count(1) as count from equipment_report_info   " +
            "   where deleted = 0 and   str_to_date(turnOnTime,'%Y-%m-%d') BETWEEN #{beginDate} and #{endDate}   " +
            "   GROUP BY time ORDER BY time desc")
    List<Map<String,Object>> getEquipmentUseFrequencyList(@Param("beginDate")String beginDate,
                                                          @Param("endDate")String endDate);

    @Select("select count(1) from ( " +
            "select equipmentNo,MAX(turnOnTime) as turnOnTime from equipment_report_info GROUP BY equipmentNo) a " +
            " ${ew.customSqlSegment} ")
    String getEquipmentStatusNum(@Param(Constants.WRAPPER) Wrapper wrapper);
}
