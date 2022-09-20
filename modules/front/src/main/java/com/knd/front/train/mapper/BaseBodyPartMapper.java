package com.knd.front.train.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.entity.BaseBodyPart;
import com.knd.front.home.dto.FilterCourseListDto;
import com.knd.front.home.dto.ListDto;
import com.knd.front.train.dto.BaseEquipmentDto;
import com.knd.front.train.dto.FilterFreeTrainListDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-07-02
 */
public interface BaseBodyPartMapper extends BaseMapper<BaseBodyPart> {


    List<FilterCourseListDto> getFilterCourseList(@Param("isVip") Integer isVip,
                                                  @Param("page") Page<FilterCourseListDto> page,
                                                  @Param("typeList") List<ListDto> typeList,
                                                  @Param("targetList") List<ListDto> targetList,
                                                  @Param("partList") List<ListDto> partList,
                                                  @Param("isPay") String isPay,
                                                  @Param("userId") String userId);
    FilterCourseListDto getFilterCourse(@Param("id") String id);
    List<BaseEquipmentDto> getBaseEquipment();

    List<FilterFreeTrainListDto> getFilterFreeTrainList(@Param("equipmentList") List<ListDto> equipmentList,
                                                        @Param("partList") List<ListDto> partList,
                                                        @Param("targetList") List<ListDto> targetList,
                                                        @Param("page") Page<FilterFreeTrainListDto> page,
                                                        @Param("userId") String userId,
                                                        @Param("isBuy") String isBuy);

}
