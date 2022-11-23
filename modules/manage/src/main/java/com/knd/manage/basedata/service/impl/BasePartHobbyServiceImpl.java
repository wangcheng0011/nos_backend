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
import com.knd.manage.basedata.dto.HobbyDto;
import com.knd.manage.basedata.dto.HobbyListDto;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.basedata.dto.PartHobbyDto;
import com.knd.manage.basedata.entity.BasePartHobby;
import com.knd.manage.basedata.mapper.BasePartHobbyMapper;
import com.knd.manage.basedata.service.IBasePartHobbyService;
import com.knd.manage.basedata.vo.VoSaveHobby;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.mall.service.IGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
public class BasePartHobbyServiceImpl extends ServiceImpl<BasePartHobbyMapper, BasePartHobby> implements IBasePartHobbyService {

    private final BasePartHobbyMapper basePartHobbyMapper;
    private final IGoodsService goodsService;
    private final IAttachService iAttachService;
    private final AttachMapper attachMapper;

    @Override
    public Result add(String userId, VoSaveHobby vo) {
        //保存选中图片
        Attach selectAPi = iAttachService.saveAttach(userId, vo.getSelectUrlDto().getPicAttachName()
                , vo.getSelectUrlDto().getPicAttachNewName(), vo.getSelectUrlDto().getPicAttachSize());

        //保存选中图片
        Attach unSelectAPi = iAttachService.saveAttach(userId, vo.getUnSelectUrlDto().getPicAttachName()
                , vo.getUnSelectUrlDto().getPicAttachNewName(), vo.getUnSelectUrlDto().getPicAttachSize());

        QueryWrapper<BasePartHobby> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("hobby",vo.getHobby());
        //获取总数
        int s = baseMapper.selectCount(wrapper);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        BasePartHobby partHobby = new BasePartHobby();
        partHobby.setId(UUIDUtil.getShortUUID());
        partHobby.setHobby(vo.getHobby());
        partHobby.setRemark(vo.getRemark());
        partHobby.setPartId(vo.getPartId());
        partHobby.setSelectUrlId(selectAPi.getId());
        partHobby.setUnSelectUrlId(unSelectAPi.getId());
        partHobby.setCreateBy(userId);
        partHobby.setCreateDate(LocalDateTime.now());
        partHobby.setDeleted("0");
        partHobby.setLastModifiedBy(userId);
        partHobby.setLastModifiedDate(LocalDateTime.now());
        baseMapper.insert(partHobby);
        return ResultUtil.success();
    }

    @Override
    public Result edit(String userId, VoSaveHobby vo) {
        //根据id获取名称
        QueryWrapper<BasePartHobby> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getHobbyId());
        wrapper.eq("deleted","0");
        BasePartHobby ho = baseMapper.selectOne(wrapper);
        if (ho == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ho.getHobby().equals(vo.getHobby())) {
            //查重
            wrapper.clear();
            wrapper.eq("hobby", vo.getHobby());
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
        attachIds.add(ho.getSelectUrlId());
        attachIds.add(ho.getUnSelectUrlId());
        attachMapper.deleteBatchIds(attachIds);

        //保存选中图片
        Attach selectAPi = iAttachService.saveAttach(userId, vo.getSelectUrlDto().getPicAttachName()
                , vo.getSelectUrlDto().getPicAttachNewName(), vo.getSelectUrlDto().getPicAttachSize());

        //保存选中图片
        Attach unSelectAPi = iAttachService.saveAttach(userId, vo.getUnSelectUrlDto().getPicAttachName()
                , vo.getUnSelectUrlDto().getPicAttachNewName(), vo.getUnSelectUrlDto().getPicAttachSize());

        BasePartHobby b = new BasePartHobby();
        b.setId(vo.getHobbyId());
        b.setHobby(vo.getHobby());
        b.setRemark(vo.getRemark());
        b.setPartId(vo.getPartId());
        b.setSelectUrlId(selectAPi.getId());
        b.setUnSelectUrlId(unSelectAPi.getId());
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        return ResultUtil.success();
    }

    @Override
    public Result deleteHobby(String userId, String id) {
        BasePartHobby ba = new BasePartHobby();
        ba.setId(id);
        ba.setDeleted("1");
        ba.setLastModifiedBy(userId);
        ba.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(ba);
        return ResultUtil.success();
    }

    @Override
    public Result getHobby(String id) {
        HobbyDto dto = new HobbyDto();
        QueryWrapper<BasePartHobby> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("deleted","0");
        BasePartHobby basePartHobby = baseMapper.selectOne(wrapper);

        BeanUtils.copyProperties(basePartHobby,dto);
        dto.setPart(basePartHobbyMapper.getPart(basePartHobby.getPartId()));

        ImgDto selectImgDto = iAttachService.getImgDto(basePartHobby.getSelectUrlId());
        dto.setSelectImg(selectImgDto);

        ImgDto unSelectImgDto = iAttachService.getImgDto(basePartHobby.getUnSelectUrlId());
        dto.setUnSelectImg(unSelectImgDto);
        //成功
        return ResultUtil.success(dto);
    }

    @Override
    public Result getHobbyList(String hobby, String currentPage) {
        Page<PartHobbyDto> tPage = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        QueryWrapper<PartHobbyDto> wrapper = Wrappers.query();
        wrapper.eq("a.deleted","0");
        if (StringUtils.isNotEmpty(hobby)) {
            //不为空时
            wrapper.like("a.hobby", hobby);
        }
        List<PartHobbyDto> partHobbyDto = basePartHobbyMapper.getList(tPage, wrapper);
        HobbyListDto dto = HobbyListDto.builder().total((int)tPage.getTotal()).hobbyList(partHobbyDto).build();
        return ResultUtil.success(dto);
    }



    @Override
    public BasePartHobby insertReturnEntity(BasePartHobby entity) {
        return null;
    }

    @Override
    public BasePartHobby updateReturnEntity(BasePartHobby entity) {
        return null;
    }


}
