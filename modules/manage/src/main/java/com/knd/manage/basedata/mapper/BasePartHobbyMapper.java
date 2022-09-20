package com.knd.manage.basedata.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.basedata.dto.PartHobbyDto;
import com.knd.manage.basedata.entity.BasePartHobby;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface BasePartHobbyMapper extends BaseMapper<BasePartHobby> {

    @Select(" select a.id,a.hobby,a.remark,b.part,a.partId " +
            " from base_part_hobby a " +
            " join base_body_part b on a.partId = b.id " +
            " ${ew.customSqlSegment}")
    List<PartHobbyDto> getList(Page<PartHobbyDto> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select part from base_body_part where id = #{id}")
    String getPart(@Param("id") String id);

}
