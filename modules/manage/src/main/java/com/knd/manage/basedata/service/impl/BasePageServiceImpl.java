package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.*;
import com.knd.manage.basedata.entity.BasePage;
import com.knd.manage.basedata.entity.BasePageFloor;
import com.knd.manage.basedata.mapper.BasePageFloorMapper;
import com.knd.manage.basedata.mapper.BasePageMapper;
import com.knd.manage.basedata.service.IbasePageService;
import com.knd.manage.basedata.vo.VoGetPageList;
import com.knd.manage.basedata.vo.VoSavePage;
import com.knd.manage.basedata.vo.VoSort;
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
public class BasePageServiceImpl extends ServiceImpl<BasePageMapper, BasePage> implements IbasePageService {

    private final BasePageMapper basePageMapper;
    private final BasePageFloorMapper basePageFloorMapper;
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
    public Result getPageList(VoGetPageList vo) {
        //分页
        Page<BasePageDto> partPage = new Page<>(Integer.parseInt(vo.getCurrent()), PageInfo.pageSize);
        QueryWrapper<BasePageDto> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(vo.getKeyValue())) {
            qw.like("a.keyValue", vo.getKeyValue());
        }
        qw.eq("a.deleted", "0");

        List<BasePageDto> list = basePageMapper.queryPageList(partPage, qw);
        list.stream().forEach(item -> {
            item.setShowUrl(fileImagesPath+item.getShowUrl());
        });

