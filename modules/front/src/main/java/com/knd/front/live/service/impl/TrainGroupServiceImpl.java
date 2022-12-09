package com.knd.front.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.qiniu.RtcRoomManager;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.domain.RankingTypeEnum;
import com.knd.front.entity.UserDetail;
import com.knd.front.live.dto.ApplyListDto;
import com.knd.front.live.dto.GroupUserListDto;
import com.knd.front.live.dto.TrainGroupDto;
import com.knd.front.live.dto.TrainGroupListDto;
import com.knd.front.live.entity.RoomEntity;
import com.knd.front.live.entity.TrainGroupApplyEntity;
import com.knd.front.live.entity.TrainGroupEntity;
import com.knd.front.live.entity.TrainGroupUserEntity;
import com.knd.front.live.mapper.*;
import com.knd.front.live.request.CreateOrUpdateTrainGroupRequest;
import com.knd.front.live.request.QueryTrainGroupRequest;
import com.knd.front.live.service.IRoomService;
import com.knd.front.live.service.ITrainGroupService;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.user.dto.RankingListDto;
import com.knd.front.user.dto.UserTrainDto;
import com.knd.front.user.mapper.UserTrainByUserMapper;
import com.knd.front.user.mapper.UserTrainMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Service
@Slf4j
public class TrainGroupServiceImpl extends ServiceImpl<TrainGroupMapper, TrainGroupEntity> implements ITrainGroupService {

    @Resource
    private UserTrainMapper userTrainMapper;

    @Resource
    private TrainGroupUserMapper trainGroupUserMapper;

    @Resource
    private TrainGroupApplyMapper trainGroupApplyMapper;

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private UserDetailMapper userDetailMapper;

    @Resource
    private IRoomService iRoomService;

    @Resource
    private UserTrainByUserMapper userTrainByUserMapper;

    @Resource
    private TrainGroupRankMapper trainGroupRankMapper;

    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    @Autowired
    private RtcRoomManager rtcRoomManager;

    @Value("${qiniu.appId}")
    private String appId;


    @Override
    public TrainGroupEntity insertReturnEntity(TrainGroupEntity entity) {
        return null;
    }

