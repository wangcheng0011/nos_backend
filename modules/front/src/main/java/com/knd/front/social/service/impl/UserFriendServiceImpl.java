package com.knd.front.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.Attach;
import com.knd.front.entity.User;
import com.knd.front.entity.UserDetail;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.social.dto.UserFansDto;
import com.knd.front.social.dto.UserFollowDto;
import com.knd.front.social.dto.UserFriendsDto;
import com.knd.front.social.entity.UserSocialFollowEntity;
import com.knd.front.social.entity.UserSocialRelationEntity;
import com.knd.front.social.mapper.UserSocialFollowMapper;
import com.knd.front.social.mapper.UserSocialRelationMapper;
import com.knd.front.social.service.UserFriendService;
import com.knd.front.train.mapper.AttachMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserFriendServiceImpl implements UserFriendService {
    private final UserSocialRelationMapper userSocialRelationMapper;
    private final UserSocialFollowMapper userSocialFollowMapper;
    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;
    private final AttachMapper attachMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    @Override
    public Result getFriendsList(String current,String userId) {
        List<UserFriendsDto> dtoList = new ArrayList<>();
        QueryWrapper<UserSocialRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("userId",userId).or().eq("friendId",userId);
        //分页
        Page<UserSocialRelationEntity> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        Page<UserSocialRelationEntity> userSocialRelationEntityPage = userSocialRelationMapper.selectPage(partPage, wrapper);
        List<UserSocialRelationEntity> userSocialRelationList = userSocialRelationEntityPage.getRecords();
        for(UserSocialRelationEntity entity : userSocialRelationList ){
            UserFriendsDto dto = new UserFriendsDto();
            dto.setFriendId(entity.getFriendId());
            User user = userMapper.selectById(entity.getFriendId());
            dto.setFriendName(user.getNickName());
            if(userId.equals(entity.getUserId())){
                dto.setHeadPicUrl(getHeadPicUrl(entity.getFriendId()));
            }else {
                dto.setHeadPicUrl(getHeadPicUrl(entity.getUserId()));
            }

            dtoList.add(dto);
        }
        Page<UserFriendsDto> dto = new Page<>();
        dto.setTotal(partPage.getTotal());
        dto.setCurrent(partPage.getCurrent());
        dto.setSize(partPage.getSize());
        dto.setRecords(dtoList);
        return ResultUtil.success(dto);
    }

    @Override
    public Result getFanList(String current,String userId) {
        List<UserFansDto> dtoList = new ArrayList<>();
        QueryWrapper<UserSocialFollowEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("targetUserId",userId);
        //分页
        Page<UserSocialFollowEntity> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        Page<UserSocialFollowEntity> userSocialFollowEntityPage = userSocialFollowMapper.selectPage(partPage, wrapper);
        List<UserSocialFollowEntity> socialFollowList = userSocialFollowEntityPage.getRecords();
        for(UserSocialFollowEntity entity : socialFollowList){
            UserFansDto dto = new UserFansDto();
            dto.setFansId(entity.getUserId());
            wrapper.clear();
            //判断是否互关
            wrapper.eq("deleted","0");
            wrapper.eq("targetUserId",entity.getUserId());
            wrapper.eq("userId",userId);
            int num = userSocialFollowMapper.selectCount(wrapper);
            if (num == 0){
                dto.setFansRelation("0");
            }else{
                dto.setFansRelation("1");
            }
            User user = userMapper.selectById(entity.getUserId());
            dto.setFansName(user.getNickName());
            dto.setHeadPicUrl(getHeadPicUrl(entity.getTargetUserId()));
            dtoList.add(dto);
        }
        Page<UserFansDto> dto = new Page<>();
        dto.setTotal(partPage.getTotal());
        dto.setCurrent(partPage.getCurrent());
        dto.setSize(partPage.getSize());
        dto.setRecords(dtoList);
        return ResultUtil.success(dto);
    }

    @Override
    public Result getFollowList(String current,String userId) {
        List<UserFollowDto> dtoList = new ArrayList<>();
        QueryWrapper<UserSocialFollowEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("userId",userId);
        //分页
        Page<UserSocialFollowEntity> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        Page<UserSocialFollowEntity> userSocialFollowEntityPage = userSocialFollowMapper.selectPage(partPage, wrapper);
        List<UserSocialFollowEntity> socialFollowList = userSocialFollowEntityPage.getRecords();
        for(UserSocialFollowEntity entity : socialFollowList){
            UserFollowDto dto = new UserFollowDto();
            dto.setFollowId(entity.getTargetUserId());
            //判断是否互关
            wrapper.eq("deleted","0");
            wrapper.eq("targetUserId",userId);
            wrapper.eq("userId",entity.getTargetUserId());
            int num = userSocialFollowMapper.selectCount(wrapper);
            if (num == 0){
                dto.setFollowRelation("0");
            }else{
                dto.setFollowRelation("1");
            }
            User user = userMapper.selectById(entity.getTargetUserId());
            dto.setFollowName(user.getNickName());
            dto.setHeadPicUrl(getHeadPicUrl(entity.getTargetUserId()));
            dtoList.add(dto);
        }
        Page<UserFollowDto> dto = new Page<>();
        dto.setTotal(partPage.getTotal());
        dto.setCurrent(partPage.getCurrent());
        dto.setSize(partPage.getSize());
        dto.setRecords(dtoList);
        return ResultUtil.success(dto);
    }

    private String getHeadPicUrl(String userId){
        UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().eq("userId", userId).eq("deleted", "0"));
        if(userDetail!=null){
            Attach attach = attachMapper.selectById(userDetail.getHeadPicUrlId());
            return attach!=null ? fileImagesPath+attach.getFilePath() : "";
        }else{
            return "";
        }
    }
}
