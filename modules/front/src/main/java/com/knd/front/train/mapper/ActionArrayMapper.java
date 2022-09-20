package com.knd.front.train.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.entity.ActionArray;
import com.knd.front.train.dto.ActionArrayDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author will
 */
public interface ActionArrayMapper extends BaseMapper<ActionArray> {

   // List<ActionArrayDto> getUserActionArrayList(@Param("page") Page<ActionArrayDto> tPage, @Param("getActionArrayRequest") GetActionArrayRequest getActionArrayRequest);
    List<ActionArrayDto> getUserActionArrayList(@Param("page") Page<ActionArrayDto> tPage, @Param(Constants.WRAPPER) QueryWrapper<ActionArray> queryWrapper);
}
