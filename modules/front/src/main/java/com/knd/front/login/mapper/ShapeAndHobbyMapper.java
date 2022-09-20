package com.knd.front.login.mapper;

import com.knd.front.login.dto.HobbyDto;
import com.knd.front.login.dto.ShapeDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Lenovo
 */
public interface ShapeAndHobbyMapper {

    @Select("select a.id,hobby,b.filePath as selectUrlPath,c.filePath as unSelectUrlPath " +
            " from base_part_hobby a " +
            " LEFT JOIN attach b on a.selectUrlId = b.id " +
            " LEFT JOIN attach c on a.unSelectUrlId = c.id " +
            " where a.deleted = 0 ORDER BY FIELD(hobby,'其他')")
    List<HobbyDto> getHobbyList();

    @Select("select a.id,shape,b.filePath as selectUrlPath,c.filePath as unSelectUrlPath" +
            " from base_part_shape a " +
            " LEFT JOIN attach b on a.selectUrlId = b.id " +
            " LEFT JOIN attach c on a.unSelectUrlId = c.id " +
            " where a.deleted = 0 and sex = #{sex}")
    List<ShapeDto> getShapeList(@Param("sex") String sex);

    @Select("select shape from base_part_shape where id = #{id}")
    String getShape(@Param("id") String id);
}
