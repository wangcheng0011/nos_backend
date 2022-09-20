package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.user.dto.CourseTypeNumDto;
import com.knd.front.user.dto.TrainProgramDto;
import com.knd.front.user.entity.SeriesCourseEntity;
import com.knd.front.user.entity.UserCourseEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Lenovo
 */
public interface UserCourseMapper {

    @Select(" select a.id,a.course,a.remark,c.filePath as picAttachUrl,a.videoDurationMinutes,   " +
            " a.amount,a.courseType,b.difficulty,a.trainingFlag   " +
            " from  course_head a   " +
            " LEFT JOIN attach c on a.picAttachId = c.id and c.deleted=0   " +
            " join base_difficulty b on b.id = a.difficultyId and b.deleted=0   " +
            " where a.deleted=0 and a.releaseFlag= 1")
    List<UserCourseEntity> getAllCourseList(Page<UserCourseEntity> page);

    @Select(" select a.id,a.course,a.remark,c.filePath as picAttachUrl,a.videoDurationMinutes, " +
            " a.amount,a.courseType,b.difficulty,a.trainingFlag " +
            " from course_head a " +
            " join base_difficulty b on b.id = a.difficultyId and b.deleted=0 " +
            " LEFT JOIN attach c on a.picAttachId = c.id and c.deleted=0 " +
            " WHERE a.deleted=0 and a.releaseFlag=1 and a.appHomeFlag=0 AND a.difficultyId = #{difficultyId}")
    List<UserCourseEntity> getCourseList(Page<UserCourseEntity> page,
                                         @Param("difficultyId") String difficultyId
                                         );
    @Select(" select a.id,a.course,a.remark,c.filePath as picAttachUrl,a.videoDurationMinutes, " +
            " a.amount,a.courseType,b.difficulty,a.trainingFlag,bt.id  as targetId, bt.target, bbp.id as partId, bbp.part"+
            " from course_head a " +
            " LEFT JOIN course_target ct on a.id = ct.courseId and ct.deleted=0  " +
            " LEFT JOIN base_target bt on ct.targetId = bt.id and bt.deleted=0 " +
            " LEFT JOIN course_body_part cbp ON cbp.courseId = a.id and cbp.deleted = 0" +
            " LEFT JOIN base_body_part bbp ON bbp.id = cbp.partId and bbp.deleted = 0" +
            " LEFT JOIN attach c on a.picAttachId = c.id and c.deleted=0   " +
            " join base_difficulty b on b.id = a.difficultyId and b.deleted=0   " +
            " WHERE a.deleted=0 and a.releaseFlag=1 and a.appHomeFlag=0 and a.courseType like #{courseType}")
    List<UserCourseEntity> getCourseListByCourseType(Page<UserCourseEntity> page,
                                         @Param("courseType") String courseType);

    @Select(" select a.id,a.course,a.remark,c.filePath as picAttachUrl,a.videoDurationMinutes, " +
            " a.amount,a.courseType,b.difficulty,a.trainingFlag,bt.id  as targetId, bt.target, bbp.id as partId, bbp.part"+
            " from course_head a " +
            " LEFT JOIN course_target ct on a.id = ct.courseId and ct.deleted=0  " +
            " LEFT JOIN base_target bt on ct.targetId = bt.id and bt.deleted=0 " +
            " LEFT JOIN course_body_part cbp ON cbp.courseId = a.id and cbp.deleted = 0" +
            " LEFT JOIN base_body_part bbp ON bbp.id = cbp.partId and bbp.deleted = 0" +
            " LEFT JOIN attach c on a.picAttachId = c.id and c.deleted=0   " +
            " join base_difficulty b on b.id = a.difficultyId and b.deleted=0   " +
            " WHERE a.deleted=0 and a.releaseFlag=1 ")
    List<UserCourseEntity> getCourseListAll(Page<UserCourseEntity> page);

    @Select("select a.id,a.programName,e.filePath as picAttachUrl  " +
            " from train_program a    " +
            " LEFT JOIN attach e on a.picAttachId = e.id and e.deleted=0   " +
            " where a.deleted = 0 and a.source = 0 " +
            " GROUP BY a.id  ")
    List<TrainProgramDto> getTrainAllList(Page<TrainProgramDto> page);

