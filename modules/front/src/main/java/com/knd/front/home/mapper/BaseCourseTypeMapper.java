package com.knd.front.home.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.BaseCourseType;
import com.knd.front.home.dto.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-07-01
 */
public interface BaseCourseTypeMapper extends BaseMapper<BaseCourseType> {
    List<CourseListDto> getHomeCourseList(@Param("userId") String userId, @Param("isVip") Integer isVip);

    List<BaseCourseTypeDto> getBaseCourseType();

    List<BaseBodyPartDto> getBaseBodyPartList();

    List<BaseTargetDto> getBaseTargetList();

    @Select("select id,difficulty as name from base_difficulty where deleted = 0 and type = 'course'")
    List<BaseDifficultyDto> getBaseDifficultyList();

    List<BaseLabelDto> getBaseLabelList();
}
