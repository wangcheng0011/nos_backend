package com.knd.front.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.live.dto.GroupUserListDto;
import com.knd.front.live.entity.TrainGroupUserEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
public interface TrainGroupUserMapper extends BaseMapper<TrainGroupUserEntity> {

    @Select("SELECT tgu.id,tgu.joinDate,tgu.isAdmin,tg.groupName,u.id userId,u.nickName userNickName,ac.filePath headPicUrl FROM `lb_train_group_user` tgu,lb_train_group tg,`user` u,user_detail ud,attach ac " +
            " WHERE tg.id = tgu.trainGroupId AND u.id = tgu.userId AND  ud.userId = u.id " +
            " AND ac.id = ud.headPicUrlId " +
            " and tg.id = #{groupId} and tgu.joinStatus ='1' ORDER BY tgu.isAdmin asc ,tg.createDate DESC, tgu.joinDate DESC ")
    Page<GroupUserListDto> groupUserList(Page<GroupUserListDto> page,String groupId);

    @Select("SELECT tgu.id,tgu.joinDate,tgu.isAdmin,tg.groupName,u.id userId,u.nickName userNickName,ac.filePath headPicUrl FROM `lb_train_group_user` tgu,lb_train_group tg,`user` u,user_detail ud,attach ac " +
            " WHERE tg.id = tgu.trainGroupId AND u.id = tgu.userId AND  ud.userId = u.id " +
            " AND ac.id = ud.headPicUrlId " +
            " and tg.id = #{groupId} and tgu.joinStatus ='1' ORDER BY tgu.isAdmin asc ,tg.createDate DESC, tgu.joinDate DESC ")
    List<GroupUserListDto> groupUserListNew(String groupId);
}