    @Select(" select a.id,a.programName,e.filePath as picAttachUrl " +
            " from train_program a " +
            " LEFT JOIN train_program_week_detail b on a.id = b.programId and b.deleted = 0 " +
            " LEFT JOIN train_program_day_item c on b.id = c.trainProgramWeekDetailId and c.deleted=0 " +
            " LEFT JOIN course_target d on c.itemId = d.courseId and c.itemType = '0' and d.deleted=0" +
            " LEFT JOIN attach e on a.picAttachId = e.id and e.deleted=0 " +
            " where a.deleted = 0 and a.source=0 " +
            " and d.targetId=(select targetId from user_detail where userId = #{userId}) " +
            " GROUP BY a.id")
    List<TrainProgramDto> getTrainByUserList(Page<TrainProgramDto> page, @Param("userId") String userId);

    @Select("select a.id,a.programName,b.filePath as picAttachUrl  " +
            " from train_program a " +
            " LEFT JOIN attach b on a.picAttachId = b.id and b.deleted = 0 " +
            " where a.type = #{type} and a.source=0 and a.deleted = 0 ")
    List<TrainProgramDto> getTrainByTypeList(Page<TrainProgramDto> page,@Param("type") String type);

    @Select("select a.id,a.name,a.synopsis,a.introduce,a.difficulty,a.consume,b.filePath  " +
            " from series_course_head a  " +
            " LEFT JOIN attach b on a.picAttachId = b.id and b.deleted=0  " +
            " LEFT JOIN series_course_relation c on a.id = c.seriesId and c.deleted=0 " +
            " LEFT JOIN course_target d on c.courseId = d.courseId and d.deleted=0 " +
            " where a.deleted = 0 and d.targetId=(select targetId from user_detail where userId = #{userId}) " +
            " GROUP BY a.id")
    List<SeriesCourseEntity> getSeriesCourseList(Page<SeriesCourseEntity> page, @Param("userId") String userId);

    @Select("select a.id,a.name,a.synopsis,a.introduce,a.difficulty,a.consume,b.filePath  " +
            " from series_course_head a  " +
            " LEFT JOIN attach b on a.picAttachId = b.id and b.deleted=0  " +
            " where a.deleted = 0")
    List<SeriesCourseEntity> getSeriesCourseAllList(Page<SeriesCourseEntity> page);

    @Select("select a.*,b.filePath from series_course_head a " +
            " LEFT JOIN attach b on a.picAttachId = b.id and b.deleted=0 " +
            " where a.deleted = 0 and a.id = #{id}")
    SeriesCourseEntity getSeriesCourseById(@Param("id") String id);


    @Select("select c.type,count(1) as num " +
            " from course_head a  " +
            " LEFT JOIN course_type b on a.id = b.courseId and b.deleted = 0 " +
            " LEFT JOIN base_course_type c on b.courseTypeId = c.id and c.deleted = 0 " +
            " ${ew.customSqlSegment} ")
    List<CourseTypeNumDto> getCourseTypeNumList(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select b.filePath from series_course_attach a  " +
            " LEFT JOIN attach b on a.attachId = b.id and b.deleted=0 " +
            " where a.seriesId=#{seriesId} and a.deleted = 0")
    List<String> getAttachList(@Param("seriesId") String seriesId);

    @Select("select a.id,a.course,a.remark,b.filePath as picAttachUrl,a.videoDurationMinutes, " +
            " a.amount,a.courseType,d.difficulty,a.trainingFlag " +
            " from course_head a  " +
            " LEFT JOIN attach b on a.picAttachId = b.id and b.deleted=0 " +
            " JOIN base_difficulty d on d.id = a.difficultyId and d.deleted=0 " +
            " where a.deleted = 0 and a.releaseFlag =1 and a.id in  " +
            " (select courseId from series_course_relation where seriesId = #{seriesId} and deleted = 0 )")
    List<UserCourseEntity> getCourseEntityList(@Param("seriesId") String seriesId);

    @Select("select count(1) from train_course_head_info " +
            " where userId = #{userId} and courseHeadId = #{courseHeadId} and deleted=0")
    Integer getTrainNum(@Param("userId") String userId,@Param("courseHeadId") String courseHeadId);

}
