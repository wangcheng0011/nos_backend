package com.knd.manage.basedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.basedata.dto.BaseFloorSortDto;
import com.knd.manage.basedata.entity.BasePageFloor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Lenovo
 */
public interface BasePageFloorMapper extends BaseMapper<BasePageFloor> {

    @Select("select a.sort,b.*,c.filePath as showUrl from base_page_floor a " +
            " LEFT JOIN base_floor b on a.floorId = b.id and b.deleted=0 " +
            " LEFT JOIN attach c on b.showUrlId = c.id " +
            " where a.pageId= #{pageId} " +
            " order by  CONVERT(a.sort,SIGNED) asc ")
    List<BaseFloorSortDto> getFloorList(@Param("pageId") String pageId);
}
