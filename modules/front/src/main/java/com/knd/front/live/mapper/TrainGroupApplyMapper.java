package com.knd.front.live.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.live.dto.ApplyListDto;
import com.knd.front.live.entity.TrainGroupApplyEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
public interface TrainGroupApplyMapper extends BaseMapper<TrainGroupApplyEntity> {

    @Select("SELECT tgu.id,tgu.applyFlag,tgu.applyDate,tgu.applyFlag,tg.groupName,u.id userId,u.nickName userNickName,ac.filePath headPicUrl FROM `lb_train_group_apply` tgu,lb_train_group tg,`user` u,user_detail ud,attach ac " +
            "WHERE tg.id = tgu.trainGroupId AND u.id = tgu.userId AND  ud.userId = u.id AND ac.id = ud.headPicUrlId " +
            "and tg.id = #{groupId} and tgu.applyFlag =#{applyStatus} ORDER BY tgu.applyDate DESC")
    Page<ApplyListDto> appleListByStatus(Page<ApplyListDto> page,String groupId,String applyStatus,String userId);
    @Select("SELECT tgu.id,tgu.applyFlag,tgu.applyDate,tgu.applyFlag,tg.groupName,u.id userId,u.nickName userNickName,ac.filePath headPicUrl FROM `lb_train_group_apply` tgu,lb_train_group tg,`user` u,user_detail ud,attach ac " +
            "WHERE tg.id = tgu.trainGroupId AND u.id = tgu.userId AND  ud.userId = u.id AND ac.id = ud.headPicUrlId " +
            "and tg.id = #{groupId} ORDER BY tgu.applyDate DESC")
    Page<ApplyListDto> appleList(Page<ApplyListDto> page,String groupId,String userId);

    @Select("select * from lb_train_group_apply " +
            "where  userId=#{userId} and trainGroupId = #{id} " +
            "order by applyDate desc " +
            "limit 1")
    TrainGroupApplyEntity selectLastApply(String userId, String id);
}
