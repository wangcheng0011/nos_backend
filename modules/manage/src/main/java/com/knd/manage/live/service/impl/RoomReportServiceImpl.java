package com.knd.manage.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.course.entity.RoomReportAttach;
import com.knd.manage.course.mapper.RoomReportAttachMapper;
import com.knd.manage.live.dto.RoomReportDto;
import com.knd.manage.live.entity.RoomEntity;
import com.knd.manage.live.entity.RoomReportEntity;
import com.knd.manage.live.entity.TrainGroupUserEntity;
import com.knd.manage.live.mapper.RoomMapper;
import com.knd.manage.live.mapper.RoomReportMapper;
import com.knd.manage.live.mapper.TrainGroupUserMapper;
import com.knd.manage.live.request.ManageRequest;
import com.knd.manage.live.request.RoomReportListRequest;
import com.knd.manage.live.service.RoomReportService;
import com.knd.manage.live.service.feignInterface.FrontReportFeign;
import com.knd.manage.mall.entity.UserCoachTimeEntity;
import com.knd.manage.mall.mapper.UserCoachTimeMapper;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 直播房间举报管理
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomReportServiceImpl implements RoomReportService {
    private final RoomMapper roomMapper;
    private final TrainGroupUserMapper trainGroupUserMapper;
    private final RoomReportMapper reportMapper;
    private final UserMapper userMapper;
    private final FrontReportFeign frontReportFeign;
    private final RoomReportAttachMapper roomReportAttachMapper;
    private final IAttachService iAttachService;
    private final UserCoachTimeMapper userCoachTimeMapper;
    // @Resource
    // private RtcRoomManager rtcRoomManager;

    @Value("${qiniu.appId}")
    private String appId;

    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    @Override
    public Result getRoomReportList(RoomReportListRequest vo) {
        log.info("------------------------我要房间获取举报列表啦--------------------------");
        log.info("getRoomReportList RoomReportListRequest：{{}}", vo);
        List<RoomReportDto> dtoList = new ArrayList<>();
        Page<RoomReportDto> page = new Page<>(Long.parseLong(vo.getCurrent()), PageInfo.pageSize);
        QueryWrapper<RoomReportEntity> wrapper = new QueryWrapper();
        if (StringUtils.isNotEmpty(vo.getNickName())) {
            wrapper.like("u.nickName", vo.getNickName());
        }
        if (StringUtils.isNotEmpty(vo.getMobile())) {
            wrapper.like("u.mobile", vo.getMobile());
        }
        if (StringUtils.isNotEmpty(vo.getType())) {
            wrapper.eq("r.type", vo.getType());
        }
        wrapper.eq("r.deleted", "0");
        wrapper.orderByDesc("r.createDate");
        Page<RoomReportDto> roomReportEntityPage = reportMapper.selectRoomReportPage(page, wrapper);
        List<RoomReportDto> list = roomReportEntityPage.getRecords();
        log.info("getRoomReportList list：{{}}", list);
        for (RoomReportDto entity : list) {
            RoomReportDto dto = new RoomReportDto();
            if ("0".equals(vo.getType())) {
                //举报房间
                RoomEntity roomEntity = roomMapper.selectById(entity.getRoomId());
                log.info("getRoomReportList roomEntity：{{}}", roomEntity);
                if (roomEntity != null) {
                    BeanUtils.copyProperties(entity, dto);
                    User roomUser = userMapper.selectById(roomEntity.getUserId());
                    log.info("getRoomReportList User：{{}}", roomUser);
                    dto.setRoomId(roomEntity.getId());
                    dto.setUserId(roomEntity.getUserId());
                    dto.setRoom(roomEntity.getRoomName());
                    // dto.setType(entity.getType());
                    dto.setRoomMobile(roomUser.getMobile());
                    dto.setRoomUserName(roomUser != null ? roomUser.getNickName() : "");
                    dto.setIsFrozen(roomUser != null ? roomUser.getFrozenFlag() : "1");
                    dto.setIsClose(roomEntity.getRoomStatus());
                    int count = trainGroupUserMapper.selectCount(new QueryWrapper<TrainGroupUserEntity>().eq("userId", roomEntity.getUserId()).eq("trainGroupId", roomEntity.getTrainGroupId()));
                    dto.setIsKickOut(count + "");
                    //   User reportUser = userMapper.selectById(entity.getReportUserId());
                    //   dto.setReportUserName(reportUser != null ? reportUser.getNickName() : "");
                    dtoList.add(dto);
                }
            } else if ("1".equals(vo.getType())) {
                //举报直播
                QueryWrapper<UserCoachTimeEntity> userCoachTimeEntityQueryWrapper = new QueryWrapper<>();
                userCoachTimeEntityQueryWrapper.eq("id", entity.getRoomId());
                userCoachTimeEntityQueryWrapper.eq("deleted", "0");
                UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(userCoachTimeEntityQueryWrapper);
                if (userCoachTimeEntity != null) {
                    BeanUtils.copyProperties(entity, dto);
                    User roomUser = userMapper.selectById(userCoachTimeEntity.getCoachUserId());
                    log.info("getRoomReportList User：{{}}", roomUser);
                    dto.setRoomId(userCoachTimeEntity.getId());
                    dto.setUserId(userCoachTimeEntity.getCoachUserId());
                    // dto.setRoom(roomUser.getNickName());
                    // dto.setType(entity.getType());
                    dto.setRoomMobile(roomUser.getMobile());
                    dto.setRoomUserName(roomUser != null ? roomUser.getNickName() : "");
                    dto.setIsFrozen(roomUser != null ? roomUser.getFrozenFlag() : "1");
                    dto.setIsClose(userCoachTimeEntity.getLiveStatus() == "1" ? "0" : "1");
                    // int count = trainGroupUserMapper.selectCount(new QueryWrapper<TrainGroupUserEntity>().eq("userId", roomEntity.getUserId()).eq("trainGroupId", roomEntity.getTrainGroupId()));
                    //  dto.setIsKickOut(count + "");
                    //  User reportUser = userMapper.selectById(entity.getReportUserId());
                    //  dto.setReportUserName(reportUser != null ? reportUser.getNickName() : "");
                    dtoList.add(dto);
                }
            } else if ("2".equals(vo.getType())) {
                //举报私教
                QueryWrapper<UserCoachTimeEntity> userCoachTimeEntityQueryWrapper = new QueryWrapper<>();
                userCoachTimeEntityQueryWrapper.eq("coachCourseId", entity.getRoomId());
                userCoachTimeEntityQueryWrapper.eq("deleted", "0");
                UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(userCoachTimeEntityQueryWrapper);
                if (userCoachTimeEntity != null) {
                    BeanUtils.copyProperties(entity, dto);
                    User roomUser = userMapper.selectById(userCoachTimeEntity.getCoachUserId());
                    log.info("getRoomReportList User：{{}}", roomUser);
                    dto.setRoomId(userCoachTimeEntity.getCoachCourseId());
                    dto.setUserId(userCoachTimeEntity.getCoachUserId());
                    // dto.setRoom(roomEntity.getRoomName());
                    // dto.setType(entity.getType());
                    dto.setRoomMobile(roomUser.getMobile());
                    dto.setRoomUserName(roomUser != null ? roomUser.getNickName() : "");
                    dto.setIsFrozen(roomUser != null ? roomUser.getFrozenFlag() : "1");
                    dto.setIsClose(userCoachTimeEntity.getLiveStatus() == "1" ? "0" : "1");

                    //   int count = trainGroupUserMapper.selectCount(new QueryWrapper<TrainGroupUserEntity>().eq("userId", roomEntity.getUserId()).eq("trainGroupId", roomEntity.getTrainGroupId()));
                    // dto.setIsKickOut(count + "");
                    //   User reportUser = userMapper.selectById(entity.getReportUserId());
                    //  dto.setReportUserName(reportUser != null ? reportUser.getNickName() : "");
                    dtoList.add(dto);
                }
            }
        }
        log.info("getRoomReportList dtoList：{{}}", dtoList);
        ResponseDto responseDto = ResponseDto.<RoomReportDto>builder()
                .total((int) roomReportEntityPage.getTotal())
                .resList(dtoList).build();
        log.info("------------------------我要获取房间举报列表结束啦--------------------------");
        return ResultUtil.success(responseDto);
    }


    @Override
    public Result getRoomReport(String id) {
        RoomReportEntity roomReportEntity = reportMapper.selectById(id);
        RoomReportDto dto = new RoomReportDto();
        BeanUtils.copyProperties(roomReportEntity, dto);
        if ("0".equals(roomReportEntity.getType())) {
            //举报房间
            RoomEntity roomEntity = roomMapper.selectById(roomReportEntity.getRoomId());
            log.info("getRoomReportList roomEntity：{{}}", roomEntity);
            if (roomEntity != null) {
                BeanUtils.copyProperties(roomReportEntity, dto);
                User roomUser = userMapper.selectById(roomEntity.getUserId());
                log.info("getRoomReportList User：{{}}", roomUser);
                dto.setRoomId(roomEntity.getId());
                dto.setUserId(roomEntity.getUserId());
                dto.setRoom(roomEntity.getRoomName());
                // dto.setType(entity.getType());
                dto.setRoomMobile(roomUser.getMobile());
                dto.setRoomUserName(roomUser != null ? roomUser.getNickName() : "");
                dto.setIsFrozen(roomUser != null ? roomUser.getFrozenFlag() : "1");
                dto.setIsClose(roomEntity.getRoomStatus());
                int count = trainGroupUserMapper.selectCount(new QueryWrapper<TrainGroupUserEntity>().eq("userId", roomEntity.getUserId()).eq("trainGroupId", roomEntity.getTrainGroupId()));
                dto.setIsKickOut(count + "");
                //   User reportUser = userMapper.selectById(entity.getReportUserId());
                //   dto.setReportUserName(reportUser != null ? reportUser.getNickName() : "");
            }
        } else if ("1".equals(roomReportEntity.getType())) {
            //举报直播
            QueryWrapper<UserCoachTimeEntity> userCoachTimeEntityQueryWrapper = new QueryWrapper<>();
            userCoachTimeEntityQueryWrapper.eq("id", roomReportEntity.getRoomId());
            userCoachTimeEntityQueryWrapper.eq("deleted", "0");
            UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(userCoachTimeEntityQueryWrapper);
            if (userCoachTimeEntity != null) {
                BeanUtils.copyProperties(roomReportEntity, dto);
                User roomUser = userMapper.selectById(userCoachTimeEntity.getCoachUserId());
                log.info("getRoomReportList User：{{}}", roomUser);
                dto.setRoomId(userCoachTimeEntity.getId());
                dto.setUserId(userCoachTimeEntity.getCoachUserId());
                // dto.setRoom(roomUser.getNickName());
                // dto.setType(entity.getType());
                dto.setRoomMobile(roomUser.getMobile());
                dto.setRoomUserName(roomUser != null ? roomUser.getNickName() : "");
                dto.setIsFrozen(roomUser != null ? roomUser.getFrozenFlag() : "1");
                dto.setIsClose(userCoachTimeEntity.getLiveStatus() == "1" ? "0" : "1");
                // int count = trainGroupUserMapper.selectCount(new QueryWrapper<TrainGroupUserEntity>().eq("userId", roomEntity.getUserId()).eq("trainGroupId", roomEntity.getTrainGroupId()));
                //  dto.setIsKickOut(count + "");
                //  User reportUser = userMapper.selectById(entity.getReportUserId());
                //  dto.setReportUserName(reportUser != null ? reportUser.getNickName() : "");
            }
        } else if ("2".equals(roomReportEntity.getType())) {
            //举报私教
            QueryWrapper<UserCoachTimeEntity> userCoachTimeEntityQueryWrapper = new QueryWrapper<>();
            userCoachTimeEntityQueryWrapper.eq("coachCourseId", roomReportEntity.getRoomId());
            userCoachTimeEntityQueryWrapper.eq("deleted", "0");
            UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(userCoachTimeEntityQueryWrapper);
            if (userCoachTimeEntity != null) {
                BeanUtils.copyProperties(roomReportEntity, dto);
                User roomUser = userMapper.selectById(userCoachTimeEntity.getCoachUserId());
                log.info("getRoomReportList User：{{}}", roomUser);
                dto.setRoomId(userCoachTimeEntity.getCoachCourseId());
                dto.setUserId(userCoachTimeEntity.getCoachUserId());
                // dto.setRoom(roomEntity.getRoomName());
                // dto.setType(entity.getType());
                dto.setRoomMobile(roomUser.getMobile());
                dto.setRoomUserName(roomUser != null ? roomUser.getNickName() : "");
                dto.setIsFrozen(roomUser != null ? roomUser.getFrozenFlag() : "1");
                dto.setIsClose(userCoachTimeEntity.getLiveStatus() == "1" ? "0" : "1");

                //   int count = trainGroupUserMapper.selectCount(new QueryWrapper<TrainGroupUserEntity>().eq("userId", roomEntity.getUserId()).eq("trainGroupId", roomEntity.getTrainGroupId()));
                // dto.setIsKickOut(count + "");
                //   User reportUser = userMapper.selectById(entity.getReportUserId());
                //  dto.setReportUserName(reportUser != null ? reportUser.getNickName() : "");
            }
        }

        User reportUser = userMapper.selectById(roomReportEntity.getReportUserId());
        dto.setReportUserName(reportUser != null ? reportUser.getNickName() : "");
        dto.setRoomMobile(reportUser != null ? reportUser.getMobile() : "");
        List<ImgDto> imageUrl = new ArrayList<>();
        QueryWrapper<RoomReportAttach> attachQueryWrapper = new QueryWrapper<>();
        attachQueryWrapper.eq("deleted", "0");
        attachQueryWrapper.eq("reportId", roomReportEntity.getId());
        log.info("getRoomReport reportId:{{}}", roomReportEntity.getId());
        List<RoomReportAttach> roomReportAttaches = roomReportAttachMapper.selectList(attachQueryWrapper);
        log.info("getRoomReport roomReportAttaches:{{}}", roomReportAttaches);
        for (RoomReportAttach attach : roomReportAttaches) {
            ImgDto img = iAttachService.getImgDto(attach.getAttachUrlId());
            imageUrl.add(img);
        }
        log.info("getRoomReport imageUrl:{{}}", imageUrl);
        dto.setImageUrl(imageUrl);
        log.info("getRoomReport RoomReportDto:{{}}", dto);
        //成功
        return ResultUtil.success(dto);
    }


    @Override
    public Result operation(ManageRequest vo) {
        log.info("operation userId", vo.getUserId());
        log.info("operation ManageRequest", vo);
        Result result = new Result();
        log.info("operation Id", vo.getId());
        RoomReportEntity entity = reportMapper.selectById(vo.getId());
        log.info("operation RoomReportEntity", entity);
        if (entity == null) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "举报信息不存在");
        }
        log.info("operation RoomId", entity.getRoomId());
        RoomEntity roomEntity = roomMapper.selectById(entity.getRoomId());
        log.info("operation roomEntity", roomEntity);
        if (roomEntity == null) {
            return ResultUtil.error(ResultEnum.FAIL.getCode(), "房间不存在");
        }
        log.info("operation PostType", vo.getPostType());
        if ("1".equals(vo.getPostType())) {
            //冻结用户
            User user = userMapper.selectById(roomEntity.getUserId());
            user.setId(roomEntity.getUserId());
            user.setFrozenFlag("1");
            user.setLastModifiedBy(vo.getUserId());
            user.setLastModifiedDate(LocalDateTime.now());
            log.info("operation user", user);
            userMapper.deleteById(user);
            userMapper.insert(user);
            return ResultUtil.success("该用户被冻结");
        } else if ("2".equals(vo.getPostType())) {
            log.info("operation Id", roomEntity.getId());
            //关闭房间
            result = frontReportFeign.closeRoomForManage(roomEntity.getId());
        } else if ("3".equals(vo.getPostType())) {
            log.info("operation TrainGroupId", roomEntity.getTrainGroupId());
            //踢出小组
            result = frontReportFeign.kickOutGroupForManage(roomEntity.getTrainGroupId());
        }
        if (!"U0001".equals(result.getCode())) {
            return result;
        } else {
            return ResultUtil.success();
        }
    }

    @Override
    public Result delete(VoId vo) {
        RoomReportEntity entity = new RoomReportEntity();
        entity.setId(vo.getId());
        entity.setDeleted("1");
        entity.setLastModifiedBy(vo.getUserId());
        entity.setLastModifiedDate(LocalDateTime.now());
        reportMapper.updateById(entity);
        return ResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result closeRoom(String id) {
        log.info("---------------------------------后台关闭房间--------------------------------------");
        log.info("closeRoom id", id);
        //关闭房间
        Result result = frontReportFeign.closeRoomForManage(id);
        return ResultUtil.success(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result closeUserCoachTime(String timeId) {
        log.info("---------------------------------后台关闭直播课程--------------------------------------");
        log.info("closeUserCoachTime id", timeId);
        //关闭直播课程
        Result result = frontReportFeign.coachTimeCloseRoom(timeId);
        return ResultUtil.success(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result closeUserCoachCourseOrder(String courseId) {
        log.info("---------------------------------后台关闭私教课程--------------------------------------");
        log.info("closeUserCoachCourseOrder id", courseId);
        //关闭私教课程
        Result result = frontReportFeign.userCoachCloseRoom(courseId);
        return ResultUtil.success(result);
    }


}
