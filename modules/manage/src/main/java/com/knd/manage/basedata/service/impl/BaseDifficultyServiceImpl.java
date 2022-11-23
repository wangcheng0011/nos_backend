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
import com.knd.manage.basedata.dto.DifficultyDto;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.basedata.entity.BaseDifficulty;
import com.knd.manage.basedata.mapper.BaseDifficultyMapper;
import com.knd.manage.basedata.service.IBaseDifficultyService;
import com.knd.manage.basedata.vo.VoGetDifficultyList;
import com.knd.manage.basedata.vo.VoSaveDifficulty;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lenovo
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BaseDifficultyServiceImpl extends ServiceImpl<BaseDifficultyMapper, BaseDifficulty> implements IBaseDifficultyService {
    private final IAttachService iAttachService;
    private final AttachMapper attachMapper;


    @Override
    public Result getDifficultyList(VoGetDifficultyList vo) {
        String currentPage = vo.getCurrent();
        QueryWrapper<BaseDifficulty> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(vo.getType())) {
            qw.eq("type", vo.getType());
        }
        if(StringUtils.isNotEmpty(vo.getDifficulty())){
            qw.eq("difficulty",vo.getDifficulty());
        }

        qw.select("id", "type", "difficulty","remark");
        qw.eq("deleted", "0");
        List<BaseDifficulty> list;
        ResponseDto dto = ResponseDto.<BaseDifficulty>builder().build();
        if (StringUtils.isEmpty(currentPage)) {
            //获取全部
            list = baseMapper.selectList(qw);
            dto.setTotal(list.size());
        }else{
            //分页
            Page<BaseDifficulty> page = new Page<>(Integer.parseInt(currentPage), PageInfo.pageSize);
            page = baseMapper.selectPage(page, qw);
            list = page.getRecords();
            dto.setTotal((int) page.getTotal());
        }
        dto.setResList(list);
        //成功
        return ResultUtil.success(dto);
    }

    @Override
    public Result getDifficulty(String id) {
        DifficultyDto dto = new DifficultyDto();
        QueryWrapper<BaseDifficulty> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("deleted","0");
        BaseDifficulty baseDifficulty = baseMapper.selectOne(wrapper);
        BeanUtils.copyProperties(baseDifficulty,dto);

        ImgDto imgDto = iAttachService.getImgDto(baseDifficulty.getImageUrlId());
        dto.setImageUrl(imgDto);

        return ResultUtil.success(dto);
    }

    @Override
    public Result add(String userId, VoSaveDifficulty vo) {
        QueryWrapper<BaseDifficulty> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("difficulty",vo.getDifficulty());
        wrapper.eq("type",vo.getType());
        //获取总数
        int s = baseMapper.selectCount(wrapper);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }

        String imageUrlId = "";
        if(StringUtils.isNotEmpty(vo.getImageUrl())
            && StringUtils.isNotEmpty(vo.getImageUrl().getPicAttachName())){
            //保存选中图片
            Attach imgAPi = iAttachService.saveAttach(userId, vo.getImageUrl().getPicAttachName()
                    , vo.getImageUrl().getPicAttachNewName(), vo.getImageUrl().getPicAttachSize());
            imageUrlId = imgAPi.getId();
        }

        BaseDifficulty difficulty = new BaseDifficulty();
        BeanUtils.copyProperties(vo,difficulty);
        difficulty.setId(UUIDUtil.getShortUUID());
        difficulty.setImageUrlId(imageUrlId);
        difficulty.setCreateBy(userId);
        difficulty.setCreateDate(LocalDateTime.now());
        difficulty.setDeleted("0");
        difficulty.setLastModifiedBy(userId);
        difficulty.setLastModifiedDate(LocalDateTime.now());
        baseMapper.insert(difficulty);
        return ResultUtil.success();
    }

    @Override
    public Result edit(String userId, VoSaveDifficulty vo) {
        //根据id获取名称
        QueryWrapper<BaseDifficulty> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getDifficultyId());
        wrapper.eq("deleted","0");
        BaseDifficulty ho = baseMapper.selectOne(wrapper);
        if (ho == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ho.getDifficulty().equals(vo.getDifficulty())) {
            //查重
            wrapper.clear();
            wrapper.eq("difficulty",vo.getDifficulty());
            wrapper.eq("type",vo.getType());
            wrapper.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(wrapper);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        if(StringUtils.isNotEmpty(ho.getImageUrlId())){
            attachMapper.deleteById(ho.getImageUrlId());
        }
        String imageUrlId = "";
        if(StringUtils.isNotEmpty(vo.getImageUrl())
                && StringUtils.isNotEmpty(vo.getImageUrl().getPicAttachName())){
            //保存选中图片
            Attach imgAPi = iAttachService.saveAttach(userId, vo.getImageUrl().getPicAttachName()
                    , vo.getImageUrl().getPicAttachNewName(), vo.getImageUrl().getPicAttachSize());
            imageUrlId = imgAPi.getId();
        }
        ho.setType(vo.getType());
        ho.setDifficulty(vo.getDifficulty());
        ho.setRemark(vo.getRemark());
        ho.setImageUrlId(imageUrlId);
        ho.setLastModifiedBy(userId);
        ho.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(ho);
        return ResultUtil.success();
    }

    @Override
    public Result delete(String userId, String id) {
        BaseDifficulty ba = new BaseDifficulty();
        ba.setId(id);
        ba.setDeleted("1");
        ba.setLastModifiedBy(userId);
        ba.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(ba);
        return ResultUtil.success();
    }

    @Override
    public BaseDifficulty insertReturnEntity(BaseDifficulty entity) {
        return null;
    }

    @Override
    public BaseDifficulty updateReturnEntity(BaseDifficulty entity) {
        return null;
    }


}
