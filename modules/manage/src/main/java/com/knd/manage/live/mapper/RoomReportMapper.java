package com.knd.manage.live.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.manage.live.dto.RoomReportDto;
import com.knd.manage.live.entity.RoomReportEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RoomReportMapper extends BaseMapper<RoomReportEntity> {
    //获取举报列表
    @Select("select u.nickName reportUserName,u.mobile reportUserMoblie,r.id,r.roomId,r.reportUserId,r.content,r.represent,r.type,r.reportTime,r.beginTime from lb_room_report r left join  user u on r.reportUserId = u.id  " +
            " ${ew.customSqlSegment} ")
    Page<RoomReportDto> selectRoomReportPage(Page<RoomReportDto> page,@Param(Constants.WRAPPER) Wrapper wrapper);
}
