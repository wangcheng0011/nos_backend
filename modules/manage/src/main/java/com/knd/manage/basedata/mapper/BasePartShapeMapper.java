package com.knd.manage.basedata.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.basedata.dto.PartShapeDto;
import com.knd.manage.basedata.entity.BasePartShape;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Lenovo
 */
public interface BasePartShapeMapper extends BaseMapper<BasePartShape> {

    @Select(" select a.id,a.shape,a.remark,a.sex,b.part,a.partId " +
            " from base_part_shape a " +
            " join base_body_part b on a.partId = b.id " +
            " ${ew.customSqlSegment}")
    List<PartShapeDto> getList(Page<PartShapeDto> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select part from base_body_part where id = #{id}")
    String getPart(@Param("id") String id);
}
