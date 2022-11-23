package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.ElementDetailDto;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.basedata.entity.BaseElement;
import com.knd.manage.basedata.mapper.BaseElementMapper;
import com.knd.manage.basedata.service.IBaseElementService;
import com.knd.manage.basedata.vo.VoSaveElement;
import com.knd.manage.basedata.vo.VoUrl;
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
public class BaseElementServiceImpl extends ServiceImpl<BaseElementMapper, BaseElement> implements IBaseElementService {
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
    public Result getElement(String id) {
        ElementDetailDto dto = new ElementDetailDto();
        QueryWrapper<BaseElement> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("deleted","0");
        BaseElement basePage = baseMapper.selectOne(wrapper);
        BeanUtils.copyProperties(basePage,dto);

        ImgDto selectImgDto = iAttachService.getImgDto(basePage.getImageUrlId());
        dto.setImageUrl(selectImgDto);

        ImgDto unSelectImgDto = iAttachService.getImgDto(basePage.getBackgroundUrlId());
        dto.setBackgroundUrl(unSelectImgDto);

        ImgDto showUrlDto = iAttachService.getImgDto(basePage.getShowUrlId());
        dto.setShowUrl(showUrlDto);
        //成功
        return ResultUtil.success(dto);
    }

    @Override
    public Result deleteElement(String userId, String id) {
        BaseElement ba = new BaseElement();
        ba.setId(id);
        ba.setDeleted("1");
        ba.setLastModifiedBy(userId);
        ba.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(ba);


        return ResultUtil.success();
    }

    @Override
    public Result addElement(String userId, VoSaveElement vo) {
        QueryWrapper<BaseElement> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("keyValue",vo.getKeyValue());
        wrapper.eq("floorId",vo.getFloorId());
        //获取总数
        int s = baseMapper.selectCount(wrapper);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        BaseElement baseElement = new BaseElement();
        BeanUtils.copyProperties(vo,baseElement);
        baseElement.setId(UUIDUtil.getShortUUID());
        baseElement.setCreateBy(userId);
        baseElement.setCreateDate(LocalDateTime.now());
        baseElement.setDeleted("0");
        baseElement.setLastModifiedBy(userId);
        baseElement.setLastModifiedDate(LocalDateTime.now());

        VoUrl imgElementUrl = vo.getImageUrl();
        VoUrl backgroundElementUrl = vo.getBackgroundUrl();
        VoUrl showElementUrl = vo.getShowUrl();
        if(imgElementUrl != null && imgElementUrl.getPicAttachSize()!=null){
            //保存选中图片
            Attach imgAttach = iAttachService.saveAttach(userId, imgElementUrl.getPicAttachName(), imgElementUrl.getPicAttachNewName(), imgElementUrl.getPicAttachSize());
            baseElement.setImageUrlId(imgAttach.getId());
        }
        if(backgroundElementUrl != null && backgroundElementUrl.getPicAttachSize()!=null){
            Attach backgroundAttach = iAttachService.saveAttach(userId, backgroundElementUrl.getPicAttachName(), backgroundElementUrl.getPicAttachNewName(), backgroundElementUrl.getPicAttachSize());
            baseElement.setBackgroundUrlId(backgroundAttach.getId());
        }
        if(showElementUrl != null && showElementUrl.getPicAttachSize()!=null){
            Attach showAttach = iAttachService.saveAttach(userId, showElementUrl.getPicAttachName(), showElementUrl.getPicAttachNewName(), showElementUrl.getPicAttachSize());
            baseElement.setShowUrlId(showAttach.getId());
        }
        baseElementMapper.insert(baseElement);
        return ResultUtil.success();
    }

    @Override
    public Result editElement(String userId, VoSaveElement vo) {
        //根据id获取名称
        QueryWrapper<BaseElement> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getElementId());
        wrapper.eq("deleted","0");
        BaseElement ho = baseMapper.selectOne(wrapper);
        if (ho == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ho.getKeyValue().equals(vo.getKeyValue())) {
            //查重
            wrapper.clear();
            wrapper.eq("keyValue", vo.getKeyValue());
            wrapper.eq("floorId",vo.getFloorId());
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

        BaseElement baseElement = new BaseElement();
        BeanUtils.copyProperties(vo,baseElement);
        baseElement.setId(vo.getElementId());
        baseElement.setCreateBy(userId);
        baseElement.setCreateDate(LocalDateTime.now());
        baseElement.setDeleted("0");
        baseElement.setLastModifiedBy(userId);
        baseElement.setLastModifiedDate(LocalDateTime.now());

        VoUrl imgElementUrl = vo.getImageUrl();
        VoUrl backgroundElementUrl = vo.getBackgroundUrl();
        VoUrl showElementUrl = vo.getShowUrl();
        if(imgElementUrl != null && imgElementUrl.getPicAttachSize()!=null){
            //保存选中图片
            Attach imgAttach = iAttachService.saveAttach(userId, imgElementUrl.getPicAttachName(), imgElementUrl.getPicAttachNewName(), imgElementUrl.getPicAttachSize());
            baseElement.setImageUrlId(imgAttach.getId());
        }
        if(backgroundElementUrl != null && backgroundElementUrl.getPicAttachSize()!=null){
            Attach backgroundAttach = iAttachService.saveAttach(userId, backgroundElementUrl.getPicAttachName(), backgroundElementUrl.getPicAttachNewName(), backgroundElementUrl.getPicAttachSize());
            baseElement.setBackgroundUrlId(backgroundAttach.getId());
        }
        if(showElementUrl != null && showElementUrl.getPicAttachSize()!=null){
            Attach showAttach = iAttachService.saveAttach(userId, showElementUrl.getPicAttachName(), showElementUrl.getPicAttachNewName(), showElementUrl.getPicAttachSize());
            baseElement.setShowUrlId(showAttach.getId());
        }
        baseElementMapper.updateById(baseElement);
        return ResultUtil.success();
    }

    @Override
    public BaseElement insertReturnEntity(BaseElement entity) {
        return null;
    }

    @Override
    public BaseElement updateReturnEntity(BaseElement entity) {
        return null;
    }




}
