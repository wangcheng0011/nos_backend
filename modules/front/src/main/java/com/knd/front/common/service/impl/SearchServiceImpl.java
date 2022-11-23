package com.knd.front.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.FollowStatusEnum;
import com.knd.common.em.LabelTypeEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.common.dto.ActionListDto;
import com.knd.front.common.dto.CourseListDto;
import com.knd.front.common.dto.UserListDto;
import com.knd.front.common.service.SearchService;
import com.knd.front.entity.*;
import com.knd.front.home.dto.BaseBodyPartDto;
import com.knd.front.home.mapper.CourseHeadMapper;
import com.knd.front.live.entity.BaseDifficulty;
import com.knd.front.live.mapper.BaseDifficultyMapper;
import com.knd.front.social.dto.LabelDto;
import com.knd.front.social.entity.UserSocialFollowEntity;
import com.knd.front.social.entity.UserSocialMomentEntity;
import com.knd.front.social.entity.UserSocialRelationEntity;
import com.knd.front.social.mapper.UserLabelMapper;
import com.knd.front.social.mapper.UserSocialFollowMapper;
import com.knd.front.social.mapper.UserSocialMomentMapper;
import com.knd.front.social.mapper.UserSocialRelationMapper;
import com.knd.front.train.mapper.AttachMapper;
import com.knd.front.train.mapper.BaseBodyPartMapper;
import com.knd.front.user.mapper.BaseActionMapper;
import com.knd.front.user.mapper.CourseBodyPartMapper;
import com.knd.front.user.mapper.UserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zm
 */
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final BaseDifficultyMapper baseDifficultyMapper;
    private final CourseHeadMapper courseHeadMapper;
    private final CourseBodyPartMapper courseBodyPartMapper;
    private final BaseBodyPartMapper baseBodyPartMapper;
    private final BaseActionMapper baseActionMapper;
    private final UserInfoMapper userInfoMapper;
    private final UserSocialFollowMapper userSocialFollowMapper;
    private final UserSocialMomentMapper userSocialMomentMapper;
    private final UserSocialRelationMapper userSocialRelationMapper;
    private final UserLabelMapper userLabelMapper;
    private final AttachServiceImpl attachServiceImpl;
    private final AttachMapper attachMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    @Value("${upload.FileVideoPath}")
    private String fileVideoPath;

    @Override
    public Result getCourseList(String current, String userId, String name) {
        List<CourseListDto> dtoList = new ArrayList<>();
        Page<CourseHead> page = new Page<>(Integer.valueOf(current), PageInfo.pageSize);
        QueryWrapper<CourseHead> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("releaseFlag","1");
        if (StringUtils.isNotEmpty(name)){
            wrapper.like("course",name);
        }
        Page<CourseHead> courseHeadPage = courseHeadMapper.selectPage(page, wrapper);
        List<CourseHead> courseHeadList = courseHeadPage.getRecords();
        for (CourseHead course : courseHeadList){
            CourseListDto dto = new CourseListDto();
            BeanUtils.copyProperties(course,dto);
            dto.setCourseId(course.getId());
            Attach attach = attachMapper.selectById(course.getPicAttachId());
            dto.setPicAttachUrl(attach!=null ? fileImagesPath+attach.getFilePath() : "");
            BaseDifficulty baseDifficulty = baseDifficultyMapper.selectById(course.getDifficultyId());
            dto.setDifficulty(StringUtils.isNotEmpty(baseDifficulty) ? baseDifficulty.getDifficulty() : "");
            List<BaseBodyPartDto> partList = new ArrayList<>();
            List<CourseBodyPart> courseBodyParts = courseBodyPartMapper.selectList(new QueryWrapper<CourseBodyPart>().eq("deleted", "0").eq("courseId", course.getId()));
            for (CourseBodyPart part : courseBodyParts){
                BaseBodyPart baseBodyPart = baseBodyPartMapper.selectById(part.getPartId());
                if (StringUtils.isNotEmpty(baseBodyPart)){
                    BaseBodyPartDto partDto = new BaseBodyPartDto();
                    partDto.setId(baseBodyPart.getId());
                    partDto.setName(baseBodyPart.getPart());
                    partList.add(partDto);
                }
            }
            dto.setPartList(partList);
            dtoList.add(dto);
        }
        Page<CourseListDto> dtoPage = new Page<>();
        dtoPage.setTotal(page.getTotal());
        dtoPage.setCurrent(page.getCurrent());
        dtoPage.setSize(page.getSize());
        dtoPage.setRecords(dtoList);
        return ResultUtil.success(dtoPage);
    }

    @Override
    public Result getActionList(String current, String userId, String name) {
        List<ActionListDto> dtoList = new ArrayList<>();

        List<String> vipStatusList = new ArrayList<>();
        vipStatusList.add("1");
        vipStatusList.add("2");
        vipStatusList.add("3");
        QueryWrapper<User> wrapperUser = new QueryWrapper<>();
        wrapperUser.eq("deleted",0);
        wrapperUser.in("vipStatus",vipStatusList);
        wrapperUser.le("vipBeginDate", LocalDate.now());
        wrapperUser.ge("vipEndDate",LocalDate.now());
        wrapperUser.eq("id",userId);
        Integer isVip = userInfoMapper.selectCount(wrapperUser);

        Page<BaseAction> page = new Page<>(Integer.valueOf(current), PageInfo.pageSize);
        QueryWrapper<BaseAction> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        if (isVip==0){
            //不是会员
            wrapper.eq("vipType","0");
        }
        if (StringUtils.isNotEmpty(name)){
            wrapper.like("action",name);
        }
        Page<BaseAction> baseActionPage = baseActionMapper.selectPage(page, wrapper);
        List<BaseAction> baseActionList = baseActionPage.getRecords();
        for (BaseAction action : baseActionList){
            ActionListDto dto = new ActionListDto();
            BeanUtils.copyProperties(action,dto);
            dto.setActionId(action.getId());
            Attach attach = attachMapper.selectById(action.getPicAttachId());
            dto.setPicAttachUrl(attach!=null ? fileImagesPath+attach.getFilePath() : "");
            if(null!=action){
                Attach attach1 = attachMapper.selectById(action.getVideoAttachId());
                dto.setVideoAttachUrl(fileVideoPath + attach1.getFilePath());
                dto.setVideoSize(attach1.getFileSize());

            }
            dtoList.add(dto);
        }
        Page<ActionListDto> dtoPage = new Page<>();
        dtoPage.setTotal(page.getTotal());
        dtoPage.setCurrent(page.getCurrent());
        dtoPage.setSize(page.getSize());
        dtoPage.setRecords(dtoList);
        return ResultUtil.success(dtoPage);
    }

    @Override
    public Result getUserList(String userId, String userName, String current) {
        Page<User> page = new Page<>(Integer.valueOf(current), PageInfo.pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted",0);
        if (StringUtils.isNotEmpty(userName)){
            wrapper.like("nickName",userName);
        }
        Page<User> userPage = userInfoMapper.selectPage(page, wrapper);
        List<User> records = userPage.getRecords();
        List<UserListDto> dtoList = new ArrayList<>();
        for (User u : records){
            UserListDto dto = new UserListDto();
            dto.setUserId(u.getId());
            dto.setUserName(u.getNickName());
            dto.setHeadPicUrl(attachServiceImpl.getHeadPicUrl(u.getId()));
            QueryWrapper<UserSocialFollowEntity> followWrapper = new QueryWrapper<>();
            followWrapper.eq("deleted","0");
            followWrapper.eq("targetUserId",u.getId());
            //粉丝数量
            int fansNum = userSocialFollowMapper.selectCount(followWrapper);
            dto.setFansNum(fansNum+"");
            Integer momentNum = userSocialMomentMapper.selectCount(new QueryWrapper<UserSocialMomentEntity>().eq("deleted", "0").eq("userId", u.getId()));
            dto.setMomentNum(momentNum+"");

            //判断是否是教练
            //默认不是教练
            dto.setIsCoach("0");
            List<String> labelTypeList = new ArrayList<>();
            labelTypeList.add(LabelTypeEnum.DANCE.ordinal()+"");
            labelTypeList.add(LabelTypeEnum.BODYBUILDING.ordinal()+"");
            labelTypeList.add(LabelTypeEnum.POWER.ordinal()+"");
            labelTypeList.add(LabelTypeEnum.PILATES.ordinal()+"");
            labelTypeList.add(LabelTypeEnum.STRENGTH.ordinal()+"");
            labelTypeList.add(LabelTypeEnum.YOGA.ordinal()+"");

            List<String> labelList = new ArrayList<>();
            //标签信息
            List<LabelDto> labelDtos = userLabelMapper.getLabelList(u.getId());
            for (LabelDto label : labelDtos){
                labelList.add(label.getLabel());
                //只有有一个教练类型得标签就改成教练
                if(labelTypeList.contains(label.getType())){
                    dto.setIsCoach("1");
                }
            }

            QueryWrapper<UserSocialRelationEntity> relationWrapper = new QueryWrapper<>();
            relationWrapper.eq("deleted","0");
            relationWrapper.eq("userId",userId);
            relationWrapper.eq("friendId",u.getId());
            //是否是好友
            int isFriend = userSocialRelationMapper.selectCount(relationWrapper);
            if(isFriend == 0){
                followWrapper.clear();
                followWrapper.eq("deleted","0");
                followWrapper.eq("userId",userId);
                followWrapper.eq("targetUserId",u.getId());
                //是否关注对方
                int isFollow = userSocialFollowMapper.selectCount(followWrapper);
                if(isFollow == 0){
                    followWrapper.clear();
                    followWrapper.eq("deleted","0");
                    followWrapper.eq("userId",u.getId());
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
            dtoList.add(dto);
        }
        Page<UserListDto> dtoPage = new Page<>();
        dtoPage.setTotal(page.getTotal());
        dtoPage.setCurrent(page.getCurrent());
        dtoPage.setSize(page.getSize());
        dtoPage.setRecords(dtoList);
        return ResultUtil.success(dtoPage);
    }



}
