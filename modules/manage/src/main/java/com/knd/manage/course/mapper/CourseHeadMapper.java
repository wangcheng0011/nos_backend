package com.knd.manage.course.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.knd.manage.course.entity.CourseHead;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.homePage.dto.DiffCourseNumDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface CourseHeadMapper extends BaseMapper<CourseHead> {
    //根据条件获取符合要求的课程id,去重
    IPage<String> selectCourseIdListBySome(IPage<String> page, @Param("course") String course, @Param("typeid") String typeid,
                                           @Param("targetid") String targetid, @Param("partid") String partid,
                                           @Param("releaseFlag") String releaseFlag, @Param("courseType") String courseType);

    //根据条件获取符合要求的课程id,去重
    List<String> selectCourseIdListBySome2(@Param("course") String course, @Param("typeid") String typeid,
                                           @Param("targetid") String targetid, @Param("partid") String partid,
                                           @Param("releaseFlag") String releaseFlag, @Param("courseType") String courseType);


    /**
     * 获取各难度课程的数量
     * @return
     */
    @Select("select b.difficulty as typeName,count(1) as num from course_head a " +
            "JOIN base_difficulty b on a.difficultyId = b.id and b.deleted=0 " +
            "where a.deleted=0 and a.releaseFlag=1 GROUP BY a.difficultyId ")
    List<DiffCourseNumDto> getDiffCourseNum();

    /**
     * 获取各分类课程的数量
     * @return
     */
    @Select("select c.type as typeName,count(1) as num from course_type a  " +
            "join course_head b on a.courseId = b.id and b.deleted=0 and b.releaseFlag=1   " +
            "join base_course_type c on a.courseTypeId = c.id and c.deleted=0  " +
            "where a.deleted=0  " +
            "GROUP BY a.courseTypeId")
    List<DiffCourseNumDto> getTypeCourseNum();

    //根据日期分组每天得课程数量
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
            "     (SELECT str_to_date(createDate,'%Y-%m-%d') as time , count(id) as count FROM course_head where deleted=0 GROUP BY  time ) a " +
            "     JOIN (  SELECT str_to_date(createDate,'%Y-%m-%d') as time , count(id) as count FROM course_head where deleted=0 GROUP BY  time) b ON a.time >= b.time  " +
            "   GROUP BY " +
            "     time  " +
            "   ORDER BY " +
            "   time ASC  " +
            "   ) a) data " +
            " right join  " +
            " (SELECT @date := DATE_ADD(@date, interval 1 day) day from  " +
            " (SELECT @date := DATE_ADD(#{beginDate}, interval -1 day) from course_head) " +
            "  days limit #{days}) day_list on day_list.day = data.time ORDER BY date ASC")
    List<Map<String, Object>> queryCourseTotal(Integer days, String beginDate, String endDate);

    //根据目标进行日期分组每天得课程数量
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
            "     (SELECT str_to_date(a.createDate,'%Y-%m-%d') as time , count(a.id) as count FROM course_head a " +
            "       JOIN course_target b on a.id = b.courseId and b.deleted=0  " +
            "       JOIN base_target c on b.targetId = c.id and c.deleted=0 " +
            "       where a.deleted=0 and c.target like '%${target}%' GROUP BY  time ) a " +
            "     JOIN (  SELECT str_to_date(a.createDate,'%Y-%m-%d') as time , count(a.id) as count FROM course_head a " +
            "               JOIN course_target b on a.id = b.courseId and b.deleted=0  " +
            "               JOIN base_target c on b.targetId = c.id and c.deleted=0 " +
            "               where a.deleted=0 and c.target like '%${target}%' GROUP BY  time) b ON a.time >= b.time  " +
            "   GROUP BY " +
            "     time  " +
            "   ORDER BY " +
            "   time ASC  " +
            "   ) a) data " +
            " right join  " +
            " (SELECT @date := DATE_ADD(@date, interval 1 day) day from  " +
            " (SELECT @date := DATE_ADD(#{beginDate}, interval -1 day) from course_head) " +
            "  days limit #{days}) day_list on day_list.day = data.time ORDER BY date ASC")
    List<Map<String, Object>> queryCourseByTarget(Integer days, String beginDate, String endDate,String target);

}