    @Override
    public TrainGroupEntity updateReturnEntity(TrainGroupEntity entity) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createGroup(CreateOrUpdateTrainGroupRequest createOrUpdateTrainGroupRequest) {
        log.info("createGroup createOrUpdateTrainGroupRequest:{{}}", createOrUpdateTrainGroupRequest);
        QueryWrapper<TrainGroupEntity> trainGroupEntityQueryWrapper = new QueryWrapper<>();
        trainGroupEntityQueryWrapper.eq("groupName", createOrUpdateTrainGroupRequest.getGroupName());
        trainGroupEntityQueryWrapper.eq("userId", UserUtils.getUserId());
        trainGroupEntityQueryWrapper.eq("deleted", "0");
        Integer integer = baseMapper.selectCount(trainGroupEntityQueryWrapper);
        if (integer > 0) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组名称已存在");
        }
        TrainGroupEntity trainGroupEntity = new TrainGroupEntity();
        BeanUtils.copyProperties(createOrUpdateTrainGroupRequest, trainGroupEntity);
        trainGroupEntity.setUserId(UserUtils.getUserId());
        log.info("createGroup trainGroupEntity:{{}}", trainGroupEntity);
        baseMapper.insert(trainGroupEntity);
        //默认在小组成员关系表中增加一条创建人的关系
        TrainGroupUserEntity trainGroupUserEntity = new TrainGroupUserEntity();
        trainGroupUserEntity.setUserId(UserUtils.getUserId());
        trainGroupUserEntity.setTrainGroupId(trainGroupEntity.getId());
        trainGroupUserEntity.setJoinStatus("1");
        trainGroupUserEntity.setIsAdmin("-1");
        trainGroupUserEntity.setJoinDate(LocalDateTime.now());
        log.info("createGroup trainGroupUserEntity:{{}}", trainGroupUserEntity);
        trainGroupUserMapper.insert(trainGroupUserEntity);
        return ResultUtil.success();
    }


    @Override
    public Result<Page<TrainGroupListDto>> groupList(QueryTrainGroupRequest queryTrainGroupRequest) {
        log.info("-----------------小组列表开始-------------------");
        String userId = UserUtils.getUserId();
        log.info("groupList queryTrainGroupRequest:{{}}", queryTrainGroupRequest);
        Page<TrainGroupListDto> page = new Page<>(Long.parseLong(queryTrainGroupRequest.getCurrentPage()), PageInfo.pageSize);
        QueryWrapper<TrainGroupEntity> qw = new QueryWrapper<>();
        QueryWrapper<TrainGroupEntity> commonQw = new QueryWrapper<>();
        qw.eq("ltg.deleted", "0");
        qw.eq("u.deleted", "0");
        commonQw.eq("ltg.deleted", "0");
        commonQw.eq("u.deleted", "0");
        qw.orderByDesc("ltg.createDate");
        commonQw.orderByDesc("ltg.createDate");
        if (StringUtils.isNotEmpty(queryTrainGroupRequest.getKeyword())) {

            //      qw.like("ltg.groupName",queryTrainGroupRequest.getKeyword()).or().like("ltg.targetId",queryTrainGroupRequest.getKeyword());
            qw.and(wrapper -> wrapper.like("ltg.groupName", queryTrainGroupRequest.getKeyword()).or().like("bt.target", queryTrainGroupRequest.getKeyword()));
            //     commonQw.like("ltg.groupName",queryTrainGroupRequest.getKeyword()).or().like("ltg.targetId",queryTrainGroupRequest.getKeyword());
            commonQw.and(wrapper -> wrapper.like("ltg.groupName", queryTrainGroupRequest.getKeyword()).or().like("bt.target", queryTrainGroupRequest.getKeyword()));
        }
        log.info("groupList QueryType():{{}}", queryTrainGroupRequest.getQueryType());
        if ("1".equals(queryTrainGroupRequest.getQueryType())) {
            //推荐
            qw.ne("ltg.userId", userId);
            commonQw.ne("ltg.userId", userId);
            UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>()
                    .eq("userId", userId).eq("deleted", "0"));
            String targetId = userDetail.getTargetId();
            log.info("------------------------------------------------------------------------------------------");
            log.info("groupList targetId:{{}}", targetId);
            log.info("------------------------------------------------------------------------------------------");
            qw.eq("ltg.targetId", targetId);
//            String[] split = userDetail.getHobbyId().split(",");
            //   String s = userDetail.getHobbyId().replaceAll(",", "|");
//            int index=(int)(Math.random()*split.length);
//            String radomHobbyId =split[index];
            //   String regexp = "'("+s+")'";
            //根据爱好推荐
            //      qw.last(" AND ltg.partHobbyId regexp " + regexp);

            //过滤掉自己已加入的房间
            qw.notExists("SELECT * FROM lb_train_group_user tga WHERE tga.trainGroupId = ltg.id and tga.userId='" + userId + "' and tga.joinStatus='1'");
            commonQw.notExists("SELECT * FROM lb_train_group_user tga WHERE tga.trainGroupId = ltg.id and tga.userId='" + userId + "' and tga.joinStatus='1'");
            page = baseMapper.groupList(page, qw);
            log.info("groupList 推荐page:{{}}", page);
            if (page.getRecords().size() == 0) {
                commonQw.orderByDesc("ltg.createDate");
                qw.orderByDesc("ltg.createDate");
                log.info("groupList commonQw:{{}}", commonQw.getSqlSelect());
                page = baseMapper.groupList(page, commonQw);
                log.info("groupList 推荐page:{{}}", page);
            }
            for (int i = 0; i < page.getRecords().size(); i++) {
//                TrainGroupApplyEntity trainGroupApplyEntity = trainGroupApplyMapper.selectLastApply(UserUtils.getUserId(), page.getRecords().get(i).getId());
                TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectOne(new QueryWrapper<TrainGroupUserEntity>()
                        .eq("userId", userId)
                        .eq("trainGroupId", page.getRecords().get(i).getId())
                        .eq("deleted", "0"));
                if (trainGroupUserEntity != null) {
                    page.getRecords().get(i).setApplyStatus(trainGroupUserEntity.getJoinStatus());
                } else {
                    page.getRecords().get(i).setApplyStatus("-1");
                }
                //当小组人数已满，则不在推荐
                Integer memberSize = page.getRecords().get(i).getMemberSize();
                List<TrainGroupUserEntity> TrainGroupUserEntitys = trainGroupUserMapper.selectList(new QueryWrapper<TrainGroupUserEntity>().eq("trainGroupId", page.getRecords().get(i).getId()).eq("joinStatus", "1"));
                if (memberSize.equals(TrainGroupUserEntitys.size())) {
                    page.getRecords().remove(page.getRecords().get(i));
                }

                log.info("groupList 推荐page:{{}}", page);
            }

            //    QueryWrapper<TrainGroupEntity> trainGroupEntityQueryWrapper = new QueryWrapper<TrainGroupEntity>().eq("userId",UserUtils.getUserId()).eq("deleted","0").eq()

            //qw.notExists("SELECT * FROM lb_train_group_apply tga WHERE tga.trainGroupId = ltg.id and tga.userId='"+UserUtils.getUserId()+"'");
        } else if ("2".equals(queryTrainGroupRequest.getQueryType())) {
            //我的

            qw.exists("SELECT * FROM lb_train_group_user tga WHERE tga.trainGroupId = ltg.id and tga.userId='" + userId + "' and tga.joinStatus='1'");
//            qw.and(e->{
//                e.exists("SELECT * FROM lb_train_group_user tga WHERE tga.trainGroupId = ltg.id and tga.userId='"+UserUtils.getUserId()+"' and tga.joinStatus='1'");
//                e.or(f->f.eq("ltg.userId",UserUtils.getUserId()));
//            });
            //qw.exists("SELECT * FROM lb_train_group_user tga WHERE tga.trainGroupId = ltg.id and tga.userId='"+UserUtils.getUserId()+"'");
            log.info("groupList qw:{{}}", qw.getSqlSelect());
            page = baseMapper.groupList(page, qw);
            for (int i = 0; i < page.getRecords().size(); i++) {
//                TrainGroupApplyEntity trainGroupApplyEntity = trainGroupApplyMapper.selectLastApply(UserUtils.getUserId(), page.getRecords().get(i).getId());
                TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectOne(new QueryWrapper<TrainGroupUserEntity>()
                        .eq("userId", userId)
                        .eq("trainGroupId", page.getRecords().get(i).getId())
                        .eq("deleted", "0"));
                if (trainGroupUserEntity != null) {
                    page.getRecords().get(i).setApplyStatus(trainGroupUserEntity.getJoinStatus());
                } else {
                    page.getRecords().get(i).setApplyStatus("-1");
                }
            }
        } else if ("3".equals(queryTrainGroupRequest.getQueryType())) {
            //全部
            qw.exists("SELECT * FROM lb_train_group_user tga WHERE tga.trainGroupId = ltg.id and tga.joinStatus='1'");
//            qw.and(e->{
//                e.exists("SELECT * FROM lb_train_group_user tga WHERE tga.trainGroupId = ltg.id and tga.userId='"+UserUtils.getUserId()+"' and tga.joinStatus='1'");
//                e.or(f->f.eq("ltg.userId",UserUtils.getUserId()));
//            });
            //qw.exists("SELECT * FROM lb_train_group_user tga WHERE tga.trainGroupId = ltg.id and tga.userId='"+UserUtils.getUserId()+"'");
            qw.orderByDesc("ltg.createDate");
            page = baseMapper.groupList(page, qw);
            for (int i = 0; i < page.getRecords().size(); i++) {
//                TrainGroupApplyEntity trainGroupApplyEntity = trainGroupApplyMapper.selectLastApply(UserUtils.getUserId(), page.getRecords().get(i).getId());
                TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectOne(new QueryWrapper<TrainGroupUserEntity>()
                        .eq("userId", userId)
                        .eq("trainGroupId", page.getRecords().get(i).getId())
                        .eq("deleted", "0"));
                if (trainGroupUserEntity != null) {
                    page.getRecords().get(i).setApplyStatus(trainGroupUserEntity.getJoinStatus());
                } else {
                    page.getRecords().get(i).setApplyStatus("-1");
                }
            }
        }
        List<TrainGroupListDto> collect = page.getRecords().stream().map(e -> {
            //Attach attach = attachMapper.selectById(e.);
            e.setHeadPicUrl(fileImagesPath + e.getHeadPicUrl());
            return e;
        }).collect(Collectors.toList());
        log.info("groupList collect:{{}}", collect);
        page.setRecords(collect);
        page.setTotal(collect.size());
        log.info("groupList page:{{}}", page);
        log.info("-----------------小组列表结束拉-------------------");
        return ResultUtil.success(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result editGroup(CreateOrUpdateTrainGroupRequest createOrUpdateTrainGroupRequest) {
        String role = getUserRole4Group(createOrUpdateTrainGroupRequest.getId());
        if (!"-1".equals(role)) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "非小组创建人无权限操作");
        }
        TrainGroupEntity trainGroupEntity = new TrainGroupEntity();
        BeanUtils.copyProperties(createOrUpdateTrainGroupRequest, trainGroupEntity);
        baseMapper.deleteById(trainGroupEntity.getId());
        QueryWrapper<TrainGroupEntity> trainGroupEntityQueryWrapper = new QueryWrapper<>();
        trainGroupEntityQueryWrapper.eq("groupName", createOrUpdateTrainGroupRequest.getGroupName());
        trainGroupEntityQueryWrapper.eq("userId", UserUtils.getUserId());
        trainGroupEntityQueryWrapper.eq("deleted", "0");
        Integer integer = baseMapper.selectCount(trainGroupEntityQueryWrapper);
        if (integer > 1) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组名称已存在");
        }
        baseMapper.insert(trainGroupEntity);
        return ResultUtil.success();
    }

    @Override
    @Transactional
    public Result joinGroup(String groupId) {
        log.info("joinGroup groupId:{{}}:", groupId);
        TrainGroupEntity trainGroupEntity = getGroup(groupId);

        if ("1".equals(trainGroupEntity.getDeleted())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "无效的小组");
        }
        QueryWrapper<TrainGroupUserEntity> trainGroupUserEntityQueryWrapper = new QueryWrapper<>();
        trainGroupUserEntityQueryWrapper.eq("trainGroupId", groupId);
        trainGroupUserEntityQueryWrapper.eq("joinStatus", "1");
        Integer count = trainGroupUserMapper.selectCount(trainGroupUserEntityQueryWrapper);
        List<TrainGroupUserEntity> trainGroupUserEntities = trainGroupUserMapper.selectList(trainGroupUserEntityQueryWrapper);
        log.info("joinGroup trainGroupUserEntities:{{}}", trainGroupUserEntities);
        log.info("joinGroup count:{{}}:", count);
        log.info("joinGroup memberSize:{{}}", trainGroupEntity.getMemberSize());
        if (count >= trainGroupEntity.getMemberSize()) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组成员已满");
        }
        TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectOne(new QueryWrapper<TrainGroupUserEntity>()
                .eq("userId", UserUtils.getUserId())
                .eq("trainGroupId", groupId)
                .eq("deleted", "0"));
        if (trainGroupUserEntity != null) {
            if ("0".equals(trainGroupUserEntity.getJoinStatus())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "审核中");
            } else {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "已加入该小组");
            }
        }
        //判断是否房间是否公开
        if ("1".equals(trainGroupEntity.getPublicFlag())) {
            QueryWrapper<TrainGroupUserEntity> trainGroupUserEntityWrapper = new QueryWrapper<>();
            trainGroupUserEntityWrapper.eq("userId", UserUtils.getUserId());
            trainGroupUserEntityWrapper.eq("trainGroupId", groupId);
            trainGroupUserEntityWrapper.eq("deleted", "0");
            TrainGroupUserEntity trainGroupUserEntity1 = trainGroupUserMapper.selectOne(trainGroupUserEntityWrapper);
            if (null == trainGroupUserEntity1) {
                trainGroupUserEntity = new TrainGroupUserEntity();
                trainGroupUserEntity.setUserId(UserUtils.getUserId());
                trainGroupUserEntity.setTrainGroupId(groupId);
                trainGroupUserEntity.setJoinDate(LocalDateTime.now());
                trainGroupUserEntity.setJoinStatus("1");
                trainGroupUserMapper.insert(trainGroupUserEntity);
            }

        } else {
            TrainGroupApplyEntity trainGroupApplyEntity = trainGroupApplyMapper.selectOne(new QueryWrapper<TrainGroupApplyEntity>()
                    .eq("userId", UserUtils.getUserId())
                    .eq("trainGroupId", groupId)
                    .eq("applyFlag", "0")
                    .eq("deleted", "0"));
            if (trainGroupApplyEntity != null) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "请等待管理员审核");
            }
            TrainGroupApplyEntity trainGroupApplyEntity1 = new TrainGroupApplyEntity();
            trainGroupApplyEntity1.setUserId(UserUtils.getUserId());
            trainGroupApplyEntity1.setTrainGroupId(groupId);
            trainGroupApplyEntity1.setApplyDate(LocalDateTime.now());
            trainGroupApplyEntity1.setApplyFlag("0");
            trainGroupApplyMapper.insert(trainGroupApplyEntity1);

            trainGroupUserEntity = new TrainGroupUserEntity();
            trainGroupUserEntity.setUserId(UserUtils.getUserId());
            trainGroupUserEntity.setTrainGroupId(groupId);
            trainGroupUserEntity.setJoinDate(LocalDateTime.now());
            trainGroupUserEntity.setJoinStatus("0");
            trainGroupUserMapper.insert(trainGroupUserEntity);

        }

        return ResultUtil.success();
    }

    @Override
    public TrainGroupEntity getGroup(String groupId) {
        return baseMapper.selectById(groupId);
//        if(trainGroupEntity.getUserId().equals(userId)) {
//            return true;
//        }else{
//            return false;
//        }
    }


    private String getUserRole4Group(String groupId) {
        TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectOne(new QueryWrapper<TrainGroupUserEntity>()
                .eq("userId", UserUtils.getUserId())
                .eq("trainGroupId", groupId)
                .eq("deleted", "0"));
        log.info("getUserRole4Group userId:{{}}", UserUtils.getUserId());
        log.info("getUserRole4Group trainGroupUserEntity:{{}}", trainGroupUserEntity);
        if (trainGroupUserEntity == null) {
            return "";
        } else {
            return trainGroupUserEntity.getIsAdmin();
        }
    }

    @Override
    public Result<String> getGroupRole(String groupId) {
        String role = getUserRole4Group(groupId);
        return ResultUtil.success(role);
    }

    @Override
    public Result getRankingList(RankingTypeEnum type, LocalDate date, String userId, String groupId) {
        if (date == null) {
            date = LocalDate.now();
        }
      /*  //本月开始时间
        LocalDate firstday = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime month_start = LocalDateTime.of(firstday, LocalTime.MIN);
        //本月结束时间
        LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime month_end = LocalDateTime.of(lastDay, LocalTime.MAX);*/

        List<UserTrainDto> userTrainList;
        UserTrainDto userTrainDto;

        ////毅力得字段存在不一样，所以无法统一使用
        if (type.equals(RankingTypeEnum.WILL)) {
            userTrainList = trainGroupRankMapper.getWill(userId, groupId, date);
            for (UserTrainDto dto : userTrainList) {
                if (StringUtils.isEmpty(dto.getTrainNum())) {
                    dto.setTrainNum("0");
                }
                if (StringUtils.isNotEmpty(dto.getHeadPicUrl())) {
                    dto.setHeadPicUrl(fileImagesPath + dto.getHeadPicUrl());
                }
            }
            userTrainDto = userTrainByUserMapper.getUserWill(userId, date);
            userTrainDto = StringUtils.isNotEmpty(userTrainDto) ? userTrainDto : new UserTrainDto();
            if (StringUtils.isNotEmpty(userTrainDto.getHeadPicUrl())) {
                userTrainDto.setHeadPicUrl(fileImagesPath + userTrainDto.getHeadPicUrl());
            }
        } else {
            QueryWrapper<UserTrainDto> wrapper = new QueryWrapper<>();
            wrapper.eq("gp.trainGroupId", groupId);
            wrapper.eq("gp.joinStatus", 1);
            wrapper.groupBy("u.id");
            wrapper.orderByDesc("length(trainNum)","trainNum","length(praiseNum)", "praiseNum");
            userTrainList = trainGroupRankMapper.getGroupTrainByParam(userId, groupId, type.getParamType(), type.getPraiseType(), date, wrapper);
            for (UserTrainDto dto : userTrainList) {
                if (StringUtils.isEmpty(dto.getTrainNum())) {
                    dto.setTrainNum("0");
                }
                if (StringUtils.isNotEmpty(dto.getHeadPicUrl())) {
                    dto.setHeadPicUrl(fileImagesPath + dto.getHeadPicUrl());
                }
            }
            wrapper.clear();
            wrapper.eq("u.id", userId);
            List<UserTrainDto> userTrainListByUser = userTrainMapper.getUserTrainByParam(userId, date, type.getParamType(), type.getPraiseType(), wrapper);
            userTrainDto = StringUtils.isNotEmpty(userTrainListByUser) ? userTrainListByUser.get(0) : new UserTrainDto();
            if (StringUtils.isNotEmpty(userTrainDto.getHeadPicUrl())) {
                userTrainDto.setHeadPicUrl(fileImagesPath + userTrainDto.getHeadPicUrl());
            }
        }

        RankingListDto dto = RankingListDto.builder().userTrainDto(userTrainDto).userTrainList(userTrainList).build();
        return ResultUtil.success(dto);
    }

    @Override
    public Result<TrainGroupDto> groupById(String groupId) {
        QueryWrapper<TrainGroupEntity> qw = new QueryWrapper<>();
        qw.eq("ltg.id", groupId);
        qw.eq("ltg.deleted", "0");
        qw.eq("u.deleted", "0");
        TrainGroupDto trainGroupDto = baseMapper.groupById(qw);
        if (StringUtils.isNotEmpty(trainGroupDto.getHeadPicUrl())) {
            trainGroupDto.setHeadPicUrl(fileImagesPath + trainGroupDto.getHeadPicUrl());
        }
        return ResultUtil.success(trainGroupDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result signOutGroup(String groupId) {
        //小组创建人不允许自己退出小组
        String role = getUserRole4Group(groupId);
        if ("-1".equals(role)) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组创建人暂不支持退出小组");
        }
        //退出小组前应当先关闭所创建的活动
        List<RoomEntity> roomList = roomMapper.selectList(
                new QueryWrapper<RoomEntity>().eq("userId", UserUtils.getUserId())
                        .eq("trainGroupId", groupId));
        List<RoomEntity> openingRooms = roomList.stream().filter(e -> "0".equals(e.getRoomStatus())).collect(Collectors.toList());
//        LocalDateTime now = LocalDateTime.now();
//        for(int i = 0;i<openingRooms.size();i++) {
//            if(openingRooms.get(i).getBeginDate().isAfter(now)||openingRooms.get(i).getBeginDate().isEqual(now)){
//                return ResultUtil.error(ResultEnum.FAIL.getCode(),"您在当前小组中有正在直播的活动，请先关闭");
//            }
//        };

        //关闭小组下所有创建的活动
        openingRooms.forEach(e -> {
            e.setRoomStatus("1");
            roomMapper.updateById(e);
        });
        //将用户从参与的房间中踢出
        List<RoomEntity> joinRoomList = roomMapper.selectList(
                new QueryWrapper<RoomEntity>().ne("userId", UserUtils.getUserId())
                        .eq("trainGroupId", groupId)
                        .eq("roomStatus", "0"));
        iRoomService.kickUser(joinRoomList, UserUtils.getUserId());


        //删除小组成员关系
        TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectOne(new QueryWrapper<TrainGroupUserEntity>()
                .eq("userId", UserUtils.getUserId())
                .eq("trainGroupId", groupId)
                .eq("deleted", "0"));
        if (trainGroupUserEntity != null) {
            trainGroupUserMapper.deleteById(trainGroupUserEntity.getId());
        }


        return ResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result kickOutGroup(String id) {
        TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectById(id);
        if (trainGroupUserEntity == null || !"1".equals(trainGroupUserEntity.getJoinStatus())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "非法操作");
        }
        //判断是否是创建人且不能踢自己
        String role = getUserRole4Group(trainGroupUserEntity.getTrainGroupId());
        if (!"-1".equals(role) && !"1".equals(role)) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "非小组创建人及管理员无权限操作");
        }
        if (trainGroupUserEntity.getUserId().equals(UserUtils.getUserId())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "暂不支持踢出自己");
        }
        TrainGroupEntity trainGroupEntity = getGroup(trainGroupUserEntity.getTrainGroupId());
        if (!trainGroupEntity.getUserId().equals(UserUtils.getUserId())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组创建人暂不支持被踢出");
        }
        //踢出小组前应当先关闭该用户所创建的活动
        List<RoomEntity> roomList = roomMapper.selectList(
                new QueryWrapper<RoomEntity>().eq("userId", trainGroupUserEntity.getUserId())
                        .eq("trainGroupId", trainGroupEntity.getId())
                        .eq("roomStatus", "0"));

        //关闭小组下所有创建的活动
        roomList.forEach(e -> {
            e.setRoomStatus("1");
            roomMapper.updateById(e);
            //ToDo 将用户从房间中踢出
        });

        //将用户从参与的房间中踢出
        List<RoomEntity> joinRoomList = roomMapper.selectList(
                new QueryWrapper<RoomEntity>().ne("userId", trainGroupUserEntity.getUserId())
                        .eq("trainGroupId", trainGroupEntity.getId())
                        .eq("roomStatus", "0"));
        iRoomService.kickUser(joinRoomList, trainGroupUserEntity.getUserId());
        //删除小组成员关系
        trainGroupUserMapper.deleteById(id);

