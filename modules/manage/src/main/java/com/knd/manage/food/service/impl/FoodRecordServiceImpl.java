package com.knd.manage.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.food.vo.VoQueryFoodRecord;
import com.knd.manage.food.entity.FoodRecord;
import com.knd.manage.food.mapper.FoodRecordMapper;
import com.knd.manage.food.service.IFoodRecordService;
import com.knd.manage.food.vo.VoSaveFoodRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class FoodRecordServiceImpl extends ServiceImpl<FoodRecordMapper, FoodRecord> implements IFoodRecordService {


    //新增
    @Override
    public Result add( VoSaveFoodRecord vo) {
        //查重
        QueryWrapper<FoodRecord> qw = new QueryWrapper<>();
        qw.eq("id", vo.getId());
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }

        FoodRecord b = new FoodRecord();
        //新增
        BeanUtils.copyProperties(vo,b);
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
    public Result edit( VoSaveFoodRecord vo) {
        //根据id获取名称
        QueryWrapper<FoodRecord> qw = new QueryWrapper<>();
        qw.eq("id", vo.getId());
        qw.eq("deleted", "0");
        FoodRecord ta = baseMapper.selectOne(qw);
        if (ta == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        BeanUtils.copyProperties(vo,ta);
        ta.setLastModifiedBy(vo.getUserId());
        ta.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(ta);
        //成功
        return ResultUtil.success(ta);
    }

    //删除
    @Override
    public Result deleteFoodRecord(String userId, String id) {
        FoodRecord b = new FoodRecord();
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
    public Result getFoodRecord(String id) {
        QueryWrapper<FoodRecord> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", 0);
        FoodRecord b = baseMapper.selectOne(qw);
        //成功
        return ResultUtil.success(b);
    }

    //获取食物分类列表
    @Override
    public Result getFoodRecordList(VoQueryFoodRecord voQueryFoodRecord, String currentPage) {
        QueryWrapper<FoodRecord> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(voQueryFoodRecord.getDate())) {
            qw.eq("DATE_FORMAT(createDate,'%Y-%m-%d')", voQueryFoodRecord.getDate());
        }
        qw.eq("deleted", "0");
        //qw.eq("userId", voQueryFoodRecord.getUserId());
        qw.orderByAsc("lastModifiedDate");
        List<FoodRecord> list;
        Page<FoodRecord> page = new Page<>(Integer.valueOf(currentPage), PageInfo.pageSize);
        if (StringUtils.isEmpty(currentPage)) {
            //获取全部
            list = baseMapper.selectList(qw);
            page.setRecords(list);
            page.setTotal(list.size());
            page.setSize(list.size());
            page.setCurrent(Long.parseLong(currentPage));
        } else {
            //分页
            page = baseMapper.selectPage(page, qw);
        }
        //成功
        return ResultUtil.success(page);
    }

    @Override
    public FoodRecord insertReturnEntity(FoodRecord entity) {
        return null;
    }

    @Override
    public FoodRecord updateReturnEntity(FoodRecord entity) {
        return null;
    }
}