        ResponseDto pageListDto = ResponseDto.<BasePageDto>builder().total((int) partPage.getTotal()).resList(list).build();
        //成功
        return ResultUtil.success(pageListDto);
    }

    @Override
    public Result getPage(String id) {
        PageDetailDto dto = new PageDetailDto();
        QueryWrapper<BasePage> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("deleted","0");
        BasePage basePage = baseMapper.selectOne(wrapper);

        BeanUtils.copyProperties(basePage,dto);

        ImgDto selectImgDto = getImgDto(basePage.getImageUrlId());
        dto.setImageUrl(selectImgDto);

        ImgDto unSelectImgDto = getImgDto(basePage.getBackgroundUrlId());
        dto.setBackgroundUrl(unSelectImgDto);

        List<BaseFloorSortDto> floorList = basePageFloorMapper.getFloorList(id);
        for(BaseFloorSortDto floor : floorList){
            floor.setShowUrl(fileImagesPath+floor.getShowUrl());
        }
        dto.setFloorList(floorList);

        ImgDto showUrlDto = getImgDto(basePage.getShowUrlId());
        dto.setShowUrl(showUrlDto);

        //成功
        return ResultUtil.success(dto);
    }

    @Override
    public Result deletePage(String userId, String id) {
        BasePage ba = new BasePage();
        ba.setId(id);
        ba.setDeleted("1");
        ba.setLastModifiedBy(userId);
        ba.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(ba);
        return ResultUtil.success();
    }

    @Override
    public Result addPage(VoSavePage vo) {
        QueryWrapper<BasePage> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("keyValue",vo.getKeyValue());
        wrapper.eq("version",vo.getVersion());
        //获取总数
        int s = baseMapper.selectCount(wrapper);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }

        BasePage bp = new BasePage();
        BeanUtils.copyProperties(vo,bp);

        bp.setId(UUIDUtil.getShortUUID());
        bp.setCreateBy(vo.getUserId());
        bp.setCreateDate(LocalDateTime.now());
        bp.setDeleted("0");
        bp.setLastModifiedBy(vo.getUserId());
        bp.setLastModifiedDate(LocalDateTime.now());

        VoUrl imgUrl = vo.getImageUrl();
        VoUrl backgroundUrl = vo.getBackgroundUrl();
        VoUrl showUrl = vo.getShowUrl();
        if(imgUrl != null && imgUrl.getPicAttachSize()!=null){
            //保存选中图片
            Attach imgAttach = goodsService.saveAttach(vo.getUserId(), imgUrl.getPicAttachName(), imgUrl.getPicAttachNewName(), imgUrl.getPicAttachSize());
            bp.setImageUrlId(imgAttach.getId());
        }
        if(backgroundUrl != null && backgroundUrl.getPicAttachSize()!=null){
            Attach backgroundAttach = goodsService.saveAttach(vo.getUserId(), backgroundUrl.getPicAttachName(), backgroundUrl.getPicAttachNewName(), backgroundUrl.getPicAttachSize());
            bp.setBackgroundUrlId(backgroundAttach.getId());
        }
        if(showUrl != null && showUrl!=null){
            Attach showAttach = goodsService.saveAttach(vo.getUserId(), showUrl.getPicAttachName(), showUrl.getPicAttachNewName(), showUrl.getPicAttachSize());
            bp.setShowUrlId(showAttach.getId());
        }
        baseMapper.insert(bp);

        //保存页面与楼层的关系信息
        BasePageFloor pageFloor = new BasePageFloor();
        for(VoSort floor : vo.getFloorList()){
            pageFloor.setId(UUIDUtil.getShortUUID());
            pageFloor.setPageId(bp.getId());
            pageFloor.setFloorId(floor.getFloorId());
            pageFloor.setSort(floor.getSort());
            pageFloor.setCreateBy(vo.getUserId());
            pageFloor.setCreateDate(LocalDateTime.now());
            pageFloor.setDeleted("0");
            pageFloor.setLastModifiedBy(vo.getUserId());
            pageFloor.setLastModifiedDate(LocalDateTime.now());
            basePageFloorMapper.insert(pageFloor);
        }

        return ResultUtil.success();
    }

    @Override
    public Result editPage(VoSavePage vo) {
        //根据id获取名称
        QueryWrapper<BasePage> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getPageId());
        wrapper.eq("deleted","0");
        BasePage ho = baseMapper.selectOne(wrapper);
        if (ho == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ho.getKeyValue().equals(vo.getKeyValue())) {
            //查重
            wrapper.clear();
            wrapper.eq("keyValue", vo.getKeyValue());
            wrapper.eq("version",vo.getVersion());
            wrapper.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(wrapper);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        //搜集关联附件id,更新前清理
        List<String> attachIds = new ArrayList<>();
        attachIds.add(ho.getImageUrlId());
        attachIds.add(ho.getBackgroundUrlId());
        attachIds.add(ho.getShowUrlId());
        attachMapper.deleteBatchIds(attachIds);

//        basePageMapper.delete(vo.getPageId());
        QueryWrapper<BasePageFloor> pageFloorQuery = new QueryWrapper<BasePageFloor>()
                .eq("pageId", vo.getPageId());
        //删除关联数据
        basePageFloorMapper.delete(pageFloorQuery);


        BasePage b = new BasePage();
        BeanUtils.copyProperties(vo,b);
        b.setId(vo.getPageId());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());

        VoUrl imgUrl = vo.getImageUrl();
        VoUrl backgroundUrl = vo.getBackgroundUrl();
        VoUrl showUrl = vo.getShowUrl();
        if(imgUrl != null && imgUrl.getPicAttachSize()!=null){
            //保存选中图片
            Attach imgAttach = goodsService.saveAttach(vo.getUserId(), imgUrl.getPicAttachName(), imgUrl.getPicAttachNewName(), imgUrl.getPicAttachSize());
            b.setImageUrlId(imgAttach.getId());
        }
        if(backgroundUrl != null && backgroundUrl.getPicAttachSize()!=null){
            Attach backgroundAttach = goodsService.saveAttach(vo.getUserId(), backgroundUrl.getPicAttachName(), backgroundUrl.getPicAttachNewName(), backgroundUrl.getPicAttachSize());
            b.setBackgroundUrlId(backgroundAttach.getId());
        }
        if(showUrl != null&& showUrl.getPicAttachSize()!=null){
            Attach showAttach = goodsService.saveAttach(vo.getUserId(), showUrl.getPicAttachName(), showUrl.getPicAttachNewName(), showUrl.getPicAttachSize());
            b.setShowUrlId(showAttach.getId());
        }
        baseMapper.updateById(b);

        //保存页面与楼层的关系信息
        BasePageFloor pageFloor = new BasePageFloor();
        for(VoSort floor : vo.getFloorList()){
            pageFloor.setId(UUIDUtil.getShortUUID());
            pageFloor.setPageId(vo.getPageId());
            pageFloor.setFloorId(floor.getFloorId());
            pageFloor.setSort(floor.getSort());
            pageFloor.setCreateBy(vo.getUserId());
            pageFloor.setCreateDate(LocalDateTime.now());
            pageFloor.setDeleted("0");
            pageFloor.setLastModifiedBy(vo.getUserId());
            pageFloor.setLastModifiedDate(LocalDateTime.now());
            basePageFloorMapper.insert(pageFloor);
        }
        return ResultUtil.success();
    }

    @Override
    public BasePage insertReturnEntity(BasePage entity) {
        return null;
    }

    @Override
    public BasePage updateReturnEntity(BasePage entity) {
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
