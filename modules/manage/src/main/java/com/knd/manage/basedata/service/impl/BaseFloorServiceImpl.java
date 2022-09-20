package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.*;
import com.knd.manage.basedata.entity.BaseElement;
import com.knd.manage.basedata.entity.BaseFloor;
import com.knd.manage.basedata.entity.BasePageFloor;
import com.knd.manage.basedata.mapper.BaseElementMapper;
import com.knd.manage.basedata.mapper.BaseFloorMapper;
import com.knd.manage.basedata.mapper.BasePageFloorMapper;
import com.knd.manage.basedata.service.IbaseFloorService;
import com.knd.manage.basedata.vo.VoGetFloorList;
import com.knd.manage.basedata.vo.VoSaveElement;
import com.knd.manage.basedata.vo.VoSaveFloor;
import com.knd.manage.basedata.vo.VoUrl;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.mall.service.IGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BaseFloorServiceImpl extends ServiceImpl<BaseFloorMapper, BaseFloor> implements IbaseFloorService {

    private final BasePageFloorMapper basePageFloorMapper;
    private final BaseFloorMapper baseFloorMapper;
    private final BaseElementMapper baseElementMapper;
    private final IGoodsService goodsService;
    private final IAttachService iAttachService;
    private final AttachMapper attachMapper;
    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    @Override
    public Result getFloorList(VoGetFloorList vo) {
        //分页
        Page<BaseFloorDto> partPage = new Page<>(Integer.parseInt(vo.getCurrent()), PageInfo.pageSize);
        QueryWrapper<BaseFloorDto> wrapper = Wrappers.query();
        wrapper.eq("a.deleted","0");
        if (StringUtils.isNotEmpty(vo.getKeyValue())) {
            wrapper.like("a.keyValue", vo.getKeyValue());
        }
        if(StringUtils.isNotEmpty(vo.getFloorType())){
            wrapper.eq("a.floorType", vo.getFloorType());
        }
//        wrapper.orderByAsc("a.sort");

        List<BaseFloorDto> list = baseFloorMapper.queryFloorList(partPage, wrapper);
        list.stream().forEach(item -> {
            item.setShowUrl(fileImagesPath+item.getShowUrl());
        });
        ResponseDto dto = ResponseDto.<BaseFloorDto>builder().total((int) partPage.getTotal()).resList(list).build();
        //成功
        return ResultUtil.success(dto);
    }

    @Override
    public Result getFloor(String id) {
        FloorDetailDto dto = new FloorDetailDto();
        QueryWrapper<BaseFloor> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("deleted","0");
        BaseFloor basePage = baseMapper.selectOne(wrapper);

        BeanUtils.copyProperties(basePage,dto);

        ImgDto selectImgDto = getImgDto(basePage.getImageUrlId());
        dto.setImageUrl(selectImgDto);

        ImgDto unSelectImgDto = getImgDto(basePage.getBackgroundUrlId());
        dto.setBackgroundUrl(unSelectImgDto);

        List<BaseElementDto> elementList = baseFloorMapper.getElementList(id);
        for(BaseElementDto elementDto : elementList){
            elementDto.setShowUrl(fileImagesPath+elementDto.getShowUrl());
        }
        dto.setElementList(elementList);

        ImgDto showUrlDto = getImgDto(basePage.getShowUrlId());
        dto.setShowUrl(showUrlDto);
        //成功
        return ResultUtil.success(dto);
    }

    @Override
    public Result deleteFloor(String userId, String id) {
        BaseFloor ba = new BaseFloor();
        ba.setId(id);
        ba.setDeleted("1");
        ba.setLastModifiedBy(userId);
        ba.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(ba);

        basePageFloorMapper.delete(new QueryWrapper<BasePageFloor>().eq("deleted",0).eq("floorId",id));
        return ResultUtil.success();
    }

    @Override
    public Result addFloor(String userId, VoSaveFloor vo) {
        BaseFloor bp = new BaseFloor();
        BeanUtils.copyProperties(vo,bp);

        bp.setId(UUIDUtil.getShortUUID());
        bp.setCreateBy(userId);
        bp.setCreateDate(LocalDateTime.now());
        bp.setDeleted("0");
        bp.setLastModifiedBy(userId);
        bp.setLastModifiedDate(LocalDateTime.now());

        VoUrl imgUrl = vo.getImageUrl();
        VoUrl backgroundUrl = vo.getBackgroundUrl();
        VoUrl showUrl = vo.getShowUrl();
        if(imgUrl != null && imgUrl.getPicAttachSize() !=null){
            //保存选中图片
            Attach imgAttach = goodsService.saveAttach(userId, imgUrl.getPicAttachName(), imgUrl.getPicAttachNewName(), imgUrl.getPicAttachSize());
            bp.setImageUrlId(imgAttach.getId());
        }
        if(backgroundUrl != null && backgroundUrl.getPicAttachSize() !=null){
            Attach backgroundAttach = goodsService.saveAttach(userId, backgroundUrl.getPicAttachName(), backgroundUrl.getPicAttachNewName(), backgroundUrl.getPicAttachSize());
            bp.setBackgroundUrlId(backgroundAttach.getId());
        }
        if(showUrl != null && showUrl.getPicAttachSize() !=null){
            Attach showAttach = goodsService.saveAttach(userId, showUrl.getPicAttachName(), showUrl.getPicAttachNewName(), showUrl.getPicAttachSize());
            bp.setShowUrlId(showAttach.getId());
        }
        baseMapper.insert(bp);
        return ResultUtil.success();
    }

    @Override
    public Result editFloor(String userId, VoSaveFloor vo) {
        //根据id获取名称
        QueryWrapper<BaseFloor> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getFloorId());
        wrapper.eq("deleted","0");
        BaseFloor ho = baseMapper.selectOne(wrapper);
        if (ho == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //搜集关联附件id,更新前清理
        List<String> attachIds = new ArrayList<>();
        attachIds.add(ho.getImageUrlId());
        attachIds.add(ho.getBackgroundUrlId());
        attachIds.add(ho.getShowUrlId());
        attachMapper.deleteBatchIds(attachIds);

        BaseFloor b = new BaseFloor();
        BeanUtils.copyProperties(vo,b);
        b.setId(vo.getFloorId());
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());

        VoUrl imgUrl = vo.getImageUrl();
        VoUrl backgroundUrl = vo.getBackgroundUrl();
        VoUrl showUrl = vo.getShowUrl();
        if(imgUrl != null && imgUrl.getPicAttachSize()!=null){
            //保存选中图片
            Attach imgAttach = goodsService.saveAttach(userId, imgUrl.getPicAttachName(), imgUrl.getPicAttachNewName(), imgUrl.getPicAttachSize());
            b.setImageUrlId(imgAttach.getId());
        }
        if(backgroundUrl != null && backgroundUrl.getPicAttachSize()!=null){
            Attach backgroundAttach = goodsService.saveAttach(userId, backgroundUrl.getPicAttachName(), backgroundUrl.getPicAttachNewName(), backgroundUrl.getPicAttachSize());
            b.setBackgroundUrlId(backgroundAttach.getId());
        }
        if(showUrl != null && showUrl.getPicAttachSize()!=null){
            Attach showAttach = goodsService.saveAttach(userId, showUrl.getPicAttachName(), showUrl.getPicAttachNewName(), showUrl.getPicAttachSize());
            b.setShowUrlId(showAttach.getId());
        }
        baseMapper.updateById(b);
        return ResultUtil.success();
    }

    @Override
    public BaseFloor insertReturnEntity(BaseFloor entity) {
        return null;
    }

    @Override
    public BaseFloor updateReturnEntity(BaseFloor entity) {
        return null;
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