//        TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectOne(new QueryWrapper<TrainGroupUserEntity>()
//                .eq("userId", UserUtils.getUserId())
//                .eq("trainGroupId", groupId));
//        if(trainGroupUserEntity!= null) {
//            trainGroupUserMapper.deleteById(trainGroupUserEntity.getId());
//        }

        return ResultUtil.success();
    }

    @Override
    public Result kickOutGroupForManage(String id) {
        log.info("kickOutGroupForManage id:{{}}", id);
        TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectById(id);
        log.info("kickOutGroupForManage trainGroupUserEntity:{{}}", trainGroupUserEntity);
        if (trainGroupUserEntity == null || !"1".equals(trainGroupUserEntity.getJoinStatus())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "非法操作");
        }

        TrainGroupEntity trainGroupEntity = getGroup(trainGroupUserEntity.getTrainGroupId());
        log.info("kickOutGroupForManage trainGroupEntity:{{}}", trainGroupEntity);
        //踢出小组前应当先关闭该用户所创建的活动
        List<RoomEntity> roomList = roomMapper.selectList(
                new QueryWrapper<RoomEntity>().eq("userId", trainGroupUserEntity.getUserId())
                        .eq("trainGroupId", trainGroupEntity.getId())
                        .eq("roomStatus", "0"));

        log.info("kickOutGroupForManage roomList:{{}}", roomList);
        //关闭小组下所有创建的活动
        roomList.forEach(e -> {
            e.setRoomStatus("1");
            roomMapper.updateById(e);
            //ToDo 将用户从房间中踢出
        });

        //将用户从参与的房间中踢出
        List<RoomEntity> joinRoomList = roomMapper.selectList(
                new QueryWrapper<RoomEntity>().ne("userId", trainGroupUserEntity.getUserId())
                        .eq("trainGroupId", trainGroupEntity.getId())
                        .eq("roomStatus", "0"));
        log.info("kickOutGroupForManage joinRoomList:{{}}", joinRoomList);
        iRoomService.kickUser(joinRoomList, trainGroupUserEntity.getUserId());
        //删除小组成员关系
        trainGroupUserMapper.deleteById(id);

        return ResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result examine(String id, String status) {
        log.info("---------------我要开始审批拉----------------------");
        log.info("examine id:{{}}", id);
        log.info("examine status:{{}}", status);
        TrainGroupApplyEntity trainGroupApplyEntity = trainGroupApplyMapper.selectById(id);
        log.info("examine trainGroupApplyEntity:{{}}", trainGroupApplyEntity);
        if (trainGroupApplyEntity == null || !"0".equals(trainGroupApplyEntity.getApplyFlag())) {
            log.info("---------------非法操作----------------------");
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "非法操作");
        }
        String role = getUserRole4Group(trainGroupApplyEntity.getTrainGroupId());
        if (!"-1".equals(role) && !"1".equals(role)) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "非小组创建人及管理员无权限操作");
        }

        //TrainGroupEntity trainGroupEntity = getGroup(trainGroupApplyEntity.getTrainGroupId());


        QueryWrapper<TrainGroupEntity> trainGroupEntityQueryWrapper = new QueryWrapper<>();
        trainGroupEntityQueryWrapper.eq("id", trainGroupApplyEntity.getTrainGroupId());
        trainGroupEntityQueryWrapper.eq("deleted", "0");
        TrainGroupEntity trainGroupEntity = baseMapper.selectOne(trainGroupEntityQueryWrapper);
        Integer memberSize = trainGroupEntity.getMemberSize();

        if ("1".equals(status)) {
            TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectOne(new QueryWrapper<TrainGroupUserEntity>()
                    .eq("userId", trainGroupApplyEntity.getUserId())
                    .eq("trainGroupId", trainGroupApplyEntity.getTrainGroupId())
                    .eq("deleted", "0"));
            trainGroupUserEntity.setJoinDate(LocalDateTime.now());
            trainGroupUserEntity.setJoinStatus("1");
            log.info("examine trainGroupUserEntity:{{}}", trainGroupUserEntity);
            List<TrainGroupUserEntity> trainGroupUserEntityList = trainGroupUserMapper.selectList(new QueryWrapper<TrainGroupUserEntity>()
                    .eq("trainGroupId", trainGroupApplyEntity.getTrainGroupId())
                    .eq("joinStatus", "1"));
            log.info("examine trainGroupUserEntityList:{{}}", trainGroupUserEntityList);
            log.info("examine size:{{}}", trainGroupUserEntityList.size());
            log.info("examine memberSize:{{}}", memberSize);
            if (trainGroupUserEntityList.size() >= memberSize) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "小组成员已满");
            }

            if (trainGroupUserEntity != null) {
                trainGroupUserMapper.updateById(trainGroupUserEntity);
            }
            //更新入组关系
