package com.knd.manage.basedata.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.basedata.entity.UserCoachEntity;
import com.knd.manage.course.entity.CoachList;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserCoachMapper extends BaseMapper<UserCoachEntity> {

    @Select("select a.id,b.nickName,b.mobile,b.registTime,a.userId,a.picAttachId " +
            "from lb_user_coach a  " +
            "JOIN `user` b on a.userId = b.id " +
            "  ${ew.customSqlSegment}  ")
    List<CoachList> getCoachList(Page<CoachList> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
