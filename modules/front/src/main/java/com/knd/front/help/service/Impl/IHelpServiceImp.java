package com.knd.front.help.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.service.AttachService;
import com.knd.front.dto.VoUrlSort;
import com.knd.front.entity.Attach;
import com.knd.front.help.dto.HelpDto;
import com.knd.front.help.entity.HelpAttachEntity;
import com.knd.front.help.entity.HelpEntity;
import com.knd.front.help.mapper.HelpAttachMapper;
import com.knd.front.help.mapper.HelpMapper;
import com.knd.front.help.request.HelpRequest;
import com.knd.front.help.service.IHelpService;
import com.knd.front.pay.dto.ImgDto;
import com.knd.front.train.mapper.AttachMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class IHelpServiceImp extends ServiceImpl<HelpMapper, HelpEntity> implements IHelpService {

    @Resource
    private AttachService attachService;
    @Resource
    private HelpAttachMapper helpAttachMapper;
    @Resource
    private AttachMapper attachMapper;

    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    //视频文件夹路径
    @Value("${OBS.videoFoldername}")
    private String videoFoldername;
    //視頻路徑
    @Value("${upload.FileVideoPath}")
    private String fileVideoPath;

    //新增
    @Override
    public Result add(HelpRequest helpRequest) {
        log.info("add helpRequest:{{}}", helpRequest);
        HelpEntity helpEntity = new HelpEntity();
        BeanUtils.copyProperties(helpRequest, helpEntity);
        helpEntity.setId(UUIDUtil.getShortUUID());
        List<VoUrlSort> imageUrls = helpRequest.getImageUrls();
        log.info("add imageUrls:{{}}", helpRequest.getImageUrls());
        imageUrls.stream().sorted(Comparator.comparing(VoUrlSort::getSort)).forEach(url -> {
            log.info("add sort:{{}}", url.getSort());
            log.info("add picAttachName:{{}}", url.getPicAttachName());
            log.info("add picAttachNewName:{{}}", url.getPicAttachNewName());
            if (StringUtils.isNotEmpty(url.getPicAttachName())
                    && StringUtils.isNotEmpty(url.getPicAttachNewName())
                    && StringUtils.isNotEmpty(url.getPicAttachSize())) {
                //保存选中图片
                Attach imgAPi = attachService.saveAttach(UserUtils.getUserId(), url.getPicAttachName()
                        , url.getPicAttachNewName(), url.getPicAttachSize());
                HelpAttachEntity helpAttachEntity = new HelpAttachEntity();
                helpAttachEntity.setId(UUIDUtil.getShortUUID());
                helpAttachEntity.setHelpId(helpEntity.getId());
                helpAttachEntity.setAttachUrlId(imgAPi.getId());
                helpAttachEntity.setCreateBy(UserUtils.getUserId());
                helpAttachEntity.setCreateDate(LocalDateTime.now());
                helpAttachEntity.setDeleted("0");
                helpAttachMapper.insert(helpAttachEntity);
            }
        });
        Attach attach = attachService.saveVedioAttach(UserUtils.getUserId(), helpRequest.getVoVideoUrl().getVideoAttachName(), helpRequest.getVoVideoUrl().getVideoAttachNewName(), helpRequest.getVoVideoUrl().getVideoAttachSize());
        helpEntity.setVideoAttachId(attach.getId());
        helpEntity.setVideoAttachName(helpRequest.getVoVideoUrl().getVideoAttachName());
        helpEntity.setCreateBy(UserUtils.getUserId());
        helpEntity.setCreateDate(LocalDateTime.now());
        helpEntity.setDeleted("0");
        baseMapper.insert(helpEntity);
        return ResultUtil.success();
    }

    //修改
    @Override
    public Result edit(HelpRequest helpRequest) {
        HelpEntity helpEntity = new HelpEntity();
        BeanUtils.copyProperties(helpRequest, helpEntity);
        QueryWrapper<HelpAttachEntity> helpAttachEntityQueryWrapper = new QueryWrapper<HelpAttachEntity>();
        helpAttachEntityQueryWrapper.eq("helpId", helpRequest.getId());
        helpAttachEntityQueryWrapper.eq("deleted", "0");
        helpAttachMapper.delete(helpAttachEntityQueryWrapper);
        List<VoUrlSort> imageUrls = helpRequest.getImageUrls();
        log.info("add imageUrls:{{}}", helpRequest.getImageUrls());
        imageUrls.stream().sorted(Comparator.comparing(VoUrlSort::getSort)).forEach(url -> {
            log.info("add sort:{{}}", url.getSort());
            log.info("add picAttachName:{{}}", url.getPicAttachName());
            log.info("add picAttachNewName:{{}}", url.getPicAttachNewName());
            if (StringUtils.isNotEmpty(url.getPicAttachName())
                    && StringUtils.isNotEmpty(url.getPicAttachNewName())
                    && StringUtils.isNotEmpty(url.getPicAttachSize())) {
                //保存选中图片
                Attach imgAPi = attachService.saveAttach(UserUtils.getUserId(), url.getPicAttachName()
                        , url.getPicAttachNewName(), url.getPicAttachSize());
                HelpAttachEntity helpAttachEntity = new HelpAttachEntity();
                helpAttachEntity.setId(UUIDUtil.getShortUUID());
                helpAttachEntity.setHelpId(helpRequest.getId());
                helpAttachEntity.setAttachUrlId(imgAPi.getId());
                helpAttachEntity.setCreateBy(UserUtils.getUserId());
                helpAttachEntity.setCreateDate(LocalDateTime.now());
                helpAttachEntity.setDeleted("0");
                helpAttachMapper.insert(helpAttachEntity);
            }
        });
        if (StringUtils.isNotEmpty(helpRequest.getVoVideoUrl().getVideoAttachNewName())) {
            //检查视频文件是否有更新
            Attach aVi = attachService.getInfoById(helpRequest.getVideoAttachId());
            //视频是否需要更新
            boolean vFlag = false;
            if (aVi != null) {
                String[] strs1 = aVi.getFilePath().split("\\?");
                if (!strs1[0].equals(helpRequest.getVoVideoUrl().getVideoAttachNewName())) {
                    //更新
                    vFlag = true;
                }
                //不需要更新
            } else {
                //更新
                vFlag = true;
            }
            if (vFlag) {
                //更新视频
                //将原文件标识设为删除
                attachService.deleteFile(helpRequest.getVideoAttachId(),UserUtils.getUserId());
                Attach attach = attachService.saveVedioAttach(UserUtils.getUserId(), helpRequest.getVoVideoUrl().getVideoAttachName(), helpRequest.getVoVideoUrl().getVideoAttachNewName(), helpRequest.getVoVideoUrl().getVideoAttachSize());
                //
                helpEntity.setVideoAttachId(attach.getId());
                helpEntity.setVideoAttachName(helpRequest.getVoVideoUrl().getVideoAttachName());
            }
        }else {
            //视频已经被清除
            //清除数据库信息
            helpEntity.setVideoAttachId("");
        }
        helpEntity.setLastModifiedBy(UserUtils.getUserId());
        helpEntity.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(helpEntity);
        return ResultUtil.success(helpEntity);
    }

    //删除a
    @Override
    public Result delete(HelpRequest helpRequest) {
        log.info("delete id:{{}}", helpRequest.getId());
        HelpEntity helpEntity = baseMapper.selectById(helpRequest.getId());
        helpEntity.setId(helpRequest.getId());
        helpEntity.setDeleted("1");
        baseMapper.updateById(helpEntity);
        //将原文件标识设为删除
        attachService.deleteFile(helpEntity.getVideoAttachId(),UserUtils.getUserId());
        //成功
        return ResultUtil.success();
    }


    //获取帮助
    @Override
    public Result getHelp(String id) {
        QueryWrapper<HelpEntity> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", "0");
        HelpEntity helpEntity = baseMapper.selectOne(qw);
        HelpDto helpDto = new HelpDto();
        BeanUtils.copyProperties(helpEntity, helpDto);
        QueryWrapper<HelpAttachEntity> helpEntityQueryWrapper = new QueryWrapper<>();
        helpEntityQueryWrapper.eq("helpId", id);
        helpEntityQueryWrapper.eq("deleted", "0");
        helpEntityQueryWrapper.orderByAsc("length(sort)","sort");
        List<HelpAttachEntity> helpAttachEntities = helpAttachMapper.selectList(helpEntityQueryWrapper);
        ArrayList<ImgDto> imgDtos = new ArrayList<>();
        helpAttachEntities.stream().forEach(helpAttach -> {
            imgDtos.add(attachService.getImgDto(helpAttach.getAttachUrlId()));
        });
        helpDto.setImageUrl(imgDtos);
        //根据id获取视频信息
        if(StringUtils.isNotEmpty(helpEntity.getVideoAttachId())) {
            Attach aVi = attachService.getInfoById(helpEntity.getVideoAttachId());
            if (aVi != null) {
                helpDto.setVideoAttachUrl(fileVideoPath + aVi.getFilePath());
                helpDto.setVideoAttachSize(aVi.getFileSize());
                helpDto.setVideoAttachName(aVi.getFileName());
                String[] strs = (aVi.getFilePath()).split("\\?");
                helpDto.setVideoAttachNewName(videoFoldername + strs[0]);
            }
        }
        return ResultUtil.success(helpDto);
    }

    //获取帮助列表
    @Override
    public Result getHelpList(String title, Integer size, String current) {
        QueryWrapper<HelpEntity> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(title)) {
            qw.like("title", title);
        }
        qw.eq("deleted", "0");
        qw.orderByDesc("createDate");
        if (StringUtils.isEmpty(size)) {
            size = PageInfo.pageSize;
        }
        //分页
        Page<HelpEntity> partPage = new Page<>(Long.parseLong(current), size);
        partPage = baseMapper.selectPage(partPage, qw);
        List<HelpEntity> helpEntityList = partPage.getRecords();
        Page<HelpDto> helpDtoPage = new Page<>(Long.parseLong(current), size);
        List<HelpDto> helpDtoList = new ArrayList<HelpDto>();
        helpEntityList.stream().forEach(helpEntity -> {
            HelpDto helpDto = new HelpDto();
            BeanUtils.copyProperties(helpEntity, helpDto);
            QueryWrapper<HelpAttachEntity> helpEntityQueryWrapper = new QueryWrapper<>();
            helpEntityQueryWrapper.eq("helpId", helpEntity.getId());
            helpEntityQueryWrapper.eq("deleted", "0");
            helpEntityQueryWrapper.orderByAsc("length(sort)","sort");
            List<HelpAttachEntity> helpAttachEntities = helpAttachMapper.selectList(helpEntityQueryWrapper);
            ArrayList<ImgDto> imgDtos = new ArrayList<>();
            helpAttachEntities.stream().forEach(helpAttach -> {
                imgDtos.add(attachService.getImgDto(helpAttach.getAttachUrlId()));
            });
            helpDto.setImageUrl(imgDtos);
            helpDtoList.add(helpDto);
            //根据id获取视频信息
            if(StringUtils.isNotEmpty(helpEntity.getVideoAttachId())) {
                if (StringUtils.isNotEmpty(helpEntity.getVideoAttachId())) {
                    Attach aVi = attachService.getInfoById(helpEntity.getVideoAttachId());
                    if (aVi != null) {
                        helpDto.setVideoAttachUrl(fileVideoPath + aVi.getFilePath());
                        helpDto.setVideoAttachSize(aVi.getFileSize());
                        helpDto.setVideoAttachName(aVi.getFileName());
                        String[] strs = (aVi.getFilePath()).split("\\?");
                        helpDto.setVideoAttachNewName(videoFoldername + strs[0]);
                    }
                }
            }
        });
        helpDtoPage.setRecords(helpDtoList);
        helpDtoPage.setTotal(partPage.getTotal());
        helpDtoPage.setCurrent(partPage.getCurrent());
        //成功
        return ResultUtil.success(helpDtoPage);
    }



}