//            TrainGroupUserEntity trainGroupUserEntity = new TrainGroupUserEntity();
//            trainGroupUserEntity.setUserId(userId);
//            trainGroupUserEntity.setTrainGroupId(trainGroupApplyEntity.getTrainGroupId());
//            trainGroupUserEntity.setJoinDate(LocalDateTime.now());
//            trainGroupUserMapper.insert(trainGroupUserEntity);
        } else {
            TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectOne(new QueryWrapper<TrainGroupUserEntity>()
                    .eq("userId", trainGroupApplyEntity.getUserId())
                    .eq("trainGroupId", trainGroupApplyEntity.getTrainGroupId())
                    .eq("deleted", "0"));
            if (trainGroupUserEntity != null) {
                trainGroupUserMapper.deleteById(trainGroupUserEntity.getId());
            }
        }
        trainGroupApplyEntity.setApplyFlag(status);
        trainGroupApplyEntity.setExamineDate(LocalDateTime.now());
        trainGroupApplyMapper.updateById(trainGroupApplyEntity);
        return ResultUtil.success();
    }

    @Override
    public Result changeUserRole(String id, String role) throws Exception {
        Map<String, String> map = new HashMap<>();
      //  try {
            log.info("changeUserRole id:{{}}", id);
            log.info("changeUserRole role:{{}}", role);
            TrainGroupUserEntity trainGroupUserEntity = trainGroupUserMapper.selectById(id);
            log.info("changeUserRole trainGroupUserEntity:{{}}", trainGroupUserEntity);
            if (trainGroupUserEntity == null || !"1".equals(trainGroupUserEntity.getJoinStatus())
                    || "-1".equals(trainGroupUserEntity.getIsAdmin())) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "非法操作");
            }
            String role1 = getUserRole4Group(trainGroupUserEntity.getTrainGroupId());
            log.info("changeUserRole role1:{{}}", role1);
            if (!"-1".equals(role1) && !"1".equals(role1)) {
                return ResultUtil.error(ResultEnum.FAIL.getCode(), "非小组创建人及管理员无权限操作");
            }
