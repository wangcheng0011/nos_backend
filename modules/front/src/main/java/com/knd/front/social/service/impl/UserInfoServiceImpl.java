package com.knd.front.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.FollowStatusEnum;
import com.knd.common.em.LabelTypeEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.Attach;
import com.knd.front.entity.User;
import com.knd.front.entity.UserDetail;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.pay.dto.ImgDto;
import com.knd.front.social.dto.*;
import com.knd.front.social.entity.*;
import com.knd.front.social.mapper.*;
import com.knd.front.social.service.UserInfoService;
import com.knd.front.train.mapper.AttachMapper;
import com.knd.front.user.dto.UserTrainDataDto;
import com.knd.front.user.mapper.UserInfoMapper;
import com.knd.front.user.request.UserTrainDataQueryRequest;
import com.knd.front.user.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户个人信息
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final IUserInfoService iUserInfoService;
    private final UserMapper userMapper;
    private final UserSocialFollowMapper userSocialFollowMapper;
    private final UserSocialRelationMapper userSocialRelationMapper;
    private final UserSocialMomentMapper userSocialMomentMapper;
    private final UserInfoMapper userInfoMapper;
    private final UserLabelMapper userLabelMapper;
    private final UserSocialAttachMapper userSocialAttachMapper;
    private final AttachMapper attachMapper;
    private final UserSocialMomentCommentMapper userSocialMomentCommentMapper;
    private final UserSocialMomentAttachMapper userSocialMomentAttachMapper;
    private final UserSocialMomentPraiseMapper userSocialMomentPraiseMapper;
    private final UserDetailMapper userDetailMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    @Override
    public Result getOtherInfo(String userId, String friendId) {
        UserInfoDto dto = new UserInfoDto();

        User user = userMapper.selectById(friendId);
        dto.setName(user!=null ? user.getNickName() : "");
        dto.setHeadPicUrl(getHeadPicUrl(friendId));

        QueryWrapper<UserSocialFollowEntity> followWrapper = new QueryWrapper<>();
        followWrapper.eq("deleted","0");
        followWrapper.eq("userId",friendId);
        //关注数
        int followNum = userSocialFollowMapper.selectCount(followWrapper);

        followWrapper.clear();
        followWrapper.eq("deleted","0");
        followWrapper.eq("targetUserId",friendId);
        //粉丝数量
        int fansNum = userSocialFollowMapper.selectCount(followWrapper);

        QueryWrapper<UserSocialRelationEntity> relationWrapper = new QueryWrapper<>();
        relationWrapper.eq("deleted","0");
        relationWrapper.eq("userId",userId);
        relationWrapper.eq("friendId",friendId);
        //是否是好友
        int isFriend = userSocialRelationMapper.selectCount(relationWrapper);
        if(isFriend == 0){
            followWrapper.clear();
            followWrapper.eq("deleted","0");
            followWrapper.eq("userId",userId);
            followWrapper.eq("targetUserId",friendId);
            //是否关注对方
            int isFollow = userSocialFollowMapper.selectCount(followWrapper);
            if(isFollow == 0){
                followWrapper.clear();
                followWrapper.eq("deleted","0");
                followWrapper.eq("userId",friendId);
                followWrapper.eq("targetUserId",userId);
                //是否被对方关注
                int isBeFollow = userSocialFollowMapper.selectCount(followWrapper);
                if(isBeFollow == 0){
                    dto.setFollowStatus(FollowStatusEnum.NOT_FOLLOW);
                }else{
                    dto.setFollowStatus(FollowStatusEnum.BE_FOLLOW);
                }
            }else{
                dto.setFollowStatus(FollowStatusEnum.IN_FOLLOW);
            }
        }else{
            dto.setFollowStatus(FollowStatusEnum.FRIEND);
        }

        //点赞数量
        long thumbupNum = userSocialMomentMapper.getThumbupNum(friendId);




        List<String> labelTypeList = new ArrayList<>();
        labelTypeList.add(LabelTypeEnum.DANCE.ordinal()+"");
        labelTypeList.add(LabelTypeEnum.BODYBUILDING.ordinal()+"");
        labelTypeList.add(LabelTypeEnum.POWER.ordinal()+"");
        labelTypeList.add(LabelTypeEnum.PILATES.ordinal()+"");
        labelTypeList.add(LabelTypeEnum.YOGA.ordinal()+"");
        labelTypeList.add(LabelTypeEnum.STRENGTH.ordinal()+"");

        List<String> labelList = new ArrayList<>();
        //默认不是教练
        dto.setIsCoach("0");
        //标签信息
        List<LabelDto> labelDtos = userLabelMapper.getLabelList(friendId);
        for (LabelDto label : labelDtos){
            labelList.add(label.getLabel());
            //只有有一个教练类型得标签就改成教练
            if(labelTypeList.contains(label.getType())){
                dto.setIsCoach("1");
            }
        }

        List<String> vipStatusList = new ArrayList<>();
        vipStatusList.add("1");
        vipStatusList.add("2");
        vipStatusList.add("3");
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted",0);
        wrapper.in("vipStatus",vipStatusList);
        wrapper.le("vipBeginDate",LocalDate.now());
        wrapper.ge("vipEndDate",LocalDate.now());
        wrapper.eq("id",userId);
        User userVip = userInfoMapper.selectOne(wrapper);
        String isVip = "0";
        if(StringUtils.isNotEmpty(userVip)){
            isVip = "1";
        }
        
        dto.setIsVip(isVip);
        dto.setFansNum(fansNum);
        dto.setFollowNum(followNum);
        dto.setThumbupNum(thumbupNum);
        dto.setLabelList(labelList);
        return ResultUtil.success(dto);
    }

    @Override
    public Result getRecord(String userId) {
        UserTrainDataQueryRequest userTrainDataQueryRequest = new UserTrainDataQueryRequest();
        userTrainDataQueryRequest.setUserId(userId);
        //总运动时长，总消耗
        UserTrainDataDto userTrainAll = iUserInfoService.getUserTrainData(userTrainDataQueryRequest);
        if(userTrainAll == null) {
            userTrainAll = new UserTrainDataDto();
        }

        userTrainDataQueryRequest.setMonth(LocalDate.now().toString().substring(0,7));
        //当月运动时长
        UserTrainDataDto userTrainMonth = iUserInfoService.getUserTrainData(userTrainDataQueryRequest);
        if(userTrainMonth == null) {
            userTrainMonth = new UserTrainDataDto();
        }

        //加入时间
        User user = userMapper.selectById(userId);
        String registTime = user.getRegistTime();

        List<RecordMessageDto> recordMessageDtoList = new ArrayList<>();
        RecordMessageDto dto1 = new RecordMessageDto();
        dto1.setType("健身");
        dto1.setRecord1(userTrainAll.getTotalTrainSeconds());
        if("0"!=userTrainAll.getTotalCalorie()){
            dto1.setRecord2((String.format("%.2f", userTrainAll.getTotalCalorie())));
        }
        recordMessageDtoList.add(dto1);

        UserRecordDto dto = new UserRecordDto();
        dto.setTotalTrainSeconds(userTrainAll.getTotalTrainSeconds());
        if("0"!=userTrainAll.getTotalCalorie()) {
            dto.setTotalCalorie(String.format("%.2f", userTrainAll.getTotalCalorie()));
        }
        dto.setMonthTrainSeconds(userTrainMonth.getTotalTrainSeconds());
        dto.setRegistTime(registTime);
        dto.setRecordMessageDtoList(recordMessageDtoList);

        return ResultUtil.success(dto);
    }

    @Override
    public Result getAlbum(String userId) {
        UserAttachDto dto = new UserAttachDto();
        List<ImgDto> attachList = new ArrayList<>();
        QueryWrapper<UserSocialAttachEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("userId",userId);
        List<UserSocialAttachEntity> userSocialAttachList = userSocialAttachMapper.selectList(wrapper);
        for (UserSocialAttachEntity u : userSocialAttachList){
            Attach attach = attachMapper.selectById(u.getAttachId());
            ImgDto imgDto = new ImgDto();
            imgDto.setPicAttachId(attach.getId());
            imgDto.setPicAttachUrl(fileImagesPath+attach.getFilePath());
            imgDto.setPicAttachSize(attach.getFileSize());
            attachList.add(imgDto);
        }
        dto.setAttachList(attachList);
        return ResultUtil.success(dto);
    }

    @Override
    public Result getMoment(String current,String userId,String friendId) {
        List<UserMomentDto> dtoList = new ArrayList<>();
        QueryWrapper<UserSocialMomentEntity> momentWrapper = new QueryWrapper<>();
        momentWrapper.eq("deleted","0");
        momentWrapper.eq("userId",friendId);
        momentWrapper.eq("isPublic","1");
        momentWrapper.orderByDesc("createTime");
        //分页
        Page<UserSocialMomentEntity> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        Page<UserSocialMomentEntity> userSocialMomentEntityPage = userSocialMomentMapper.selectPage(partPage, momentWrapper);
        List<UserSocialMomentEntity> userSocialMomentList = userSocialMomentEntityPage.getRecords();
        for(UserSocialMomentEntity moment : userSocialMomentList){
            UserMomentDto dto = new UserMomentDto();
            BeanUtils.copyProperties(moment,dto);
            List<ImgDto> imageUrl = new ArrayList<>();
            QueryWrapper<UserSocialMomentAttachEntity> attachWrapper = new QueryWrapper<>();
            attachWrapper.eq("deleted","0");
            attachWrapper.eq("momentId",moment.getId());
            List<UserSocialMomentAttachEntity> userSocialMomentAttachList = userSocialMomentAttachMapper.selectList(attachWrapper);
            for(UserSocialMomentAttachEntity attachEntity : userSocialMomentAttachList){
                Attach attach = attachMapper.selectById(attachEntity.getAttachId());
                ImgDto imgDto = new ImgDto();
                imgDto.setPicAttachUrl(fileImagesPath+attach.getFilePath());
                imgDto.setPicAttachSize(attach.getFileSize());
                imageUrl.add(imgDto);
            }
            dto.setImageUrl(imageUrl);

//            QueryWrapper<UserSocialMomentCommentEntity> wrapper = new QueryWrapper<>();
//            wrapper.eq("deleted","0");
//            wrapper.eq("momentId",moment.getId());
//            wrapper.eq("parentId","");
//            wrapper.orderByAsc("publishtime");
//            List<UserSocialMomentCommentEntity> userSocialMomentCommentList = userSocialMomentCommentMapper.selectList(wrapper);
//            List<MomentCommentDto> commentDtoList = new ArrayList<>();
//            for (UserSocialMomentCommentEntity entity : userSocialMomentCommentList){
//                MomentCommentDto momentCommentDto = new MomentCommentDto();
//                BeanUtils.copyProperties(entity,momentCommentDto);
//                commentDtoList.add(momentCommentDto);
//            }
//            List<MomentCommentDto> commentList = getTreeList(commentDtoList);
//            dto.setMomentCommentDtoList(commentList);
            User user = userMapper.selectById(moment.getUserId());
            dto.setUserName(user!=null ? user.getNickName() : "");
            dto.setUserHeadUrl(getHeadPicUrl(moment.getUserId()));
            int praise = userSocialMomentPraiseMapper.selectCount(new QueryWrapper<UserSocialMomentPraiseEntity>()
                    .eq("deleted", "0")
                    .eq("userId", userId)
                    .eq("momentId", moment.getId()).eq("praise", "0"));
            dto.setIsPraise(praise+"");
            dtoList.add(dto);
        }
        Page<UserMomentDto> dto = new Page<>();
        dto.setTotal(partPage.getTotal());
        dto.setCurrent(partPage.getCurrent());
        dto.setSize(partPage.getSize());
        dto.setRecords(dtoList);
        return ResultUtil.success(dto);
    }

    @Override
    public Result getOneMoment(String userId,String momentId) {
        UserMomentDto dto = new UserMomentDto();
        UserSocialMomentEntity moment = userSocialMomentMapper.selectById(momentId);
        BeanUtils.copyProperties(moment,dto);

        User momentUser = userMapper.selectById(moment.getUserId());
        if(momentUser!=null){
            dto.setUserName(momentUser.getNickName());
            dto.setUserHeadUrl(getHeadPicUrl(moment.getUserId()));
        }

        int praise = userSocialMomentPraiseMapper.selectCount(new QueryWrapper<UserSocialMomentPraiseEntity>()
                .eq("deleted", "0")
                .eq("userId", userId)
                .eq("momentId", moment.getId()).eq("praise", "0"));
        dto.setIsPraise(praise+"");

        List<ImgDto> imageUrl = new ArrayList<>();
        QueryWrapper<UserSocialMomentAttachEntity> attachWrapper = new QueryWrapper<>();
        attachWrapper.eq("deleted","0");
        attachWrapper.eq("momentId",moment.getId());
        List<UserSocialMomentAttachEntity> userSocialMomentAttachList = userSocialMomentAttachMapper.selectList(attachWrapper);
        for(UserSocialMomentAttachEntity attachEntity : userSocialMomentAttachList){
            Attach attach = attachMapper.selectById(attachEntity.getAttachId());
            ImgDto imgDto = new ImgDto();
            imgDto.setPicAttachUrl(fileImagesPath+attach.getFilePath());
            imgDto.setPicAttachSize(attach.getFileSize());
            imageUrl.add(imgDto);
        }
        dto.setImageUrl(imageUrl);

        QueryWrapper<UserSocialMomentCommentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("momentId",moment.getId());
        wrapper.eq("parentId","");
        wrapper.orderByAsc("publishtime");
        List<UserSocialMomentCommentEntity> userSocialMomentCommentList = userSocialMomentCommentMapper.selectList(wrapper);
        List<MomentCommentDto> commentDtoList = new ArrayList<>();
        for (UserSocialMomentCommentEntity entity : userSocialMomentCommentList){
            MomentCommentDto momentCommentDto = new MomentCommentDto();
            BeanUtils.copyProperties(entity,momentCommentDto);

            User user = userMapper.selectById(entity.getUserId());
            if(user!=null){
                momentCommentDto.setUserName(user.getNickName());
                momentCommentDto.setUserHeadUrl(getHeadPicUrl(entity.getUserId()));
            }

            if(StringUtils.isNotEmpty(momentCommentDto.getCallUserId())){
                User callUser = userMapper.selectById(momentCommentDto.getCallUserId());
                momentCommentDto.setCallUserName(callUser!=null ? callUser.getNickName() : "");
            }
            commentDtoList.add(momentCommentDto);
        }
        List<MomentCommentDto> commentList = getTreeList(commentDtoList);
        dto.setMomentCommentDtoList(commentList);
        return ResultUtil.success(dto);
    }

    @Override
    public Result getCourse(String userId) {
        return null;
    }

    public List<MomentCommentDto> getTreeList(List<MomentCommentDto> list){
        for (int i = 0; i < list.size(); i++){
            QueryWrapper<UserSocialMomentCommentEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted","0");
            wrapper.eq("parentId",list.get(i).getId());
            wrapper.orderByAsc("publishtime");
            List<UserSocialMomentCommentEntity> userSocialMomentCommentList = userSocialMomentCommentMapper.selectList(wrapper);
            if(userSocialMomentCommentList != null && userSocialMomentCommentList.size()>0){
                List<MomentCommentDto> commentDtoList = new ArrayList<>();
                for (UserSocialMomentCommentEntity entity : userSocialMomentCommentList){
                    MomentCommentDto dto = new MomentCommentDto();
                    BeanUtils.copyProperties(entity,dto);
                    User user = userMapper.selectById(entity.getUserId());
                    if(user!=null){
                        dto.setUserName(user.getNickName());
                        dto.setUserHeadUrl(getHeadPicUrl(entity.getUserId()));
                    }
                    if(StringUtils.isNotEmpty(dto.getCallUserId())){
                        User callUser = userMapper.selectById(dto.getCallUserId());
                        dto.setCallUserName(callUser!=null ? callUser.getNickName() : "");
                    }

                    commentDtoList.add(dto);
                }
                list.get(i).setSunList(commentDtoList);
                getTreeList(list.get(i).getSunList());
            }
        }
        return list;
    }

    private String getHeadPicUrl(String userId){
        UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().eq("userId", userId).eq("deleted", "0"));
        if(userDetail!=null && userDetail.getHeadPicUrlId()!=null){
            Attach attach = attachMapper.selectById(userDetail.getHeadPicUrlId());
            return attach!=null ? fileImagesPath+attach.getFilePath() : "";
        }else{
            return "";
        }
    }

}
