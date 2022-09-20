package com.knd.manage.basedata.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.basedata.dto.BasePageDto;
import com.knd.manage.basedata.entity.BaseFloor;
import com.knd.manage.basedata.entity.BasePage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Lenovo
 */
public interface BasePageMapper extends BaseMapper<BasePage> {

    @Select("select a.id,a.keyValue,a.version,a.pageType,b.filePath as showUrl,a.description " +
            " from base_page a " +
            " LEFT JOIN attach b on a.showUrlId = b.id " +
            " ${ew.customSqlSegment} ")
    List<BasePageDto> queryPageList(Page<BasePageDto> page,@Param(Constants.WRAPPER) Wrapper wrapper);

}
