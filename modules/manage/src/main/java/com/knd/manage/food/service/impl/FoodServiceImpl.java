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
import com.knd.manage.food.dto.FoodListDto;
import com.knd.manage.food.entity.Food;
import com.knd.manage.food.mapper.FoodMapper;
import com.knd.manage.food.service.IFoodService;
import com.knd.manage.food.vo.VoSaveFood;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements IFoodService {


    //新增
    @Override
    public Result add(VoSaveFood vo) {
        //查重
        /*QueryWrapper<Food> qw = new QueryWrapper<>();
        qw.eq("name", vo.getName());
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }*/

        Food b = new Food();
        //新增
        b.setId(UUIDUtil.getShortUUID());
        b.setName(vo.getName());
        b.setParentId(vo.getParentId());
        b.setQuantity(vo.getQuantity());
        b.setQuantityHeat(vo.getQuantityHeat());
        b.setUnit(vo.getUnit());
        b.setUserId(vo.getUserId());
        b.setCreateBy(vo.getUserId());
        b.setCreateDate(LocalDateTime.now());
        b.setDeleted("0");
        baseMapper.insert(b);
        //成功
        return ResultUtil.success(b);
    }

    //更新
    @Override
    public Result edit(VoSaveFood vo) {
        //根据id获取名称
        QueryWrapper<Food> qw = new QueryWrapper<>();
        qw.eq("id", vo.getId());
        qw.eq("deleted", "0");
        Food ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ta.getName().equals(vo.getName())) {
            //查重
            qw.clear();
            qw.eq("name", vo.getName());
        //    qw.eq("userId",vo.getUserId());
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        ta.setName(vo.getName());
        ta.setParentId(vo.getParentId());
        ta.setQuantity(vo.getQuantity());
        ta.setQuantityHeat(vo.getQuantityHeat());
        ta.setUnit(vo.getUnit());
        ta.setLastModifiedBy(vo.getUserId());
        ta.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(ta);
        //成功
        return ResultUtil.success(ta);
    }

    //删除
    @Override
    public Result deleteFood(String userId, String id) {
        Food b = new Food();
        b.setId(id);
        b.setDeleted("1");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    //获取食物分类
    @Override
    public Result getFood(String id) {
        QueryWrapper<Food> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", 0);
        Food b = baseMapper.selectOne(qw);
        //成功
        return ResultUtil.success(b);
    }

    //获取食物分类列表
    @Override
    public Result getFoodList(String name, String currentPage) {
        QueryWrapper<Food> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            qw.like("name", name);
        }
        qw.eq("deleted", "0");
       // qw.eq("userId", UserUtils.getUserId());
        qw.orderByAsc("lastModifiedDate");
        List<Food> list;
        FoodListDto foodListDto = new FoodListDto();
        if (StringUtils.isEmpty(currentPage)) {
            //获取全部
            list = baseMapper.selectList(qw);
            foodListDto.setTotal(list.size());
        } else {
            //分页
            Page<Food> page = new Page<>(Integer.parseInt(currentPage), PageInfo.pageSize);
            page = baseMapper.selectPage(page, qw);
            list = page.getRecords();
            foodListDto.setTotal((int) page.getTotal());
        }
        foodListDto.setFoodList(list);
        //成功
        return ResultUtil.success(foodListDto);
    }

    @Override
    public Food insertReturnEntity(Food entity) {
        return null;
    }

    @Override
    public Food updateReturnEntity(Food entity) {
        return null;
    }
}