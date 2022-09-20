package com.knd.manage.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.CoachCourseTypeEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.basedata.entity.UserCoachEntity;
import com.knd.manage.basedata.mapper.UserCoachMapper;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.course.dto.CoachCourseDto;
import com.knd.manage.course.dto.CoachCourseListDto;
import com.knd.manage.course.entity.UserCoachCourseEntity;
import com.knd.manage.course.entity.UserCoachCourseOrderEntity;
import com.knd.manage.course.mapper.UserCoachCourseMapper;
import com.knd.manage.course.mapper.UserCoachCourseOrderMapper;
import com.knd.manage.course.service.CoachCourseService;
import com.knd.manage.course.vo.VoGetCoachCourseList;
import com.knd.manage.course.vo.VoSaveCoachCourse;
import com.knd.manage.mall.entity.UserCoachTimeEntity;
import com.knd.manage.mall.mapper.UserCoachTimeMapper;
import com.knd.manage.mall.service.IGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zm
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CoachCourseServiceImpl implements CoachCourseService {
    private final UserCoachMapper userCoachMapper;
    private final UserCoachCourseMapper userCoachCourseMapper;
    private final UserCoachTimeMapper userCoachTimeMapper;
    private final UserCoachCourseOrderMapper userCoachCourseOrderMapper;
    private final AttachMapper attachMapper;
    private final IAttachService iAttachService;
    private final IGoodsService goodsService;
    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    @Override
    public Result getCoachCourseList(VoGetCoachCourseList vo) {
        List<CoachCourseListDto> dtoList = new ArrayList<>();
        Page<UserCoachTimeEntity> page = new Page<>(Long.parseLong(vo.getCurrent()), PageInfo.pageSize);
        QueryWrapper<UserCoachTimeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("coachUserId",vo.getCoachUserId());
        if (StringUtils.isNotEmpty(vo.getDate())){
            wrapper.eq("date",vo.getDate());
        }
        wrapper.orderByDesc("beginTime");
        Page<UserCoachTimeEntity> userCoachTimeEntityPage = userCoachTimeMapper.selectPage(page, wrapper);
        List<UserCoachTimeEntity> records = userCoachTimeEntityPage.getRecords();
        for (UserCoachTimeEntity entity : records){
            CoachCourseListDto dto = new CoachCourseListDto();
            UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(entity.getCoachCourseId());
            if (StringUtils.isNotEmpty(userCoachCourseEntity)){
                dto.setId(userCoachCourseEntity.getId());
                dto.setCourseType(CoachCourseTypeEnum.values()[Integer.valueOf(userCoachCourseEntity.getCourseType())].getDisplay());
                dto.setCourseName(userCoachCourseEntity.getCourseName());
                dto.setCourseTime(userCoachCourseEntity.getCourseTime());
                dto.setConsume(userCoachCourseEntity.getConsume());
                dto.setBeginTime(entity.getBeginTime());
                dto.setEndTime(entity.getEndTime());
                dto.setPrice(entity.getPrice());
                int orderNum = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>().eq("deleted", "0").eq("coachTimeId", entity.getId()));
                dto.setOrderNum(orderNum+"");
            }
            dtoList.add(dto);
        }
        ResponseDto responseDto = ResponseDto.<CoachCourseListDto>builder().total((int) page.getTotal()).resList(dtoList).build();
        return ResultUtil.success(responseDto);
    }

    @Override
    public Result getCoachCourse(String id) {
        CoachCourseDto dto = new CoachCourseDto();
        UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(id);
        if (StringUtils.isNotEmpty(userCoachCourseEntity)){
            BeanUtils.copyProperties(userCoachCourseEntity,dto);
            UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(new QueryWrapper<UserCoachTimeEntity>().eq("coachCourseId", userCoachCourseEntity.getId()).eq("deleted", "0"));
            BeanUtils.copyProperties(userCoachTimeEntity,dto);
            dto.setId(userCoachCourseEntity.getId());
            ImgDto imgUrlDto = getImgDto(userCoachCourseEntity.getPicAttachId());
            dto.setPicAttach(imgUrlDto);
        }
        return ResultUtil.success(dto);
    }

    @Override
    public Result add(VoSaveCoachCourse vo) {
        UserCoachEntity userCoachEntity = userCoachMapper.selectById(vo.getCoachId());
        if (StringUtils.isEmpty(userCoachEntity)){
            return ResultUtil.error("U0999", "教练不存在");
        }
        UserCoachCourseEntity userCoachCourseEntity = new UserCoachCourseEntity();
        UserCoachTimeEntity userCoachTimeEntity = new UserCoachTimeEntity();
        //教练用户id
        String coachUserId = userCoachEntity.getUserId();
        LocalDateTime beginTime = vo.getBeginTime();
        if(StringUtils.isNotEmpty(vo.getEndTime())){
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
            userCoachTimeEntity.setEndTime(endTime);
        }


        Attach picAttachUrl = goodsService.saveAttach(vo.getUserId(), vo.getPicAttachUrl().getPicAttachName()
                , vo.getPicAttachUrl().getPicAttachNewName(), vo.getPicAttachUrl().getPicAttachSize());


        BeanUtils.copyProperties(vo,userCoachCourseEntity);
        userCoachCourseEntity.setId(UUIDUtil.getShortUUID());
        userCoachCourseEntity.setPicAttachId(picAttachUrl.getId());
        userCoachCourseEntity.setCreateBy(vo.getUserId());
        userCoachCourseEntity.setCreateDate(LocalDateTime.now());
        userCoachCourseEntity.setDeleted("0");
        userCoachCourseEntity.setLastModifiedBy(vo.getUserId());
        userCoachCourseEntity.setLastModifiedDate(LocalDateTime.now());
        userCoachCourseMapper.insert(userCoachCourseEntity);


        userCoachTimeEntity.setId(UUIDUtil.getShortUUID());
        userCoachTimeEntity.setDate(LocalDate.parse(beginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        userCoachTimeEntity.setCoachCourseId(userCoachCourseEntity.getId());
        userCoachTimeEntity.setCoachUserId(coachUserId);
        userCoachTimeEntity.setLiveStatus("0");
        userCoachTimeEntity.setBeginTime(beginTime);
        userCoachTimeEntity.setPrice(vo.getPrice());
        userCoachTimeEntity.setCreateBy(vo.getUserId());
        userCoachTimeEntity.setCreateDate(LocalDateTime.now());
        userCoachTimeEntity.setDeleted("0");
        userCoachTimeEntity.setLastModifiedBy(vo.getUserId());
        userCoachTimeEntity.setLastModifiedDate(LocalDateTime.now());
        userCoachTimeMapper.insert(userCoachTimeEntity);

        return ResultUtil.success();
    }

    @Override
    public Result edit(VoSaveCoachCourse vo) {
        UserCoachEntity userCoachEntity = userCoachMapper.selectById(vo.getCoachId());
        if (StringUtils.isEmpty(userCoachEntity)){
            return ResultUtil.error("U0999", "教练不存在");
        }
        //教练用户id
        String coachUserId = userCoachEntity.getUserId();
        LocalDateTime beginTime = vo.getBeginTime();

        if(StringUtils.isNotEmpty(vo.getEndTime())){
            LocalDateTime endTime= vo.getEndTime();
            //验证时间是否重叠
            QueryWrapper<UserCoachTimeEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted","0");
            wrapper.eq("coachUserId",coachUserId);
            wrapper.and(i ->i.lt("beginTime",beginTime).gt("endTime",beginTime));
            wrapper.ne("coachCourseId",vo.getCourseId());
            int num1 = userCoachTimeMapper.selectCount(wrapper);
            wrapper.clear();
            wrapper.eq("deleted","0");
            wrapper.eq("coachUserId",coachUserId);
            wrapper.and(j ->j.lt("beginTime",endTime).gt("endTime",endTime));
            wrapper.ne("coachCourseId",vo.getCourseId());
            int num2 = userCoachTimeMapper.selectCount(wrapper);
            wrapper.clear();
            wrapper.eq("deleted","0");
            wrapper.eq("coachUserId",coachUserId);
            wrapper.and(k->k.ge("beginTime",beginTime).le("endTime",endTime));
            wrapper.ne("coachCourseId",vo.getCourseId());
            int num3 = userCoachTimeMapper.selectCount(wrapper);
            if (num1 >0 || num2>0 || num3>0){
                return ResultUtil.error("U0999", "时间存在重叠");
            }
        }

        UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(vo.getCourseId());
        if (StringUtils.isNotEmpty(userCoachCourseEntity)){
            UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(new QueryWrapper<UserCoachTimeEntity>().eq("coachCourseId", userCoachCourseEntity.getId()).eq("deleted", "0"));
            if (StringUtils.isNotEmpty(userCoachTimeEntity)){
                List<String> orderStatus = new ArrayList<>();
                orderStatus.add("0");
                orderStatus.add("1");
                int orderNum = userCoachCourseOrderMapper.selectCount(new QueryWrapper<UserCoachCourseOrderEntity>().eq("deleted", "0").eq("coachTimeId", userCoachTimeEntity.getId()).in("isOrder", orderStatus));
                if (orderNum>0){
                    return ResultUtil.error("U0999", "该课程已被预约,不能修改");
                }

                if(StringUtils.isNotEmpty(userCoachCourseEntity.getPicAttachId())){
                    attachMapper.deleteById(userCoachCourseEntity.getPicAttachId());
                }

                //保存选中图片
                Attach picAttachUrl = goodsService.saveAttach(vo.getUserId(), vo.getPicAttachUrl().getPicAttachName()
                        , vo.getPicAttachUrl().getPicAttachNewName(), vo.getPicAttachUrl().getPicAttachSize());

                BeanUtils.copyProperties(vo,userCoachCourseEntity);
                if(StringUtils.isNotEmpty(vo.getEndTime())){
                    Duration duration = Duration.between(beginTime,vo.getEndTime());
                    userCoachCourseEntity.setCourseTime(duration.toMinutes()+"");
                }
                userCoachCourseEntity.setPicAttachId(picAttachUrl.getId());
                userCoachCourseEntity.setLastModifiedBy(vo.getUserId());
                userCoachCourseEntity.setLastModifiedDate(LocalDateTime.now());
                userCoachCourseMapper.updateById(userCoachCourseEntity);

                userCoachTimeEntity.setDate(LocalDate.parse(beginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
                userCoachTimeEntity.setBeginTime(beginTime);
                userCoachTimeEntity.setEndTime(vo.getEndTime());
                userCoachTimeEntity.setPrice(vo.getPrice());
                userCoachTimeEntity.setLastModifiedBy(vo.getUserId());
                userCoachTimeEntity.setLastModifiedDate(LocalDateTime.now());
                userCoachTimeMapper.updateById(userCoachTimeEntity);
            }
        }
        return ResultUtil.success();
    }

    @Override
    public Result delete(String userId, String id) {
        UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(id);
        if (StringUtils.isNotEmpty(userCoachCourseEntity)){
            userCoachCourseEntity.setDeleted("1");
            userCoachCourseEntity.setLastModifiedBy(userId);
            userCoachCourseEntity.setLastModifiedDate(LocalDateTime.now());
            userCoachCourseMapper.updateById(userCoachCourseEntity);
            UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(new QueryWrapper<UserCoachTimeEntity>().eq("coachCourseId", userCoachCourseEntity.getId()).eq("deleted", "0"));
            if (StringUtils.isNotEmpty(userCoachTimeEntity)){
                userCoachTimeEntity.setDeleted("1");
                userCoachTimeEntity.setLastModifiedBy(userId);
                userCoachTimeEntity.setLastModifiedDate(LocalDateTime.now());
                userCoachTimeMapper.updateById(userCoachTimeEntity);
            }
        }
        return ResultUtil.success();
    }

    public ImgDto getImgDto(String urlId){
        //根据id获取图片信息
        Attach aPi = iAttachService.getInfoById(urlId);
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
