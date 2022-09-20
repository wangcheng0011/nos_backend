package com.knd.manage.basedata.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.basedata.dto.BaseElementDto;
import com.knd.manage.basedata.dto.BaseFloorDto;
import com.knd.manage.basedata.entity.BaseFloor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Lenovo
 */
public interface BaseFloorMapper extends BaseMapper<BaseFloor> {

    @Select("select a.id,a.keyValue,a.version,a.floorType,b.filePath as showUrl,a.description " +
            " from base_floor a " +
            " LEFT JOIN attach b on a.showUrlId = b.id " +
            " ${ew.customSqlSegment} ")
    List<BaseFloorDto> queryFloorList(Page<BaseFloorDto> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select(" select a.id,a.keyValue,a.sort,a.description,b.filePath as showUrl  " +
            " from base_element a " +
            " LEFT JOIN attach b on a.showUrlId = b.id " +
            " where a.deleted = 0 and floorId = #{floorId} " +
            " order by  CONVERT(a.sort,SIGNED) asc ")
    List<BaseElementDto> getElementList(@Param("floorId") String floorId);

}
