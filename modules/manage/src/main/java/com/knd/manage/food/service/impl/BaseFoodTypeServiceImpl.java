package com.knd.manage.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.food.dto.BaseFoodTypeDto;
import com.knd.manage.food.dto.FoodDto;
import com.knd.manage.food.entity.BaseFoodType;
import com.knd.manage.food.entity.Food;
import com.knd.manage.food.mapper.BaseFoodTypeMapper;
import com.knd.manage.food.mapper.FoodMapper;
import com.knd.manage.food.service.IBaseFoodTypeService;
import com.knd.manage.food.vo.VoSaveFoodItem;
import com.knd.manage.food.vo.VoSaveFoodType;
import com.knd.manage.mall.service.IGoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BaseFoodTypeServiceImpl extends ServiceImpl<BaseFoodTypeMapper, BaseFoodType> implements IBaseFoodTypeService {

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private FoodMapper foodMapper;

    public BaseFoodTypeServiceImpl() {
    }

    //新增
    @Override
    public Result add(String userId, VoSaveFoodType vo) {
        //查重
        QueryWrapper<BaseFoodType> qw = new QueryWrapper<>();
        qw.eq("name", vo.getName());
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        String musicUrlId = "";
        if(StringUtils.isNotEmpty(vo.getPicAttachUrl())
                && StringUtils.isNotEmpty(vo.getPicAttachUrl().getPicAttachName())){
            //保存选中图片
            Attach imgAPi = iAttachService.saveAttach(vo.getUserId(), vo.getPicAttachUrl().getPicAttachName()
                    , vo.getPicAttachUrl().getPicAttachNewName(), vo.getPicAttachUrl().getPicAttachSize());
            musicUrlId = imgAPi.getId();
        }
        BaseFoodType b = new BaseFoodType();
        //新增
        if(StringUtils.isNotEmpty(vo.getId())){
            b.setId(vo.getId());
        }else {
            b.setId(UUIDUtil.getShortUUID());
        }
        b.setUserId(vo.getUserId());
        b.setName(vo.getName());
        b.setFoodUrlId(musicUrlId);
        b.setCreateBy(userId);
        b.setCreateDate(LocalDateTime.now());
        b.setDeleted("0");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.insert(b);
        for (VoSaveFoodItem voItem : vo.getItemList()){
            Food itemEntity = new Food();
            BeanUtils.copyProperties(voItem,itemEntity);
            if(StringUtils.isNotEmpty(itemEntity.getId())){
                itemEntity.setId(vo.getId());
            }else {
                itemEntity.setId(UUIDUtil.getShortUUID());
            }
            itemEntity.setParentId(b.getId());
            itemEntity.setUserId(vo.getUserId());
            itemEntity.setCreateBy(userId);
            itemEntity.setCreateDate(LocalDateTime.now());
            itemEntity.setDeleted("0");
            itemEntity.setLastModifiedBy(userId);
            itemEntity.setLastModifiedDate(LocalDateTime.now());
            foodMapper.insert(itemEntity);
        }
        //成功
        return ResultUtil.success(b);
    }

    //更新
    @Override
    public Result edit(String userId, VoSaveFoodType vo) {
        //根据id获取名称
        QueryWrapper<BaseFoodType> qw = new QueryWrapper<>();
        qw.eq("id", vo.getId());
        qw.eq("deleted", "0");
        BaseFoodType ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ta.getName().equals(vo.getName())) {
            //查重
            qw.clear();
            qw.eq("name", vo.getName());
          ///  qw.eq("userId",vo.getUserId());
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        String musicUrlId = "";
        if(StringUtils.isNotEmpty(vo.getPicAttachUrl())
                && StringUtils.isNotEmpty(vo.getPicAttachUrl().getPicAttachName())){
            //保存选中图片
            Attach imgAPi = iAttachService.saveAttach(vo.getUserId(), vo.getPicAttachUrl().getPicAttachName()
                    , vo.getPicAttachUrl().getPicAttachNewName(), vo.getPicAttachUrl().getPicAttachSize());
            musicUrlId = imgAPi.getId();
        }
        ta.setName(vo.getName());
        ta.setFoodUrlId(musicUrlId);
        ta.setLastModifiedBy(userId);
        ta.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(ta);
        for (VoSaveFoodItem voItem : vo.getItemList()){
            Food itemEntity = new Food();
            BeanUtils.copyProperties(voItem,itemEntity);
            itemEntity.setParentId(ta.getId());
            itemEntity.setDeleted("0");
            itemEntity.setLastModifiedBy(userId);
            itemEntity.setLastModifiedDate(LocalDateTime.now());
            foodMapper.updateById(itemEntity);
        }
        //成功
        return ResultUtil.success(ta);
    }

    //删除
    @Override
    public Result deleteFoodType(String userId, String id) {
        BaseFoodType b = baseMapper.selectById(id);
        if (StringUtils.isEmpty(b)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"数据不存在");
        }
        b.setDeleted("1");
        b.setUserId(userId);
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    //获取食物分类
    @Override
    public Result getFoodType(String id) {
        QueryWrapper<BaseFoodType> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", 0);
        BaseFoodType b = baseMapper.selectOne(qw);
        BaseFoodTypeDto dto = new BaseFoodTypeDto();
        BeanUtils.copyProperties(b,dto);
        QueryWrapper<Food> foodQueryWrapper = new QueryWrapper<>();
        foodQueryWrapper.eq("parentId",b.getId());
        foodQueryWrapper.eq("deleted",0);
        foodQueryWrapper.orderByDesc("lastModifiedDate");
        List<Food> foods = foodMapper.selectList(foodQueryWrapper);
        ArrayList<FoodDto> foodDtos = new ArrayList<>();
        foods.stream().forEach(i->{
            FoodDto foodDto = new FoodDto();
            BeanUtils.copyProperties(i,foodDto);
            foodDtos.add(foodDto);
        });
        dto.setItemList(foodDtos);
        if(StringUtils.isNotEmpty(b.getFoodUrlId())){
            dto.setPicAttachUrl(iAttachService.getImgDto(b.getFoodUrlId()));
        }
        //成功
        return ResultUtil.success(dto);
    }

    //获取食物类型分类列表
    @Override
    public Result getFoodTypeList(String name, String currentPage) {
        Page<BaseFoodType> page = new Page<>(Integer.valueOf(currentPage), PageInfo.pageSize);
        QueryWrapper<BaseFoodType> wrapper = new QueryWrapper<>();
      //  wrapper.eq("userId", UserUtils.getUserId());
        wrapper.eq("deleted","0");
        if (StringUtils.isNotEmpty(name)){
            wrapper.like("name",name);
        }
        wrapper.orderByDesc("lastModifiedDate");
        Page<BaseFoodType> baseFoodTypePage = baseMapper.selectPage(page, wrapper);
        List<BaseFoodType> list = baseFoodTypePage.getRecords();
        List<BaseFoodTypeDto> dtoList = new ArrayList<>();
        for (BaseFoodType entity : list){
            BaseFoodTypeDto dto = new BaseFoodTypeDto();
            BeanUtils.copyProperties(entity,dto);
            QueryWrapper<Food> foodQueryWrapper = new QueryWrapper<>();
            foodQueryWrapper.eq("parentId",entity.getId());
            //foodQueryWrapper.eq("userId",UserUtils.getUserId());
            foodQueryWrapper.eq("deleted",0);
            foodQueryWrapper.orderByDesc("lastModifiedDate");
            List<Food> foods = foodMapper.selectList(foodQueryWrapper);
            ArrayList<FoodDto> foodDtos = new ArrayList<>();
            foods.stream().forEach(i->{
                FoodDto foodDto = new FoodDto();
                BeanUtils.copyProperties(i,foodDto);
                foodDtos.add(foodDto);
            });
            dto.setItemList(foodDtos);
            if(StringUtils.isNotEmpty(entity.getFoodUrlId())){
                dto.setPicAttachUrl(iAttachService.getImgDto(entity.getFoodUrlId()));
            }
            dtoList.add(dto);
        }
        Page<BaseFoodTypeDto> dtoPage = new Page<>();
        dtoPage.setTotal(baseFoodTypePage.getTotal());
        dtoPage.setCurrent(baseFoodTypePage.getCurrent());
        dtoPage.setSize(baseFoodTypePage.getSize());
        dtoPage.setRecords(dtoList);
        //成功
        return ResultUtil.success(dtoPage);
    }

    @Override
    public BaseFoodType insertReturnEntity(BaseFoodType entity) {
        return null;
    }

    @Override
    public BaseFoodType updateReturnEntity(BaseFoodType entity) {
        return null;
    }
}