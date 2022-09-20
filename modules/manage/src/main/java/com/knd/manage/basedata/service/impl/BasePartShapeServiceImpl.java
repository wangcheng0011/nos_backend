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
import com.knd.manage.basedata.dto.PartShapeDto;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.basedata.dto.ShapeDto;
import com.knd.manage.basedata.dto.ShapeListDto;
import com.knd.manage.basedata.entity.BasePartShape;
import com.knd.manage.basedata.mapper.BasePartShapeMapper;
import com.knd.manage.basedata.service.IBasePartShapeService;
import com.knd.manage.basedata.vo.VoSaveShape;
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
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zm
 * @since 2021-01-25
 */

@Service
@Transactional
@RequiredArgsConstructor
public class BasePartShapeServiceImpl extends ServiceImpl<BasePartShapeMapper, BasePartShape> implements IBasePartShapeService {
    private final BasePartShapeMapper basePartShapeMapper;
    private final  IGoodsService goodsService;
    private final IAttachService iAttachService;
    private final AttachMapper attachMapper;
    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    @Override
    public Result add(VoSaveShape vo) {
        //保存选中图片
        Attach selectAPi = goodsService.saveAttach(vo.getUserId(), vo.getSelectUrlDto().getPicAttachName()
                , vo.getSelectUrlDto().getPicAttachNewName(), vo.getSelectUrlDto().getPicAttachSize());

        //保存选中图片
        Attach unSelectAPi = goodsService.saveAttach(vo.getUserId(), vo.getUnSelectUrlDto().getPicAttachName()
                , vo.getUnSelectUrlDto().getPicAttachNewName(), vo.getUnSelectUrlDto().getPicAttachSize());

        QueryWrapper<BasePartShape> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("shape",vo.getShape());
        wrapper.eq("sex",vo.getSex());
        //获取总数
        int s = baseMapper.selectCount(wrapper);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        BasePartShape partShape = new BasePartShape();
        partShape.setId(UUIDUtil.getShortUUID());
        partShape.setShape(vo.getShape());
        partShape.setRemark(vo.getRemark());
        partShape.setPartId(vo.getPartId());
        partShape.setSelectUrlId(selectAPi.getId());
        partShape.setUnSelectUrlId(unSelectAPi.getId());
        partShape.setSex(vo.getSex());
        partShape.setCreateBy(vo.getUserId());
        partShape.setCreateDate(LocalDateTime.now());
        partShape.setDeleted("0");
        partShape.setLastModifiedBy(vo.getUserId());
        partShape.setLastModifiedDate(LocalDateTime.now());
        baseMapper.insert(partShape);
        return ResultUtil.success();
    }

    @Override
    public Result edit(VoSaveShape vo) {
        //根据id获取名称
        QueryWrapper<BasePartShape> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getShapeId());
        wrapper.eq("deleted","0");
        BasePartShape ps = baseMapper.selectOne(wrapper);
        if (ps == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ps.getShape().equals(vo.getShape())) {
            //查重
            wrapper.clear();
            wrapper.eq("shape", vo.getShape());
            wrapper.eq("sex",vo.getSex());
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
        attachIds.add(ps.getSelectUrlId());
        attachIds.add(ps.getUnSelectUrlId());
        attachMapper.deleteBatchIds(attachIds);

        //保存选中图片
        Attach selectAPi = goodsService.saveAttach(vo.getUserId(), vo.getSelectUrlDto().getPicAttachName()
                , vo.getSelectUrlDto().getPicAttachNewName(), vo.getSelectUrlDto().getPicAttachSize());

        //保存选中图片
        Attach unSelectAPi = goodsService.saveAttach(vo.getUserId(), vo.getUnSelectUrlDto().getPicAttachName()
                , vo.getUnSelectUrlDto().getPicAttachNewName(), vo.getUnSelectUrlDto().getPicAttachSize());

        BasePartShape b = new BasePartShape();
        b.setId(vo.getShapeId());
        b.setShape(vo.getShape());
        b.setRemark(vo.getRemark());
        b.setPartId(vo.getPartId());
        b.setSex(vo.getSex());
        b.setSelectUrlId(selectAPi.getId());
        b.setUnSelectUrlId(unSelectAPi.getId());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        return ResultUtil.success();
    }

    @Override
    public Result deleteShape(String userId, String id) {
        BasePartShape ba = new BasePartShape();
        ba.setId(id);
        ba.setDeleted("1");
        ba.setLastModifiedBy(userId);
        ba.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(ba);
        return ResultUtil.success();
    }

    @Override
    public Result GetShape(String id) {
        ShapeDto dto = new ShapeDto();
        QueryWrapper<BasePartShape> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("deleted","0");
//        wrapper.select("id", "shape", "remark","partId");
        BasePartShape bp = baseMapper.selectOne(wrapper);

        BeanUtils.copyProperties(bp,dto);
        dto.setPart(basePartShapeMapper.getPart(bp.getPartId()));

        ImgDto selectImgDto = getImgDto(bp.getSelectUrlId());
        dto.setSelectImg(selectImgDto);

        ImgDto unSelectImgDto = getImgDto(bp.getUnSelectUrlId());
        dto.setUnSelectImg(unSelectImgDto);
        //成功
        return ResultUtil.success(dto);
    }

    @Override
    public Result getShapeList(String shape, String currentPage) {
        Page<PartShapeDto> page = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        QueryWrapper<PartShapeDto> wrapper = Wrappers.query();
        wrapper.eq("a.deleted","0");
        if (StringUtils.isNotEmpty(shape)) {
            //不为空时
            wrapper.like("a.shape", shape);
        }
        List<PartShapeDto> list = basePartShapeMapper.getList(page, wrapper);
        ShapeListDto dto = ShapeListDto.builder().total((int)page.getTotal()).shapeList(list).build();
        return ResultUtil.success(dto);
    }

    @Override
    public BasePartShape insertReturnEntity(BasePartShape entity) {
        return null;
    }

    @Override
    public BasePartShape updateReturnEntity(BasePartShape entity) {
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
