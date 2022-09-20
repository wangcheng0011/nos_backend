package com.knd.front.home.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.entity.CourseHead;
import com.knd.front.user.entity.UserCourseEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-07-01
 */
public interface CourseHeadMapper extends BaseMapper<CourseHead> {

    @Select(" select a.id,a.course,a.remark,a.picAttachUrl,case when a.videoDurationSeconds = 00 then a.videoDurationMinutes else a.videoDurationMinutes+1 end  as videoDurationMinutes ,a.appHomeFlag,a.releaseFlag," +
            " a.amount,a.courseType,d.difficulty,a.createDate " +
            " from course_head a  " +
            " LEFT JOIN course_body_part b on a.id = b.courseId and b.deleted=0 " +
            " LEFT JOIN course_target c on a.id = c.courseId and c.deleted=0 " +
            " LEFT JOIN base_difficulty d on a.difficultyId = d.id and d.deleted=0 " +
            " LEFT JOIN base_part_hobby e on b.partId=e.partId and e.deleted=0 " +
            " ${ew.customSqlSegment} ")
    List<UserCourseEntity> getList(Page<UserCourseEntity> page,@Param(Constants.WRAPPER) Wrapper wrapper);

    /*@Select("select a.id,a.course,a.remark,a.picAttachUrl,a.videoDurationMinutes," +
            "a.amount,a.courseType " +
            "from course_head a  " +
            "where a.id IN " +
            "(SELECT DISTINCT(cbp.courseId) " +
            "from base_part_hobby bph,course_body_part cbp " +
            "where cbp.partId=bph.partId and bph.id =#{hobbyId}) "+
            "and a.deleted = '0' AND a.courseType = #{courseType}")
    List<UserCourseEntity> getListByBobby(Page<UserCourseEntity> page,@Param("hobbyId") String hobbyId,@Param("courseType") Integer courseType);*/
}
