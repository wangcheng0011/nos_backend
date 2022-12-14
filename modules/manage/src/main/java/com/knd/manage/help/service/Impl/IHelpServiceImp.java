package com.knd.manage.help.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.basedata.vo.VoUrlSort;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.help.dto.HelpDto;
import com.knd.manage.help.entity.HelpAttachEntity;
import com.knd.manage.help.entity.HelpEntity;
import com.knd.manage.help.mapper.HelpAttachMapper;
import com.knd.manage.help.mapper.HelpMapper;
import com.knd.manage.help.request.HelpRequest;
import com.knd.manage.help.service.IHelpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class IHelpServiceImp extends ServiceImpl<HelpMapper, HelpEntity> implements IHelpService {

    @Resource
    private IAttachService iAttachService;

    @Resource
    private HelpAttachMapper helpAttachMapper;

    @Resource
    private AttachMapper attachMapper;

    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    //视频路径
    @Value("${upload.FileVideoPath}")
    private String fileVideoPath;

    //视频文件夹路径
    @Value("${OBS.videoFoldername}")
    private String videoFoldername;



    //新增
    @Override
    public Result add(HelpRequest helpRequest) {
        log.info("add helpRequest:{{}}", helpRequest);
        HelpEntity helpEntity = new HelpEntity();
        BeanUtils.copyProperties(helpRequest, helpEntity);
        helpEntity.setId(UUIDUtil.getShortUUID());
        List<VoUrlSort> imageUrls = helpRequest.getImageUrls();
        log.info("add imageUrls:{{}}", imageUrls);
        imageUrls.stream().forEach(url -> {
            log.info("add sort:{{}}", url.getSort());
            log.info("add picAttachName:{{}}", url.getPicAttachName());
            log.info("add picAttachNewName:{{}}", url.getPicAttachNewName());
            if (StringUtils.isNotEmpty(url.getPicAttachName())
                    && StringUtils.isNotEmpty(url.getPicAttachNewName())
                    && StringUtils.isNotEmpty(url.getPicAttachSize())) {
                //保存选中图片
                log.info("add sort:{{}}", url.getSort());
                Attach imgAPi = iAttachService.saveAttach(UserUtils.getUserId(), url.getPicAttachName()
                        , url.getPicAttachNewName(), url.getPicAttachSize());
                HelpAttachEntity helpAttachEntity = new HelpAttachEntity();
                helpAttachEntity.setId(UUIDUtil.getShortUUID());
                helpAttachEntity.setHelpId(helpEntity.getId());
                helpAttachEntity.setAttachUrlId(imgAPi.getId());
                helpAttachEntity.setSort(url.getSort());
                helpAttachEntity.setCreateBy(UserUtils.getUserId());
                helpAttachEntity.setCreateDate(LocalDateTime.now());
                helpAttachEntity.setDeleted("0");
                helpAttachMapper.insert(helpAttachEntity);
            }
        });
        Attach attach = iAttachService.saveVedioAttach(UserUtils.getUserId(), helpRequest.getVoVideoUrl().getVideoAttachName(), helpRequest.getVoVideoUrl().getVideoAttachNewName(), helpRequest.getVoVideoUrl().getVideoAttachSize());
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
        log.info("add helpRequest:{{}}", helpRequest);
        HelpEntity helpEntity = new HelpEntity();
        BeanUtils.copyProperties(helpRequest, helpEntity);
        QueryWrapper<HelpAttachEntity> helpAttachEntityQueryWrapper = new QueryWrapper<HelpAttachEntity>();
        helpAttachEntityQueryWrapper.eq("helpId", helpRequest.getId());
        helpAttachEntityQueryWrapper.eq("deleted", "0");
        helpAttachMapper.delete(helpAttachEntityQueryWrapper);
        List<VoUrlSort> imageUrls = helpRequest.getImageUrls();
        log.info("add imageUrls:{{}}", imageUrls);
        imageUrls.stream().forEach(url -> {
            log.info("add sort:{{}}", url.getSort());
            log.info("add picAttachName:{{}}", url.getPicAttachName());
            log.info("add picAttachNewName:{{}}", url.getPicAttachNewName());
            if (StringUtils.isNotEmpty(url.getPicAttachName())
                    && StringUtils.isNotEmpty(url.getPicAttachNewName())
                    && StringUtils.isNotEmpty(url.getPicAttachSize())) {
                //保存选中图片
                log.info("add sort:{{}}", url.getSort());
                Attach imgAPi = iAttachService.saveAttach(UserUtils.getUserId(), url.getPicAttachName()
                        , url.getPicAttachNewName(), url.getPicAttachSize());
                HelpAttachEntity helpAttachEntity = new HelpAttachEntity();
                helpAttachEntity.setId(UUIDUtil.getShortUUID());
                helpAttachEntity.setHelpId(helpRequest.getId());
                helpAttachEntity.setAttachUrlId(imgAPi.getId());
                helpAttachEntity.setSort(url.getSort());
                helpAttachEntity.setCreateBy(UserUtils.getUserId());
                helpAttachEntity.setCreateDate(LocalDateTime.now());
                helpAttachEntity.setDeleted("0");
                helpAttachMapper.insert(helpAttachEntity);
            }
        });

        if (StringUtils.isNotEmpty(helpRequest.getVoVideoUrl().getVideoAttachNewName())) {
            //检查视频文件是否有更新
            Attach aVi = iAttachService.getInfoById(helpRequest.getVideoAttachId());
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
                iAttachService.deleteFile(helpRequest.getVideoAttachId(),UserUtils.getUserId());
                Attach attach = iAttachService.saveVedioAttach(UserUtils.getUserId(), helpRequest.getVoVideoUrl().getVideoAttachName(), helpRequest.getVoVideoUrl().getVideoAttachNewName(), helpRequest.getVoVideoUrl().getVideoAttachSize());
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

    //删除
    @Override
    public Result delete(HelpRequest helpRequest) {
        log.info("delete id:{{}}", helpRequest.getId());
        HelpEntity helpEntity = baseMapper.selectById(helpRequest.getId());
        helpEntity.setId(helpRequest.getId());
        helpEntity.setDeleted("1");
        baseMapper.updateById(helpEntity);
        //将原文件标识设为删除
        iAttachService.deleteFile(helpEntity.getVideoAttachId(),UserUtils.getUserId());
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
            imgDtos.add(iAttachService.getImgDto(helpAttach.getAttachUrlId()));
        });
        //根据id获取视频信息
        if(StringUtils.isNotEmpty(helpEntity.getVideoAttachId())) {
            Attach aVi = iAttachService.getInfoById(helpEntity.getVideoAttachId());
            if (aVi != null) {
                helpDto.setVideoAttachUrl(fileVideoPath + aVi.getFilePath());
                helpDto.setVideoAttachSize(aVi.getFileSize());
                helpDto.setVideoAttachName(aVi.getFileName());
                String[] strs = (aVi.getFilePath()).split("\\?");
                helpDto.setVideoAttachNewName(videoFoldername + strs[0]);
            }
        }
        helpDto.setImageUrl(imgDtos);
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
        log.info("getHelpList helpDtoList:{{}}",helpDtoList);
        helpEntityList.stream().forEach(helpEntity -> {
            HelpDto helpDto = new HelpDto();
            BeanUtils.copyProperties(helpEntity, helpDto);
            QueryWrapper<HelpAttachEntity> helpEntityQueryWrapper = new QueryWrapper<>();
            helpEntityQueryWrapper.eq("helpId", helpEntity.getId());
            helpEntityQueryWrapper.eq("deleted", "0");
            helpEntityQueryWrapper.orderByAsc("length(sort)","sort");
            List<HelpAttachEntity> helpAttachEntities = helpAttachMapper.selectList(helpEntityQueryWrapper);
            ArrayList<ImgDto> imgDtos = new ArrayList<>();
            log.info("getHelpList helpAttachEntities:{{}}",helpAttachEntities);
            helpAttachEntities.stream().forEach(helpAttach -> {
                log.info("getHelpList sort:{{}}",helpAttach.getSort());
                imgDtos.add(iAttachService.getImgDto(helpAttach.getAttachUrlId()));
            });
            helpDto.setImageUrl(imgDtos);
            helpDtoList.add(helpDto);
            //根据id获取视频信息
            if(StringUtils.isNotEmpty(helpEntity.getVideoAttachId())){
                Attach aVi = iAttachService.getInfoById(helpEntity.getVideoAttachId());
                if (aVi != null) {
                    helpDto.setVideoAttachUrl(fileVideoPath + aVi.getFilePath());
                    helpDto.setVideoAttachSize(aVi.getFileSize());
                    helpDto.setVideoAttachName(aVi.getFileName());
                    String[] strs = (aVi.getFilePath()).split("\\?");
                    helpDto.setVideoAttachNewName(videoFoldername + strs[0]);
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
