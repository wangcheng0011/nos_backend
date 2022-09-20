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
import com.knd.manage.basedata.vo.VoUrl;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.help.dto.HelpDto;
import com.knd.manage.help.entity.HelpAttachEntity;
import com.knd.manage.help.entity.HelpEntity;
import com.knd.manage.help.mapper.HelpAttachMapper;
import com.knd.manage.help.mapper.HelpMapper;
import com.knd.manage.help.request.HelpRequest;
import com.knd.manage.help.service.IHelpService;
import com.knd.manage.mall.service.IGoodsService;
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
    private IGoodsService goodsService;

    @Resource
    private IAttachService iAttachService;

    @Resource
    private HelpAttachMapper helpAttachMapper;

    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    //新增
    @Override
    public Result add(HelpRequest helpRequest) {
        log.info("add helpRequest:{{}}", helpRequest);
        HelpEntity helpEntity = new HelpEntity();
        BeanUtils.copyProperties(helpRequest, helpEntity);
        helpEntity.setId(UUIDUtil.getShortUUID());
        List<VoUrl> imageUrls = helpRequest.getImageUrls();
        imageUrls.stream().forEach(url -> {
            if (StringUtils.isNotEmpty(url.getPicAttachName())
                    && StringUtils.isNotEmpty(url.getPicAttachNewName())
                    && StringUtils.isNotEmpty(url.getPicAttachSize())) {
                //保存选中图片
                Attach imgAPi = goodsService.saveAttach(UserUtils.getUserId(), url.getPicAttachName()
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
        List<VoUrl> imageUrls = helpRequest.getImageUrls();
        imageUrls.stream().forEach(url -> {
            if (StringUtils.isNotEmpty(url.getPicAttachName())
                    && StringUtils.isNotEmpty(url.getPicAttachNewName())
                    && StringUtils.isNotEmpty(url.getPicAttachSize())) {
                //保存选中图片
                Attach imgAPi = goodsService.saveAttach(UserUtils.getUserId(), url.getPicAttachName()
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
        helpEntity.setLastModifiedBy(UserUtils.getUserId());
        helpEntity.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(helpEntity);
        return ResultUtil.success(helpEntity);
    }

    //删除
    @Override
    public Result delete(HelpRequest helpRequest) {
        log.info("delete id:{{}}", helpRequest.getId());
        HelpEntity helpEntity = new HelpEntity();
        helpEntity.setId(helpRequest.getId());
        helpEntity.setDeleted("1");
        baseMapper.updateById(helpEntity);
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
        helpEntityQueryWrapper.orderByAsc("createDate");
        List<HelpAttachEntity> helpAttachEntities = helpAttachMapper.selectList(helpEntityQueryWrapper);
        ArrayList<ImgDto> imgDtos = new ArrayList<>();
        helpAttachEntities.stream().forEach(helpAttach -> {
            imgDtos.add(getImgDto(helpAttach.getAttachUrlId()));
        });
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
        qw.orderByAsc("createDate");
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
            helpEntityQueryWrapper.orderByDesc("createDate");
            List<HelpAttachEntity> helpAttachEntities = helpAttachMapper.selectList(helpEntityQueryWrapper);
            ArrayList<ImgDto> imgDtos = new ArrayList<>();
            helpAttachEntities.stream().forEach(helpAttach -> {
                imgDtos.add(getImgDto(helpAttach.getAttachUrlId()));
            });
            helpDto.setImageUrl(imgDtos);
            helpDtoList.add(helpDto);
        });
        helpDtoPage.setRecords(helpDtoList);
        helpDtoPage.setTotal(partPage.getTotal());
        helpDtoPage.setCurrent(partPage.getCurrent());
        //成功
        return ResultUtil.success(helpDtoPage);
    }

    public ImgDto getImgDto(String urlId) {
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