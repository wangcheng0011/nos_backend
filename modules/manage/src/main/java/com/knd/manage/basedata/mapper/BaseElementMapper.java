package com.knd.manage.basedata.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.basedata.dto.BaseElementDto;
import com.knd.manage.basedata.entity.BaseElement;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Lenovo
 */
public interface BaseElementMapper extends BaseMapper<BaseElement> {

    @Select("select a.id,a.keyValue,a.version,a.sort,b.filePath as showUrl,a.description " +
            " from base_element a " +
            " LEFT JOIN attach b on a.showUrlId = b.id " +
            " ${ew.customSqlSegment} ")
    List<BaseElementDto> getList(Page<BaseElementDto> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
