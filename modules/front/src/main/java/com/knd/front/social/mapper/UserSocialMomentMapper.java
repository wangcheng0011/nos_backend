package com.knd.front.social.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.social.dto.MessageListDto;
import com.knd.front.social.entity.UserSocialMomentEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface UserSocialMomentMapper extends BaseMapper<UserSocialMomentEntity> {

    @Select("select ifnull(SUM(thumbup),'0') from user_social_moment where deleted=0 and userId=#{userId} ")
    long getThumbupNum(@Param("userId") String userId);
    
    @Select("select total.* from ( " +
            " select '0' as type,a.id as momentId,a.title,b.userId,'' as userName,'' as userHeadPicUrl,b.content,b.publishTime as time " +
            " from  user_social_moment a  " +
            " JOIN user_social_moment_comment b on a.id = b.momentId and b.deleted='0' " +
            " where a.deleted='0' and a.userId=#{userId} " +
            "union ALL " +
            " select '1' as type,c.id as momentId,c.title,b.userId,'' as userName,'' as userHeadPicUrl,b.content,b.publishTime as time  " +
            " from user_social_moment_comment b " +
            " join user_social_moment c on b.momentId = c.id and c.deleted='0' " +
            " where b.deleted='0' and  " +
            " b.parentId in( " +
            " select a.id from user_social_moment_comment a where a.deleted='0' and a.type='0' and a.userId = #{userId}) " +
            "union all " +
            " select '2' as type,a.id as momentId,a.title,b.userId,'' as userName,'' as userHeadPicUrl,b.content,b.publishTime as time " +
            " from  user_social_moment a  " +
            " JOIN user_social_moment_comment b on a.id = b.momentId and b.deleted='0' " +
            " where a.deleted='0' and b.callUserId=#{userId} " +
            "union all  " +
            " select '3' as type,a.id as momentId,a.title,b.userId,'' as userName,'' as userHeadPicUrl " +
            " ,'' as content,b.createDate as time  " +
            " from user_social_moment a  " +
            " JOIN user_social_moment_praise b on a.id = b.momentId and b.deleted='0' " +
            " where a.deleted='0' and a.userId=#{userId} " +
            " ) total ORDER BY time desc")
    List<MessageListDto> getAllMessageList(Page<MessageListDto> page, @Param("userId") String userId);

    @Select("select '3' as type,a.id as momentId,a.title,b.userId,'' as userName,'' as userHeadPicUrl" +
            ",'' as content,b.createDate as time " +
            "from user_social_moment a  " +
            "JOIN user_social_moment_praise b on a.id = b.momentId and b.deleted='0' and b.praise='0' " +
            "where a.deleted='0' and a.userId=#{userId}")
    List<MessageListDto> getPraiseMessageList(Page<MessageListDto> page, @Param("userId") String userId);
    
    @Select("select total.* from ( " +
            " select '0' as type,a.id as momentId,a.title,b.userId,'' as userName,'' as userHeadPicUrl,b.content,b.publishTime as time " +
            " from  user_social_moment a  " +
            " JOIN user_social_moment_comment b on a.id = b.momentId and b.deleted='0' " +
            " where a.deleted='0' and a.userId=#{userId} " +
            " union ALL " +
            " select '1' as type,c.id as momentId,c.title,b.userId,'' as userName,'' as userHeadPicUrl,b.content,b.publishTime as time  " +
            " from user_social_moment_comment b " +
            " join user_social_moment c on b.momentId = c.id and c.deleted='0' " +
            " where b.deleted='0' and  " +
            " b.parentId in( " +
            " select a.id from user_social_moment_comment a where a.deleted='0' and a.type='0' and a.userId = #{userId}) " +
            " ) total ORDER BY time desc")
    List<MessageListDto> getCommentMessageList(Page<MessageListDto> page, @Param("userId") String userId);
}
