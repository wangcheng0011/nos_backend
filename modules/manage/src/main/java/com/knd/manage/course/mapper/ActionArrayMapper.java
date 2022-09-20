package com.knd.manage.course.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.course.entity.ActionArrayEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zm
 */
public interface ActionArrayMapper extends BaseMapper<ActionArrayEntity> {

    @Select(" select id,actionArrayName,actionQuantity,totalDuration from action_array " +
            " ${ew.customSqlSegment} ")
    List<ActionArrayEntity> getList(Page<ActionArrayEntity> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
