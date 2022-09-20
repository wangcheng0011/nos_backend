package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.user.dto.UserCoursePurchasedDto;
import com.knd.front.user.entity.UserCourseEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserPurchasedMapper {

    @Select("select a.id,a.course,a.remark,a.picAttachUrl,a.videoDurationMinutes,a.courseType,a.amount,c.difficulty  " +
            " from course_head a  " +
            " LEFT JOIN user_pay b on a.id = b.payId and b.type = 0 and b.deleted = 0 " +
            " LEFT JOIN base_difficulty c on a.difficultyId = c.id and c.deleted = 0 " +
            " where b.userId = #{userId} and a.releaseFlag =1")
    List<UserCourseEntity> getPurchasedCourseList(Page<UserCoursePurchasedDto> page,
                                                  @Param("userId") String userId);
}
