package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.basedata.dto.PowerTestDto;
import com.knd.manage.basedata.dto.PowerTestItemDto;
import com.knd.manage.basedata.dto.PowerTestListDto;
import com.knd.manage.basedata.entity.BaseDifficulty;
import com.knd.manage.basedata.entity.PowerTestEntity;
import com.knd.manage.basedata.entity.PowerTestItemEntity;
import com.knd.manage.basedata.mapper.BaseDifficultyMapper;
import com.knd.manage.basedata.mapper.PowerTestItemMapper;
import com.knd.manage.basedata.mapper.PowerTestMapper;
import com.knd.manage.basedata.service.PowerTestService;
import com.knd.manage.basedata.vo.VoSavePowerTest;
import com.knd.manage.basedata.vo.VoSavePowerTestItem;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.common.vo.VoId;
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
 * @author zm
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PowerTestServiceImpl implements PowerTestService {
    private final PowerTestMapper powerTestMapper;
    private final PowerTestItemMapper powerTestItemMapper;
    private final BaseDifficultyMapper difficultyMapper;
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
    public Result getPowerTestList(String gender, String difficultyId, String current) {
        Page<PowerTestEntity> page = new Page<>(Integer.valueOf(current), PageInfo.pageSize);
        QueryWrapper<PowerTestEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        if (StringUtils.isNotEmpty(gender)){
            wrapper.eq("gender",gender);
        }
        if (StringUtils.isNotEmpty(difficultyId)){
            wrapper.eq("difficultyId",difficultyId);
        }
        Page<PowerTestEntity> powerTestEntityPage = powerTestMapper.selectPage(page, wrapper);
        List<PowerTestEntity> list = powerTestEntityPage.getRecords();
        List<PowerTestListDto> dtoList = new ArrayList<>();
        for (PowerTestEntity entity : list){
            PowerTestListDto dto = new PowerTestListDto();
            BeanUtils.copyProperties(entity,dto);
            BaseDifficulty baseDifficulty = difficultyMapper.selectById(entity.getDifficultyId());
            if (StringUtils.isEmpty(baseDifficulty)){
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"难度不存在");
            }
            dto.setDifficulty(baseDifficulty.getDifficulty());
            dtoList.add(dto);
        }
        ResponseDto<PowerTestListDto> responseDto = ResponseDto.<PowerTestListDto>builder().total((int) page.getTotal()).resList(dtoList).build();
        //成功
        return ResultUtil.success(responseDto);
    }

    @Override
    public Result getPowerTest(String id) {
        PowerTestDto dto = new PowerTestDto();
        List<PowerTestItemDto> itemDtoList = new ArrayList<>();
        PowerTestEntity entity = powerTestMapper.selectById(id);
        if (StringUtils.isEmpty(entity)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"数据不存在");
        }
        BeanUtils.copyProperties(entity,dto);
        List<PowerTestItemEntity> powerTestItemEntities = powerTestItemMapper.selectList(new QueryWrapper<PowerTestItemEntity>().eq("deleted", "0").eq("powerTestId", id));
        for (PowerTestItemEntity itemEntity : powerTestItemEntities){
            PowerTestItemDto itemDto = new PowerTestItemDto();
            BeanUtils.copyProperties(itemEntity,itemDto);
            itemDtoList.add(itemDto);
        }
        dto.setItemDtoList(itemDtoList);
        ImgDto imgDto = getImgDto(entity.getPicAttachId());
        dto.setPicAttach(imgDto);
        return ResultUtil.success(dto);
    }

    @Override
    public Result addPowerTest(VoSavePowerTest vo) {
        String userId = vo.getUserId();
        QueryWrapper<PowerTestEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        wrapper.eq("gender",vo.getGender());
        wrapper.eq("difficultyId",vo.getDifficultyId());
        Integer num = powerTestMapper.selectCount(wrapper);
        if (num > 0){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"数据重复");
        }

        String imageUrlId = "";
        if(StringUtils.isNotEmpty(vo.getPicAttachUrl())
                && StringUtils.isNotEmpty(vo.getPicAttachUrl().getPicAttachName())){
            //保存选中图片
            Attach imgAPi = goodsService.saveAttach(userId, vo.getPicAttachUrl().getPicAttachName()
                    , vo.getPicAttachUrl().getPicAttachNewName(), vo.getPicAttachUrl().getPicAttachSize());
            imageUrlId = imgAPi.getId();
        }

        PowerTestEntity powerTestEntity = new PowerTestEntity();
        BeanUtils.copyProperties(vo,powerTestEntity);
        powerTestEntity.setId(UUIDUtil.getShortUUID());
        powerTestEntity.setPicAttachId(imageUrlId);
        powerTestEntity.setCreateBy(userId);
        powerTestEntity.setCreateDate(LocalDateTime.now());
        powerTestEntity.setDeleted("0");
        powerTestEntity.setLastModifiedBy(userId);
        powerTestEntity.setLastModifiedDate(LocalDateTime.now());
        powerTestMapper.insert(powerTestEntity);
        for (VoSavePowerTestItem voItem : vo.getItemList()){
            PowerTestItemEntity itemEntity = new PowerTestItemEntity();
            BeanUtils.copyProperties(voItem,itemEntity);
            itemEntity.setId(UUIDUtil.getShortUUID());
            itemEntity.setPowerTestId(powerTestEntity.getId());
            itemEntity.setCreateBy(userId);
            itemEntity.setCreateDate(LocalDateTime.now());
            itemEntity.setDeleted("0");
            itemEntity.setLastModifiedBy(userId);
            itemEntity.setLastModifiedDate(LocalDateTime.now());
            powerTestItemMapper.insert(itemEntity);
        }
        return ResultUtil.success();
    }

    @Override
    public Result editPowerTest(VoSavePowerTest vo) {
        String userId = vo.getUserId();
        QueryWrapper<PowerTestEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getPowerTestId());
        wrapper.eq("deleted","0");
        PowerTestEntity powerTestEntity = powerTestMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(powerTestEntity)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"数据不存在");
        }
        //性别不同 || 难度不同，需要查重
        if (!powerTestEntity.getGender().equals(vo.getGender()) || !powerTestEntity.getDifficultyId().equals(vo.getDifficultyId())){
            //查重
            wrapper.clear();
            wrapper.eq("deleted","0");
            wrapper.eq("gender",vo.getGender());
            wrapper.eq("difficultyId",vo.getDifficultyId());
            Integer num = powerTestMapper.selectCount(wrapper);
            if (num > 0){
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }

        if(StringUtils.isNotEmpty(powerTestEntity.getPicAttachId())){
            attachMapper.deleteById(powerTestEntity.getPicAttachId());
        }

        String imageUrlId = "";
        if(StringUtils.isNotEmpty(vo.getPicAttachUrl())
                && StringUtils.isNotEmpty(vo.getPicAttachUrl().getPicAttachName())){
            //保存选中图片
            Attach imgAPi = goodsService.saveAttach(userId, vo.getPicAttachUrl().getPicAttachName()
                    , vo.getPicAttachUrl().getPicAttachNewName(), vo.getPicAttachUrl().getPicAttachSize());
            imageUrlId = imgAPi.getId();
        }

        BeanUtils.copyProperties(vo,powerTestEntity);
        powerTestEntity.setPicAttachId(imageUrlId);
        powerTestEntity.setLastModifiedBy(userId);
        powerTestEntity.setLastModifiedDate(LocalDateTime.now());
        powerTestMapper.updateById(powerTestEntity);

        powerTestItemMapper.delete(new QueryWrapper<PowerTestItemEntity>().eq("deleted", "0").eq("powerTestId", powerTestEntity.getId()));

        for (VoSavePowerTestItem voItem : vo.getItemList()){
            PowerTestItemEntity itemEntity = new PowerTestItemEntity();
            BeanUtils.copyProperties(voItem,itemEntity);
            itemEntity.setId(UUIDUtil.getShortUUID());
            itemEntity.setPowerTestId(powerTestEntity.getId());
            itemEntity.setCreateBy(userId);
            itemEntity.setCreateDate(LocalDateTime.now());
            itemEntity.setDeleted("0");
            itemEntity.setLastModifiedBy(userId);
            itemEntity.setLastModifiedDate(LocalDateTime.now());
            powerTestItemMapper.insert(itemEntity);
        }
        return ResultUtil.success();
    }

    @Override
    public Result deletePowerTest(VoId vo) {
        PowerTestEntity entity = powerTestMapper.selectById(vo.getId());
        if (StringUtils.isEmpty(entity)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"数据不存在");
        }
        entity.setDeleted("1");
        entity.setLastModifiedBy(vo.getUserId());
        entity.setLastModifiedDate(LocalDateTime.now());
        powerTestMapper.updateById(entity);
        return ResultUtil.success();
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
