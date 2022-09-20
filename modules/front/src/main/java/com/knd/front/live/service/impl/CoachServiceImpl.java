package com.knd.front.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.*;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.service.AttachService;
import com.knd.front.dto.OrderUser;
import com.knd.front.entity.Attach;
import com.knd.front.entity.User;
import com.knd.front.entity.UserDetail;
import com.knd.front.live.dto.*;
import com.knd.front.live.entity.*;
import com.knd.front.live.mapper.*;
import com.knd.front.live.request.CoachRequest;
import com.knd.front.live.request.SaveCoachCourseRequest;
import com.knd.front.live.service.CoachService;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.login.service.IUserDetailService;
import com.knd.front.pay.dto.ImgDto;
import com.knd.front.social.dto.LabelDto;
import com.knd.front.social.entity.BaseLabelEntity;
import com.knd.front.social.entity.UserLabelEntity;
import com.knd.front.social.entity.UserSocialFollowEntity;
import com.knd.front.social.entity.UserSocialRelationEntity;
import com.knd.front.social.mapper.*;
import com.knd.front.train.mapper.AttachMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class CoachServiceImpl implements CoachService {
    private final UserSocialFollowMapper userSocialFollowMapper;
    private final UserSocialRelationMapper userSocialRelationMapper;
    private final UserCoachCourseOrderMapper userCoachCourseOrderMapper;
    private final UserCoachCourseMapper userCoachCourseMapper;
    private final UserCoachTimeMapper userCoachTimeMapper;
    private final UserCoachMapper userCoachMapper;
    private final BaseLabelMapper baseLabelMapper;
    private final UserLabelMapper userLabelMapper;
    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;
    private final UserSocialMomentMapper userSocialMomentMapper;
    private final AttachMapper attachMapper;
    private final BaseDifficultyMapper difficultyMapper;
    private final AttachService attachService;
    private final IUserDetailService iUserDetailService;
    private final UserCoachAttachMapper userCoachAttachMapper;
    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    @Override
    public Result getCoachList(CoachRequest request) {
        log.info("---------------------------获取教练列表开始------------------------------------");
        log.info("getCoachList request:{{}}",request);
        List<String> typeList = request.getTypeList();
        List<CoachListDto> dtoList = new ArrayList<>();
        QueryWrapper<CoachListEntityDto> wrapper = new QueryWrapper<>();
        wrapper.eq("a.deleted", "0");
        if (StringUtils.isNotEmpty(request.getSex())) {
            wrapper.eq("b.gender", request.getSex());
        }

        if (StringUtils.isNotEmpty(typeList)) {
            List<String> labelIdList = new ArrayList<>();
            List<BaseLabelEntity> baseLabelEntities = baseLabelMapper.selectList(new QueryWrapper<BaseLabelEntity>().in("type", typeList).eq("deleted", "0"));
            for (BaseLabelEntity entity : baseLabelEntities) {
                labelIdList.add(entity.getId());
            }
            if (StringUtils.isEmpty(labelIdList)) {
                return ResultUtil.success();
            }
            List<String> userIdList = new ArrayList<>();
            List<UserLabelEntity> userLabelEntities = userLabelMapper.selectList(new QueryWrapper<UserLabelEntity>().eq("deleted", "0").in("labelId", labelIdList));
            for (UserLabelEntity entity : userLabelEntities) {
                userIdList.add(entity.getUserId());
            }
           if (StringUtils.isEmpty(userIdList)) {
               userIdList.add("");
            }
            wrapper.in("a.userId", userIdList);
        }
        wrapper.ne("a.userId",UserUtils.getUserId());
        if ("0".equals(request.getSort())) {
            //服务学员人数高到低
            wrapper.orderByDesc("a.traineeNum");
        } else if ("1".equals(request.getSort())) {
            //服务学员人数低到高
            wrapper.orderByAsc("a.traineeNum");
            //价格低到高
            //wrapper.orderByAsc("coursePrice");
       } else {
            //默认根据课程数量排序
            wrapper.orderByDesc("num");
        }
        wrapper.groupBy("a.userId");
        Page<CoachListEntityDto> page = new Page<>(Integer.parseInt(request.getCurrent()), PageInfo.pageSize);
        List<CoachListEntityDto> coachListEntityDtoList = userCoachMapper.getCoachListEntityDto(page, wrapper);
        log.info("getCoachList coachListEntityDtoList:{{}}",coachListEntityDtoList);
        for (CoachListEntityDto entity : coachListEntityDtoList) {
            CoachListDto dto = new CoachListDto();
            BeanUtils.copyProperties(entity, dto);
            //用户标签
            List<String> labelList = new ArrayList<>();
            log.info("getCoachList coachUserId:{{}}",entity.getCoachUserId());
            List<UserLabelEntity> userLabelEntities = userLabelMapper.selectList(new QueryWrapper<UserLabelEntity>().eq("deleted", "0").eq("userId", entity.getCoachUserId()));
            log.info("getCoachList userLabelEntities:{{}}",userLabelEntities);
            for (UserLabelEntity labelEntity : userLabelEntities) {
                BaseLabelEntity baseLabelEntity = baseLabelMapper.selectById(labelEntity.getLabelId());
                List<String> labelTypeList = new ArrayList<>();
                labelTypeList.add(LabelTypeEnum.DANCE.ordinal() + "");
                labelTypeList.add(LabelTypeEnum.BODYBUILDING.ordinal() + "");
                labelTypeList.add(LabelTypeEnum.POWER.ordinal() + "");
                labelTypeList.add(LabelTypeEnum.PILATES.ordinal() + "");
                labelTypeList.add(LabelTypeEnum.YOGA.ordinal() + "");
                labelTypeList.add(LabelTypeEnum.STRENGTH.ordinal() + "");
                if (baseLabelEntity != null && labelTypeList.contains(baseLabelEntity.getType())) {
                    labelList.add(baseLabelEntity.getLabel());
                }
            }
            log.info("getCoachList labelTypeList:{{}}",labelList);
            dto.setLabelList(labelList);
            dto.setCoachHeadUrl(getHeadPicUrl(entity.getCoachUserId()));
            QueryWrapper<UserCoachAttachEntity> userCoachAttachQueryWrapper = new QueryWrapper<>();
            userCoachAttachQueryWrapper.eq("coachUserId", entity.getCoachUserId());
            userCoachAttachQueryWrapper.eq("deleted", "0");
            List<UserCoachAttachEntity> userCoachAttaches = userCoachAttachMapper.selectList(userCoachAttachQueryWrapper);
            log.info("getCoachList userCoachAttaches:{{}}",userCoachAttaches);
            ArrayList<ImgDto> imgDtos = new ArrayList<>();
            userCoachAttaches.stream().forEach(userCoachAttach -> {
                imgDtos.add(getImgDto(userCoachAttach.getAttachUrlId()));
            });
            dto.setImageUrl(imgDtos);
            dtoList.add(dto);
        }
        log.info("getCoachList dtoList:{{}}",dtoList);
        Page<CoachListDto> dto = new Page<>();
        dto.setTotal(page.getTotal());
        dto.setCurrent(page.getCurrent());
        dto.setSize(page.getSize());
        dto.setRecords(dtoList);
        log.info("--------------------------------------获取教练列表结束啦----------------------------------------------------");
        return ResultUtil.success(dto);
    }

    @Override
    public Result getCoachDetails(String userId,String coachUserId) {
       log.info("getCoachDetails userId:{{}}",userId);
       log.info("getCoachDetails coachUserId:{{}}",coachUserId);
        CoachDetailsDto dto = new CoachDetailsDto();
        //教练信息
        UserCoachEntity entity = userCoachMapper.selectOne(new QueryWrapper<UserCoachEntity>().eq("deleted", "0").eq("userId", coachUserId));
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        } else {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "教练不存在");
        }

        BigDecimal minCoursePrice = userCoachMapper.getMinPrice(LocalDateTime.now(), CoachCourseTypeEnum.COURSE.ordinal() + "", coachUserId);
        dto.setCoursePrice(minCoursePrice);
        BigDecimal minConsultPrice = userCoachMapper.getMinPrice(LocalDateTime.now(), CoachCourseTypeEnum.COURSE_CONSULT.ordinal() + "", coachUserId);
        dto.setConsultPrice(minConsultPrice);
        BigDecimal minLivePrice = userCoachMapper.getMinPrice(LocalDateTime.now(), CoachCourseTypeEnum.LIVE.ordinal() + "", coachUserId);
        dto.setLivePrice(minLivePrice);


        QueryWrapper<UserSocialFollowEntity> followWrapper = new QueryWrapper<>();
        followWrapper.eq("deleted", "0");
        followWrapper.eq("userId", coachUserId);
        //关注数
        int followNum = userSocialFollowMapper.selectCount(followWrapper);

        followWrapper.clear();
        followWrapper.eq("deleted", "0");
        followWrapper.eq("targetUserId", coachUserId);
        //粉丝数量
        int fansNum = userSocialFollowMapper.selectCount(followWrapper);

        QueryWrapper<UserSocialRelationEntity> relationWrapper = new QueryWrapper<>();
        relationWrapper.eq("deleted", "0");
        relationWrapper.eq("userId", userId);
        relationWrapper.eq("friendId", coachUserId);
        //是否是好友
        int isFriend = userSocialRelationMapper.selectCount(relationWrapper);
        if (isFriend == 0) {
            followWrapper.clear();
            followWrapper.eq("deleted", "0");
            followWrapper.eq("userId", userId);
            followWrapper.eq("targetUserId", coachUserId);
            //是否关注对方
            int isFollow = userSocialFollowMapper.selectCount(followWrapper);
            if (isFollow == 0) {
                followWrapper.clear();
                followWrapper.eq("deleted", "0");
                followWrapper.eq("userId", coachUserId);
                followWrapper.eq("targetUserId", userId);
                //是否被对方关注
                int isBeFollow = userSocialFollowMapper.selectCount(followWrapper);
                if (isBeFollow == 0) {
                    dto.setFollowStatus(FollowStatusEnum.NOT_FOLLOW);
                } else {
                    dto.setFollowStatus(FollowStatusEnum.BE_FOLLOW);
                }
            } else {
                dto.setFollowStatus(FollowStatusEnum.IN_FOLLOW);
            }
        } else {
            dto.setFollowStatus(FollowStatusEnum.FRIEND);
        }

        //点赞数量
        long thumbupNum = userSocialMomentMapper.getThumbupNum(coachUserId);
        //标签信息
        List<String> labelList = new ArrayList<>();
        //标签信息
        List<LabelDto> labelDtos = userLabelMapper.getLabelList(userId);
        for (LabelDto label : labelDtos) {
            labelList.add(label.getLabel());
        }
      /*  //添加学员们
        QueryWrapper<UserCoachCourseOrderEntity> userCoachCourseOrderEntityQueryWrapper = new QueryWrapper<>();
        userCoachCourseOrderEntityQueryWrapper.eq("coachUserId",userId);
        userCoachCourseOrderEntityQueryWrapper.eq("deleted","0");
        List<UserCoachCourseOrderEntity> userCoachCourseOrderEntities = userCoachCourseOrderMapper.selectList(userCoachCourseOrderEntityQueryWrapper);
        ArrayList<User> users = new ArrayList<>();
        userCoachCourseOrderEntities.stream().forEach(i->{
            User user = userMapper.selectById(i.getOrderUserId());
            users.add(user);
        });
        dto.setOrderUsers(users);*/
        //返回教练昵称头像
        User coach= userMapper.selectById(userId);
        if (StringUtils.isNotEmpty(coach)) {
            dto.setCoachName(coach.getNickName());
            dto.setCoachHeadUrl(getHeadPicUrl(coach.getId()));
        }
        QueryWrapper<UserCoachAttachEntity> userCoachAttachQueryWrapper = new QueryWrapper<>();
        userCoachAttachQueryWrapper.eq("coachUserId", coachUserId);
        userCoachAttachQueryWrapper.eq("deleted", "0");
        List<UserCoachAttachEntity> userCoachAttaches = userCoachAttachMapper.selectList(userCoachAttachQueryWrapper);
        log.info("getCoachDetails userCoachAttaches:{{}}",userCoachAttaches);
        ArrayList<ImgDto> imgDtos = new ArrayList<>();
        userCoachAttaches.stream().forEach(userCoachAttach -> {
            imgDtos.add(getImgDto(userCoachAttach.getAttachUrlId()));
        });
        dto.setImageUrl(imgDtos);
        dto.setFansNum(fansNum);
        dto.setFollowNum(followNum);
        dto.setThumbupNum(thumbupNum);
        dto.setLabelList(labelList);
        return ResultUtil.success(dto);
    }

    @Override
    public Result getCoachByUser(String userId) {
        List<CoachListByUserDto> dtoList = new ArrayList<>();

        List<String> coachIdList = userCoachMapper.getCoachIdByUserId(userId);
        for (String coachId : coachIdList) {
            CoachListByUserDto dto = new CoachListByUserDto();
            UserCoachEntity userCoachEntity = userCoachMapper.selectById(coachId);
            if(StringUtils.isNotEmpty(userCoachEntity)&&!userId.equals(userCoachEntity.getUserId())){
                String coachUserId = userCoachEntity.getUserId();
                //用户标签
                List<String> labelList = new ArrayList<>();
                List<UserLabelEntity> userLabelEntities = userLabelMapper.selectList(new QueryWrapper<UserLabelEntity>().eq("deleted", "0").eq("userId", coachUserId));
                for (UserLabelEntity labelEntity : userLabelEntities) {
                    BaseLabelEntity baseLabelEntity = baseLabelMapper.selectById(labelEntity.getLabelId());
                    List<String> labelTypeList = new ArrayList<>();
                    labelTypeList.add(LabelTypeEnum.DANCE.ordinal() + "");
                    labelTypeList.add(LabelTypeEnum.BODYBUILDING.ordinal() + "");
                    labelTypeList.add(LabelTypeEnum.POWER.ordinal() + "");
                    labelTypeList.add(LabelTypeEnum.PILATES.ordinal() + "");
                    labelTypeList.add(LabelTypeEnum.YOGA.ordinal() + "");
                    labelTypeList.add(LabelTypeEnum.STRENGTH.ordinal() + "");
                    if (baseLabelEntity != null && labelTypeList.contains(baseLabelEntity.getType())) {
                        labelList.add(baseLabelEntity.getLabel());
                    }
                }
                dto.setLabelList(labelList);
                dto.setCoachId(coachId);
                dto.setCoachUserId(coachUserId);
                dto.setDepict(userCoachEntity.getDepict());
                User user = userMapper.selectById(coachUserId);
                if (StringUtils.isNotEmpty(user)) {
                    dto.setCoachName(user.getNickName());
                    dto.setCoachHeadUrl(getHeadPicUrl(user.getId()));
                }
                dtoList.add(dto);
            }
        }
        return ResultUtil.success(dtoList);
    }

    @Override
    public Result getCoachCourseByUserList(String userId, String coachUserId, List<String> typeList, String current) {
        if (StringUtils.isEmpty(current)) {
            current = "1";
        }
        Page<CoachCourseByUserListDto> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        QueryWrapper<CoachCourseByUserListDto> wrapper = new QueryWrapper<>();
        wrapper.eq("coach.deleted", "0");
        wrapper.eq("c.orderUserId", userId);
        wrapper.eq("c.isOrder", "1");
        wrapper.ne("b.liveStatus","2");
        if (StringUtils.isNotEmpty(typeList)) {
            wrapper.in("a.courseType", typeList);
        }
        if (StringUtils.isNotEmpty(coachUserId)) {
            wrapper.eq("coach.userId", coachUserId);
        }
        wrapper.orderByDesc("b.beginTime");
        List<CoachCourseByUserListDto> dtoList = userCoachCourseMapper.getCoachCourseByUserList(page, wrapper);
        for (CoachCourseByUserListDto dto : dtoList) {
            if (StringUtils.isNotEmpty(dto.getPicAttachUrl())) {
                Attach attach = attachMapper.selectById(dto.getPicAttachUrl());
                dto.setPicAttachUrl(attach != null ? fileImagesPath + attach.getFilePath() : "");
            }
            BaseDifficulty baseDifficulty = difficultyMapper.selectById(dto.getDifficulty());
            dto.setDifficulty(StringUtils.isNotEmpty(baseDifficulty) ? baseDifficulty.getDifficulty() : "");
            //TODO播放地址
            dto.setReplayUrl(dto.getReplayUrl());
        /*    if (StringUtils.isNotEmpty(dto.getPicAttachUrl())){
                Attach attach = attachMapper.selectById(dto.getPicAttachUrl());
                dto.setPicAttachUrl(attach!=null ? fileImagesPath+attach.getFilePath() : "");
            }*/
            UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(dto.getCourseId());
            if (StringUtils.isNotEmpty(userCoachCourseEntity)) {
                String[] splitTarget = userCoachCourseEntity.getTargets().split(",");
                List<String> targetList = Arrays.asList(splitTarget);
                dto.setTargetList(targetList);

                String[] splitPart = userCoachCourseEntity.getParts().split(",");
                List<String> partList = Arrays.asList(splitPart);
                dto.setPartList(partList);
            }
            dto.setCoachHeadUrl(getHeadPicUrl(dto.getCoachUserId()));
        }
        page.setRecords(dtoList);
        return ResultUtil.success(page);
    }

    @Override
    public Result getCoachOrderList(String userId, List<String> typeList, String coachUserId, LocalDate beginDate, LocalDate endDate) {
        QueryWrapper<CoachCourseOrderListDto> wrapper = new QueryWrapper<>();
        wrapper.eq("coach.deleted", "0");
        if (StringUtils.isNotEmpty(coachUserId)) {
            wrapper.eq("coach.userId", coachUserId);
        }
        wrapper.in("a.courseType", typeList);
        wrapper.between("b.date", beginDate, endDate);
        wrapper.orderByAsc("b.beginTime");
        List<String> idList = userCoachCourseOrderMapper.selectIdList(userId);
        log.info("getCoachOrderList idList:{{}}",idList);
        if(StringUtils.isNotEmpty(idList)&&idList.size()!=0){
            wrapper.notIn("b.id",idList);
        }
        List<CoachCourseOrderListDto> dtoList = userCoachCourseMapper.getCoachCourseOrderList(wrapper, userId);
        for (CoachCourseOrderListDto dto : dtoList) {
            dto.setCoachHeadUrl(getHeadPicUrl(dto.getCoachHeadUrl()));
            if (StringUtils.isNotEmpty(dto.getPicAttachUrl())) {
                Attach attach = attachMapper.selectById(dto.getPicAttachUrl());
                dto.setPicAttachUrl(attach != null ? fileImagesPath + attach.getFilePath() : "");
            }
            BaseDifficulty baseDifficulty = difficultyMapper.selectById(dto.getDifficulty());
            dto.setDifficulty(StringUtils.isNotEmpty(baseDifficulty) ? baseDifficulty.getDifficulty() : "");
            dto.setReplayUrl(dto.getReplayUrl());
            if("1".equals(dto.getIsOrder())||"2".equals(dto.getIsOrder())){
                dtoList.remove(dto);
            }
        }
        return ResultUtil.success(dtoList);
    }

    @Override
    public Result getDayOrderList(String userId, LocalDate beginDate, LocalDate endDate) {
        log.info("--------------------日历（教练查看课程）开始--------------------");
        log.info("getDayOrderList userId:{{}}", userId);
        log.info("getDayOrderList beginDate:{{}}", beginDate);
        log.info("getDayOrderList endDate:{{}}", endDate);
        List<DayOrderListDto> dtoList = new ArrayList<>();
        QueryWrapper<UserCoachTimeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", "0");
        wrapper.eq("coachUserId", userId);
        wrapper.ne("liveStatus","2");
        wrapper.between("date", beginDate, endDate);
        wrapper.orderByDesc("createDate");
        List<UserCoachTimeEntity> userCoachTimeEntities = userCoachTimeMapper.selectList(wrapper);
        log.info("getDayOrderList userCoachTimeEntities:{{}}", userCoachTimeEntities);
        //查询出该日该教练用户所有得课程，目前课程与time是一一对应
        //userCoachTimeEntities.stream().sorted(Comparator.comparing(UserCoachTimeEntity::getDate).reversed()).forEach();
        for (UserCoachTimeEntity timeEntity : userCoachTimeEntities) {
            DayOrderListDto dto = new DayOrderListDto();
            UserCoachCourseEntity courseEntity = userCoachCourseMapper.selectById(timeEntity.getCoachCourseId());
            log.info("getDayOrderList courseEntity:{{}}", courseEntity);
            if (StringUtils.isNotEmpty(courseEntity)) {
            //预约人数
            List<String> isOrderList = new ArrayList<>();
            isOrderList.add("0");
            isOrderList.add("1");
            log.info("getDayOrderList coachTimeId:{{}}", timeEntity.getId());
            List<UserCoachCourseOrderEntity> userCoachCourseOrderEntities = userCoachCourseOrderMapper.selectList(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("coachTimeId", timeEntity.getId())
                    .in("isOrder", isOrderList)
                    .eq("deleted", "0"));
            dto.setTimeId(timeEntity.getId());
            dto.setBeginTime(timeEntity.getBeginTime());
            dto.setEndTime(timeEntity.getEndTime());
            dto.setCoursePrice(timeEntity.getPrice());
            dto.setCourseType(courseEntity.getCourseType());
            dto.setCourseName(courseEntity.getCourseName());
            dto.setOrderNum(userCoachCourseOrderEntities.size());
            dto.setLiveStatus(timeEntity.getLiveStatus());
            dto.setCoachUserId(timeEntity.getCoachUserId());
            dto.setCourseId(courseEntity.getId());
            //预约人的信息
            ArrayList<OrderUser> orderUsers = new ArrayList<OrderUser>();
            userCoachCourseOrderEntities.stream().forEach(userCoachCourseOrderEntity -> {
                OrderUser orderUser = new OrderUser();
                User user = userMapper.selectById(userCoachCourseOrderEntity.getOrderUserId());
                orderUser.setId(userCoachCourseOrderEntity.getCoachUserId());
                orderUser.setMobile(user.getMobile());
                orderUser.setNickName(user.getNickName());
                orderUser.setVipStatus(user.getVipStatus());
                orderUser.setHeadPicUrl(iUserDetailService.getHeadUrl(user.getId()));
                orderUsers.add(orderUser);
            });
            dto.setOrderUsers(orderUsers);
            log.info("getDayOrderList DayOrderListDto:{{}}", dto);
            dtoList.add(dto);
            log.info("getDayOrderList List<DayOrderListDto>:{{}}", dtoList);
         }
        }
        log.info("--------------------日历（教练查看课程）结束--------------------");
        return ResultUtil.success(dtoList);
    }

    @Override
    public Result getCoachCourseById(String timeId, String userId) {
        CoachCourseOrderListDto dto = new CoachCourseOrderListDto();
        UserCoachTimeEntity timeEntity = userCoachTimeMapper.selectById(timeId);
        if (StringUtils.isEmpty(timeEntity)) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "课程时间不存在");
        }
        dto.setTimeId(timeEntity.getId());
        UserCoachCourseEntity courseEntity = userCoachCourseMapper.selectById(timeEntity.getCoachCourseId());
        if (StringUtils.isEmpty(courseEntity)) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "课程不存在");
        }

        if (StringUtils.isNotEmpty(courseEntity.getPicAttachId())) {
            Attach attach = attachMapper.selectById(courseEntity.getPicAttachId());
            dto.setPicAttachUrl(attach != null ? fileImagesPath + attach.getFilePath() : "");
        }
        User user = userMapper.selectById(timeEntity.getCoachUserId());
        dto.setCoachName(user != null ? user.getNickName() : "");

        List<String> orderHeadPic = new ArrayList<>();
        List<UserCoachCourseOrderEntity> userCoachCourseOrderEntities = userCoachCourseOrderMapper.selectList(new QueryWrapper<UserCoachCourseOrderEntity>().eq("deleted", "0").eq("isOrder", "1").eq("coachTimeId", timeId));
        for (UserCoachCourseOrderEntity e : userCoachCourseOrderEntities) {
            String orderUserId = e.getOrderUserId();
            String headPic = getHeadPicUrl(orderUserId);
            if (StringUtils.isNotEmpty(headPic)) {
                orderHeadPic.add(headPic);
            }
            if (orderHeadPic.size() == 5) {
                break;
            }
        }
        dto.setOrderNum(userCoachCourseOrderEntities.size());
        dto.setOrderHeadPic(orderHeadPic);

        QueryWrapper<UserSocialRelationEntity> relationWrapper = new QueryWrapper<>();
        relationWrapper.eq("deleted", "0");
        relationWrapper.eq("userId", userId);
        relationWrapper.eq("friendId", timeEntity.getCoachUserId());
        //是否是好友
        int isFriend = userSocialRelationMapper.selectCount(relationWrapper);
        if (isFriend == 0) {
            QueryWrapper<UserSocialFollowEntity> followWrapper = new QueryWrapper<>();
            followWrapper.eq("deleted", "0");
            followWrapper.eq("userId", userId);
            followWrapper.eq("targetUserId", timeEntity.getCoachUserId());
            //是否关注对方
            int isFollow = userSocialFollowMapper.selectCount(followWrapper);
            if (isFollow == 0) {
                followWrapper.clear();
                followWrapper.eq("deleted", "0");
                followWrapper.eq("userId", timeEntity.getCoachUserId());
                followWrapper.eq("targetUserId", userId);
                //是否被对方关注
                int isBeFollow = userSocialFollowMapper.selectCount(followWrapper);
                if (isBeFollow == 0) {
                    dto.setFollowStatus(FollowStatusEnum.NOT_FOLLOW);
                } else {
                    dto.setFollowStatus(FollowStatusEnum.BE_FOLLOW);
                }
            } else {
                dto.setFollowStatus(FollowStatusEnum.IN_FOLLOW);
            }
        } else {
            dto.setFollowStatus(FollowStatusEnum.FRIEND);
        }

        int orderNum = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                .eq("coachTimeId", timeEntity.getId())
                .eq("orderUserId", userId)
                .eq("isOrder", "1")
                .eq("deleted", "0"));
        dto.setIsOrder(orderNum + "");

        BaseDifficulty baseDifficulty = difficultyMapper.selectById(courseEntity.getDifficultyId());
        dto.setDifficulty(StringUtils.isNotEmpty(baseDifficulty) ? baseDifficulty.getDifficulty() : "");

        dto.setBeginTime(timeEntity.getBeginTime());
        dto.setEndTime(timeEntity.getEndTime());
        dto.setCoachHeadUrl(getHeadPicUrl(timeEntity.getCoachUserId()));
        dto.setConsume(courseEntity.getConsume());
        dto.setCoachUserId(timeEntity.getCoachUserId());
        dto.setCourseName(courseEntity.getCourseName());
        dto.setCourseSynopsis(courseEntity.getCourseSynopsis());
        dto.setCoursePrice(timeEntity.getPrice());
        dto.setCourseTime(courseEntity.getCourseTime());
        dto.setCourseType(courseEntity.getCourseType());
        dto.setLiveStatus(timeEntity.getLiveStatus());
        dto.setReplayUrl(timeEntity.getReplayUrl());
        //保存浏览记录
        //iViewRecordService.addViewRecord(ViewTypeEnum.LIVE.getCode(),timeId);
        return ResultUtil.success(dto);
    }

    @Override
    public Result getLiveList(String userId, String current, String type, String coachUserId, String courseType) {
        if (StringUtils.isEmpty(current)) {
            current = "1";
        }
        Page<LiveListDto> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        QueryWrapper<LiveListDto> wrapper = new QueryWrapper<>();
        wrapper.eq("a.deleted", "0");
        if (StringUtils.isNotEmpty(courseType)) {
            wrapper.like("b.courseType", courseType);
        }

        if (StringUtils.isNotEmpty(coachUserId)) {
            //是否限定教练
            wrapper.eq("a.coachUserId", coachUserId);
        }
        if ("0".equals(type)) {
            //往期直播
            wrapper.le("a.actualEndTime", LocalDateTime.now());
        } else if ("1".equals(type)) {
            //未来直播
            wrapper.ge("a.beginTime", LocalDateTime.now());
        } else if ("2".equals(type)) {
            //今日回放
            wrapper.le("a.actualEndTime", LocalDateTime.now());
            wrapper.eq("a.date", LocalDate.now());
        }

        wrapper.orderByAsc("a.beginTime");
        List<LiveListDto> liveList = userCoachTimeMapper.getLiveList(page, wrapper);
        for (LiveListDto live : liveList) {
            Attach attach = attachMapper.selectById(live.getPicAttachUrl());
            live.setPicAttachUrl(attach != null ? fileImagesPath + attach.getFilePath() : "");
            //是否预约
            int isOrderNum = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("coachTimeId", live.getTimeId())
                    .eq("orderUserId", userId)
                    .eq("isOrder", "1")
                    .eq("deleted", "0"));
            if (isOrderNum > 0) {
                //TODO播放地址
                live.setReplayUrl(live.getReplayUrl());
                live.setIsOrder("1");
            } else {
                live.setReplayUrl("");
                live.setIsOrder("0");
            }

            BaseDifficulty baseDifficulty = difficultyMapper.selectById(live.getDifficulty());
            live.setDifficulty(StringUtils.isNotEmpty(baseDifficulty) ? baseDifficulty.getDifficulty() : "");
            live.setCoachHeadUrl(getHeadPicUrl(live.getCoachUserId()));

            //预约人数
            List<String> isOrderList = new ArrayList<>();
            isOrderList.add("0");
            isOrderList.add("1");
            int orderNum = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("coachTimeId", live.getTimeId())
                    .in("isOrder", isOrderList)
                    .eq("deleted", "0"));
            live.setOrderNum(orderNum);
        }
        page.setRecords(liveList);
        return ResultUtil.success(page);
    }

    @Override
    public Result getLiveIngList(String userId, String current) {
        if (StringUtils.isEmpty(current)) {
            current = "1";
        }
        List<LiveIngListDto> dtoList = new ArrayList<>();
        Page<UserCoachTimeEntity> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.le("liveStatus", "1");
        wrapper.orderByAsc("beginTime");
        Page pageList = userCoachTimeMapper.selectPage(page, wrapper);
        List<UserCoachTimeEntity> list = pageList.getRecords();
        for (UserCoachTimeEntity entity : list) {
            LiveIngListDto dto = new LiveIngListDto();
            String coachCourseId = entity.getCoachCourseId();
            UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(coachCourseId);
            if (StringUtils.isEmpty(userCoachCourseEntity)) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "课程异常");
            }
            dto.setCourseType(userCoachCourseEntity.getCourseType());
            dto.setCourseName(userCoachCourseEntity.getCourseName());
            dto.setConsume(userCoachCourseEntity.getConsume());
            dto.setCourseTime(userCoachCourseEntity.getCourseTime());
            if (StringUtils.isNotEmpty(userCoachCourseEntity.getPicAttachId())) {
                Attach attach = attachMapper.selectById(userCoachCourseEntity.getPicAttachId());
                dto.setPicAttachUrl(attach != null ? fileImagesPath + attach.getFilePath() : "");
            }
            BaseDifficulty baseDifficulty = difficultyMapper.selectById(userCoachCourseEntity.getDifficultyId());
            dto.setDifficulty(StringUtils.isNotEmpty(baseDifficulty) ? baseDifficulty.getDifficulty() : "");

            String[] splitTarget = userCoachCourseEntity.getTargets().split(",");
            List<String> targetList = Arrays.asList(splitTarget);
            dto.setTargetList(targetList);

            String[] splitPart = userCoachCourseEntity.getParts().split(",");
            List<String> partList = Arrays.asList(splitPart);
            dto.setPartList(partList);

            //是否预约
            int isOrderNum = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("coachTimeId", entity.getId())
                    .eq("orderUserId", userId)
                    .eq("isOrder", "1")
                    .eq("deleted", "0"));
            dto.setIsOrder(isOrderNum + "");
            //预约人数
            List<String> isOrderList = new ArrayList<>();
            isOrderList.add("0");
            isOrderList.add("1");
            int orderNum = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>()
                    .eq("coachTimeId", entity.getId())
                    .in("isOrder", isOrderList)
                    .eq("deleted", "0"));
            dto.setOrderNum(orderNum);
            dto.setTimeId(entity.getId());
            dto.setCoachUserId(entity.getCoachUserId());
            User user = userMapper.selectById(entity.getCoachUserId());
            dto.setCoachName(user != null ? user.getNickName() : "");
            dto.setCoachHeadUrl(getHeadPicUrl(entity.getCoachUserId()));
            dtoList.add(dto);
        }
        Page<LiveIngListDto> pageDto = new Page<>();
        pageDto.setTotal(page.getTotal());
        pageDto.setCurrent(page.getCurrent());
        pageDto.setSize(page.getSize());
        pageDto.setRecords(dtoList);
        return ResultUtil.success(pageDto);
    }

    @Override
    public Result<Page<CoachCourseTimeDto>> queryCoachCourseTime(String current, String queryType) {
        Page<CoachCourseTimeDto> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        Page<CoachCourseTimeDto> pageList = userCoachCourseMapper.selectCoachCoursePageByStatus(page, UserUtils.getUserId(), queryType);
        pageList.getRecords().stream().forEach(e -> {
            if ("0".equals(queryType)
                    && LiveOrderStatusEnum.LIVE_CANCEL.getCode().equals(e.getIsOrder())) {
                e.setStatus(LiveCourseStatusEnum.LIVE_CANCEL.getCode());
            } else {
                e.setStatus(e.getLiveStatus());
            }
            UserDetail userDetail = getUserDetail(e.getUserId());
            e.setHeadPicUrl(userDetail.getHeadPicUrlId());
            e.setGender(userDetail.getGender());
            e.setBirthDay(userDetail.getBirthDay());
        });

        return ResultUtil.success(pageList);
    }

    @Override
    public Result publishCourse(String userId, SaveCoachCourseRequest vo) {
        log.info("publishCourse SaveCoachCourseRequest:{{}}",vo);
        //教练用户id
        String coachUserId = vo.getCoachUserId();
        UserCoachEntity userCoachEntity = userCoachMapper.selectOne(new QueryWrapper<UserCoachEntity>().eq("userId", coachUserId).eq("deleted", 0).last("limit 1"));
        log.info("publishCourse userCoachEntity:{{}}",userCoachEntity);
        if (StringUtils.isEmpty(userCoachEntity)) {
            return ResultUtil.error("U0999", "教练不存在");
        }
        UserCoachCourseEntity userCoachCourseEntity = new UserCoachCourseEntity();
        BeanUtils.copyProperties(vo, userCoachCourseEntity);
        log.info("publishCourse userCoachCourseEntity:{{}}",userCoachCourseEntity);
        UserCoachTimeEntity userCoachTimeEntity = new UserCoachTimeEntity();
        LocalDateTime beginTime = vo.getBeginTime();
        log.info("publishCourse beginTime:{{}}",vo.getBeginTime());
        log.info("publishCourse picAttachUrl:{{}}",vo.getPicAttachUrl());
       /* if(StringUtils.isNotEmpty(vo.getBeginTime())&&StringUtils.isNotEmpty(vo.getEndTime())){
            LocalDateTime beginTime = vo.getBeginTime();
            LocalDateTime endTime = vo.getEndTime();
        //验证时间是否重叠
        QueryWrapper<UserCoachTimeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("coachUserId",coachUserId);
        wrapper.and(i ->i.lt("beginTime",beginTime).gt("endTime",beginTime));
        int num1 = userCoachTimeMapper.selectCount(wrapper);
        wrapper.clear();
        wrapper.eq("deleted","0");
        wrapper.eq("coachUserId",coachUserId);
        wrapper.and(j ->j.lt("beginTime",endTime).gt("endTime",endTime));
        int num2 = userCoachTimeMapper.selectCount(wrapper);
        wrapper.clear();
        wrapper.eq("deleted","0");
        wrapper.eq("coachUserId",coachUserId);
        wrapper.and(k->k.ge("beginTime",beginTime).le("endTime",endTime));
        int num3 = userCoachTimeMapper.selectCount(wrapper);
        if (num1 >0 || num2>0 || num3>0){
            return ResultUtil.error("U0999", "时间存在重叠");
        }
            Duration duration = Duration.between(beginTime,endTime);
            userCoachCourseEntity.setCourseTime(duration.toMinutes()+"");
            userCoachTimeEntity.setDate(LocalDate.parse(beginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            userCoachTimeEntity.setBeginTime(beginTime);
            userCoachTimeEntity.setEndTime(endTime);
        }*/
        String picAttachId = "";
        if (StringUtils.isNotEmpty(vo.getPicAttachUrl())
                &&StringUtils.isNotEmpty(vo.getPicAttachUrl().getPicAttachName())
                && StringUtils.isNotEmpty(vo.getPicAttachUrl().getPicAttachNewName())
                && StringUtils.isNotEmpty(vo.getPicAttachUrl().getPicAttachSize())) {
            //保存选中图片
            Attach attach = attachService.saveAttach(userId, vo.getPicAttachUrl().getPicAttachName()
                    , vo.getPicAttachUrl().getPicAttachNewName(), vo.getPicAttachUrl().getPicAttachSize());
            picAttachId = attach.getId();
        }
        log.info("publishCourse picAttachId:{{}}",picAttachId);
        userCoachCourseEntity.setId(UUIDUtil.getShortUUID());
        userCoachCourseEntity.setCoachId(userCoachEntity.getId());
        userCoachCourseEntity.setPicAttachId(picAttachId);
        userCoachCourseEntity.setCreateBy(userId);
        userCoachCourseEntity.setCreateDate(LocalDateTime.now());
        userCoachCourseEntity.setDeleted("0");
        userCoachCourseEntity.setLastModifiedBy(userId);
        userCoachCourseEntity.setLastModifiedDate(LocalDateTime.now());
        log.info("publishCourse userCoachCourseEntity:{{}}",userCoachCourseEntity);
        userCoachCourseMapper.insert(userCoachCourseEntity);
        userCoachTimeEntity.setId(UUIDUtil.getShortUUID());
        userCoachTimeEntity.setDate(LocalDate.parse(beginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        userCoachTimeEntity.setBeginTime(beginTime);
        userCoachTimeEntity.setCoachCourseId(userCoachCourseEntity.getId());
        userCoachTimeEntity.setCoachUserId(coachUserId);
        userCoachTimeEntity.setLiveStatus("0");
        userCoachTimeEntity.setPrice(vo.getPrice());
        userCoachTimeEntity.setCreateBy(userId);
        userCoachTimeEntity.setCreateDate(LocalDateTime.now());
        userCoachTimeEntity.setDeleted("0");
        userCoachTimeEntity.setLastModifiedBy(userId);
        userCoachTimeEntity.setLastModifiedDate(LocalDateTime.now());
        log.info("publishCourse userCoachTimeEntity:{{}}",userCoachTimeEntity);
        userCoachTimeMapper.insert(userCoachTimeEntity);

        return ResultUtil.success();
    }


    private String getHeadPicUrl(String userId) {
        UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().eq("userId", userId).eq("deleted", "0"));
        if (userDetail != null) {
            Attach attach = attachMapper.selectById(userDetail.getHeadPicUrlId());
            return attach != null ? fileImagesPath + attach.getFilePath() : "";
        } else {
            return "";
        }
    }

    private UserDetail getUserDetail(String userId) {
        UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().eq("userId", userId).eq("deleted", "0"));
        if (userDetail != null) {
            Attach attach = attachMapper.selectById(userDetail.getHeadPicUrlId());
            userDetail.setHeadPicUrlId(attach != null ? fileImagesPath + attach.getFilePath() : "");
            return userDetail;
        } else {
            return null;
        }
    }

    public ImgDto getImgDto(String urlId) {
        //根据id获取图片信息
        Attach aPi = attachService.getInfoById(urlId);
        ImgDto imgDto = new ImgDto();
        if (aPi != null) {
            imgDto.setPicAttachUrl(fileImagesPath + aPi.getFilePath());
            imgDto.setPicAttachSize(aPi.getFileSize());
            String[] strs = (aPi.getFilePath()).split("\\?");
            imgDto.setPicAttachNewName(imageFoldername + strs[0]);
            imgDto.setPicAttachName(aPi.getFileName());
        }
        return imgDto;
    }
}
