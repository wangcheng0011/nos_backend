package com.knd.manage.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.course.dto.CourseDto;
import com.knd.manage.course.entity.SeriesCourseHead;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author Lenovo
 */
public interface SeriesCourseHeadMapper extends BaseMapper<SeriesCourseHead> {

    @Select("select a.id,a.course,bb.type as types,cc.target as targets,dd.part as parts  " +
            " from course_head a  " +
            " LEFT JOIN course_type b on a.id = b.courseId and b.deleted=0 " +
            " LEFT JOIN base_course_type bb on b.courseTypeId = bb.id and bb.deleted=0 " +
            " LEFT JOIN course_target c on a.id = c.courseId and c.deleted=0 " +
            " LEFT JOIN base_target cc on c.targetId = cc.id and cc.deleted=0 " +
            " LEFT JOIN course_body_part d on a.id = d.courseId and d.deleted=0 " +
            " LEFT JOIN base_body_part dd on d.partId = dd.id and dd.deleted=0 " +
            " where a.id in  " +
            " (select courseId from series_course_relation  " +
            "  where seriesId = #{id} and deleted=0)")
    List<CourseDto> getCourseList(@Param("id") String id);

    //根据日期分组每天得系列课程数量
    @Select("SELECT day_list.day as date,IFNULL(data.allNum, 0) as count from  " +
            " (SELECT " +
            "   a.time time, " +
            "   a.allNum allNum " +
            " FROM " +
            "   ( " +
            "   SELECT " +
            "     a.time, " +
            "     SUM( b.count ) AS allNum  " +
            "   FROM " +
            "     (SELECT str_to_date(createDate,'%Y-%m-%d') as time , count(id) as count FROM series_course_head where deleted=0 GROUP BY  time ) a " +
            "     JOIN (  SELECT str_to_date(createDate,'%Y-%m-%d') as time , count(id) as count FROM series_course_head where deleted=0 GROUP BY  time) b ON a.time >= b.time  " +
            "   GROUP BY " +
            "     time  " +
            "   ORDER BY " +
            "   time ASC  " +
            "   ) a) data " +
            " right join  " +
            " (SELECT @date := DATE_ADD(@date, interval 1 day) day from  " +
            " (SELECT @date := DATE_ADD(#{beginDate}, interval -1 day) from series_course_head) " +
            "  days limit #{days}) day_list on day_list.day = data.time ORDER BY date ASC")
    List<Map<String, Object>> querySeriesCourseTotal(Integer days, String beginDate, String endDate);
}