//        TrainGroupEntity trainGroupEntity = getGroup(trainGroupUserEntity.getTrainGroupId());
//        //不允许修改创建人身份
//        if(UserUtils.getUserId().equals(trainGroupUserEntity.getUserId())) {
//            return ResultUtil.success();
//        }
//        //不允许创建人操作自己
//        if(UserUtils.getUserId().equals(trainGroupUserEntity.getUserId())) {
//            return ResultUtil.success();
//        }
           // QueryWrapper<RoomEntity> roomEntityQueryWrapper = new QueryWrapper<>();
            //roomEntityQueryWrapper.eq("trainGroupId", trainGroupUserEntity.getTrainGroupId());
          //  roomEntityQueryWrapper.eq("userId", "jTbRL3Rj");
           // roomEntityQueryWrapper.eq("deleted", 0);
           // RoomEntity roomEntity = roomMapper.selectOne(roomEntityQueryWrapper);
            //log.info("changeUserRole roomEntity:{{}}", roomEntity);
            String roomToken = rtcRoomManager.getRoomToken(appId, trainGroupUserEntity.getTrainGroupId(), UserUtils.getUserId(), DateUtils.addDate(new Date(), 30).getTime(), role);
            log.info("getRoomToken roomToken:{{}}", roomToken);
            log.info("获取签发roomToken：" + roomToken);
            map.put("roomToken", roomToken);
            if (!trainGroupUserEntity.getIsAdmin().equals(role)) {
                trainGroupUserEntity.setIsAdmin(role);
                trainGroupUserMapper.updateById(trainGroupUserEntity);
            }
      //  } catch (Exception e) {
       //     e.printStackTrace();
        //    return ResultUtil.error(ResultEnum.FAIL.getCode(), "服务器异常,获取roomToken失败");
       // }

        return ResultUtil.success(map);
    }

    @Override
    public Result<Page<ApplyListDto>> applyList(String groupId, String applyStatus, String currentPage) {
        log.info("-------------我要开始申请加入列表拉-------------------------------");
        log.info("applyList groupId:{{}}", groupId);
        log.info("applyList applyStatus:{{}}", applyStatus);
        log.info("applyList currentPage:{{}}", currentPage);
        log.info("applyList userId:{{}}", UserUtils.getUserId());
        String role = getUserRole4Group(groupId);
        log.info("applyList role:{{}}", role);
        if (!"-1".equals(role) && !"1".equals(role)) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "非小组创建人及管理员无权限操作");
        }
        Page<ApplyListDto> page = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        Page<ApplyListDto> appleListDtoPage = null;
        if (StringUtils.isEmpty(applyStatus)) {
            appleListDtoPage = trainGroupApplyMapper.appleList(page, groupId, UserUtils.getUserId());
            log.info("applyList appleListDtoPage:{{}}", appleListDtoPage);
        } else {
            appleListDtoPage = trainGroupApplyMapper.appleListByStatus(page, groupId, applyStatus, UserUtils.getUserId());
            log.info("applyList appleListDtoPage:{{}}", appleListDtoPage);
        }

        List<ApplyListDto> collect = appleListDtoPage.getRecords().stream().map(e -> {
            //Attach attach = attachMapper.selectById(e.);
            e.setHeadPicUrl(fileImagesPath + e.getHeadPicUrl());
            return e;
        }).collect(Collectors.toList());
        log.info("applyList collect:{{}}", collect);
        appleListDtoPage.setRecords(collect);
        log.info("applyList appleListDtoPage:{{}}", appleListDtoPage);
        log.info("-------------申请加入列表结束拉------------------------------");
        return ResultUtil.success(appleListDtoPage);
    }

    @Override
    public Result<Page<GroupUserListDto>> groupUserList(String groupId, String currentPage) {
        log.info("---------------小组成员列表分页-----------------");
        log.info("groupUserList groupId:{{}}", groupId);
        log.info("groupUserList currentPage:{{}}", currentPage);
        Page<GroupUserListDto> page = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        Page<GroupUserListDto> appleListDtoPage = trainGroupUserMapper.groupUserList(page, groupId);
        List<GroupUserListDto> collect = appleListDtoPage.getRecords().stream().map(e -> {
            //Attach attach = attachMapper.selectById(e.);
            e.setHeadPicUrl(fileImagesPath + e.getHeadPicUrl());
            return e;
        }).collect(Collectors.toList());
        log.info("groupUserList collect:{{}}", collect);
        appleListDtoPage.setRecords(collect);
        return ResultUtil.success(appleListDtoPage);
    }

    @Override
    public Result<GroupUserListDto> groupUserList(String groupId) {

        List<GroupUserListDto> appleListDtoList = trainGroupUserMapper.groupUserListNew(groupId);
        List<GroupUserListDto> collect = appleListDtoList.stream().map(e -> {
            //Attach attach = attachMapper.selectById(e.);
            e.setHeadPicUrl(fileImagesPath + e.getHeadPicUrl());
            return e;
        }).collect(Collectors.toList());

        return ResultUtil.success(collect);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteGroup(String id) {
        //判断是否是创建人
        String role = getUserRole4Group(id);
        if (!"-1".equals(role)) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "非小组创建人无权限操作");
        }

        TrainGroupEntity trainGroupEntity = getGroup(id);
        if (trainGroupEntity == null || "1".equals(trainGroupEntity.getDeleted())) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "无效的小组");
        }
        //退出小组前应当先关闭所创建的活动
//        List<RoomEntity> roomList = roomMapper.selectList(
//                new QueryWrapper<RoomEntity>().eq("userId", UserUtils.getUserId())
//                        .eq("trainGroupId", id));
//        List<RoomEntity> openingRooms = roomList.stream().filter(e -> "0".equals(e.getRoomStatus())).collect(Collectors.toList());
//        LocalDateTime now = LocalDateTime.now();
//        for(int i = 0;i<openingRooms.size();i++) {
//            if(openingRooms.get(i).getBeginDate().isAfter(now)||openingRooms.get(i).getBeginDate().isEqual(now)){
//                return ResultUtil.error(ResultEnum.FAIL.getCode(),"您在当前小组中有正在直播的活动，请先关闭");
//            }
//        };
        //关闭小组下所有创建的活动
        List<RoomEntity> roomList = roomMapper.selectList(
                new QueryWrapper<RoomEntity>().eq("trainGroupId", id));
        roomList.forEach(e -> {
            e.setRoomStatus("1");
            roomMapper.updateById(e);
            //ToDo 将用户从房间中踢出
        });

        //删除小组
        trainGroupEntity.setDeleted("1");
        baseMapper.updateById(trainGroupEntity);
        return ResultUtil.success();
    }
}
