package com.knd.manage.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.basedata.entity.UserCoachEntity;
import com.knd.manage.basedata.mapper.UserCoachMapper;
import com.knd.manage.basedata.vo.VoUrl;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.course.dto.CoachDto;
import com.knd.manage.course.dto.CoachListDto;
import com.knd.manage.course.entity.CoachList;
import com.knd.manage.course.entity.UserCoachAttach;
import com.knd.manage.course.mapper.UserCoachAttachMapper;
import com.knd.manage.course.service.CoachService;
import com.knd.manage.course.vo.VoGetCoachList;
import com.knd.manage.course.vo.VoSaveCoach;
import com.knd.manage.mall.service.IGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zm
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class CoachServiceImpl implements CoachService {
    private final UserCoachMapper userCoachMapper;
    private final IAttachService attachService;
    private final IGoodsService goodsService;
    private final UserCoachAttachMapper userCoachAttachMapper;
    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    @Override
    public Result getCoachList(VoGetCoachList vo) {
        Page<CoachList> page = new Page<>(Long.parseLong(vo.getCurrent()), PageInfo.pageSize);
        QueryWrapper<UserCoachEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("a.deleted","0");
        if(StringUtils.isNotEmpty(vo.getName())){
            wrapper.like("b.nickName",vo.getName());
        }
        wrapper.orderByDesc("a.createDate");
        List<CoachList> coachList = userCoachMapper.getCoachList(page, wrapper);
        List<CoachListDto> coachListDtos = new ArrayList<CoachListDto>();
        coachList.stream().forEach(i->{
            CoachListDto dto = new CoachListDto();
            BeanUtils.copyProperties(i,dto);
            QueryWrapper<UserCoachAttach> userCoachAttachQueryWrapper = new QueryWrapper<>();
            userCoachAttachQueryWrapper.eq("coachUserId",i.getUserId());
            userCoachAttachQueryWrapper.eq("deleted","0");
            List<UserCoachAttach> userCoachAttaches = userCoachAttachMapper.selectList(userCoachAttachQueryWrapper);
            ArrayList<ImgDto> imgDtos = new ArrayList<>();
            userCoachAttaches.stream().forEach(userCoachAttach -> {
                imgDtos.add(getImgDto(userCoachAttach.getAttachUrlId()));
            });
            dto.setImageUrl(imgDtos);
            coachListDtos.add(dto);
        });
        ResponseDto dto = ResponseDto.<CoachListDto>builder().total((int) page.getTotal()).resList(coachListDtos).build();
        return ResultUtil.success(dto);
    }

    @Override
    public Result getCoach(String id) {
        CoachDto dto = new CoachDto();
        UserCoachEntity userCoachEntity = userCoachMapper.selectById(id);
        if (StringUtils.isNotEmpty(userCoachEntity)){
            BeanUtils.copyProperties(userCoachEntity,dto);
            QueryWrapper<UserCoachAttach> userCoachAttachQueryWrapper = new QueryWrapper<>();
            userCoachAttachQueryWrapper.eq("coachUserId",userCoachEntity.getUserId());
            userCoachAttachQueryWrapper.eq("deleted","0");
            List<UserCoachAttach> userCoachAttaches = userCoachAttachMapper.selectList(userCoachAttachQueryWrapper);
            ArrayList<ImgDto> imgDtos = new ArrayList<>();
            userCoachAttaches.stream().forEach(userCoachAttach -> {
                imgDtos.add(getImgDto(userCoachAttach.getAttachUrlId()));
            });
            dto.setImageUrl(imgDtos);
        }
        return ResultUtil.success(dto);
    }

    @Override
    public Result edit(VoSaveCoach vo) {
        UserCoachEntity userCoachEntity = userCoachMapper.selectById(vo.getId());
        if (StringUtils.isNotEmpty(userCoachEntity)){
            userCoachEntity.setDepict(vo.getDepict());
            userCoachEntity.setContent(vo.getContent());
            userCoachEntity.setSynopsis(vo.getSynopsis());
            QueryWrapper<UserCoachAttach> userCoachAttachQueryWrapper = new QueryWrapper<>();
            userCoachAttachQueryWrapper.eq("coachUserId", userCoachEntity.getUserId());
            userCoachAttachQueryWrapper.eq("deleted","0");
            userCoachAttachMapper.delete(userCoachAttachQueryWrapper);
            List<VoUrl> imageUrls = vo.getImageUrl();
            imageUrls.stream().forEach(url->{
                if(StringUtils.isNotEmpty(url.getPicAttachName())
                        && StringUtils.isNotEmpty(url.getPicAttachNewName())
                        && StringUtils.isNotEmpty(url.getPicAttachSize())){
                //保存选中图片
                Attach imgAPi = goodsService.saveAttach(UserUtils.getUserId(), url.getPicAttachName()
                        , url.getPicAttachNewName(), url.getPicAttachSize());
                UserCoachAttach userCoachAttach = new UserCoachAttach();
                userCoachAttach.setId(UUIDUtil.getShortUUID());
                userCoachAttach.setCoachUserId(userCoachEntity.getUserId());
                userCoachAttach.setAttachUrlId(imgAPi.getId());
                userCoachAttach.setLastModifiedDate(LocalDateTime.now());
                userCoachAttach.setLastModifiedBy(UserUtils.getUserId());
                userCoachAttach.setDeleted("0");
                userCoachAttachMapper.insert(userCoachAttach);
                }
            });
            userCoachEntity.setLastModifiedBy(vo.getUserId());
            userCoachEntity.setLastModifiedDate(LocalDateTime.now());
            userCoachMapper.updateById(userCoachEntity);
         }

        return ResultUtil.success();
    }

    public ImgDto getImgDto(String urlId){
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
