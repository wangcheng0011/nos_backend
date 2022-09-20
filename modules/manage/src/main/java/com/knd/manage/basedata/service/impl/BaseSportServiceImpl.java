package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.entity.BaseSportEntity;
import com.knd.manage.basedata.mapper.BaseSportMapper;
import com.knd.manage.basedata.service.BaseSportService;
import com.knd.manage.basedata.vo.VoSaveSport;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.user.entity.UserDetail;
import com.knd.manage.user.mapper.UserDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class BaseSportServiceImpl implements BaseSportService {

    private final BaseSportMapper baseSportMapper;
    private final UserDetailMapper userDetailMapper;

    @Override
    public Result add(VoSaveSport vo) {
        //查重
        QueryWrapper<BaseSportEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("Sport",vo.getSport());
        wrapper.eq("deleted", "0");
        //获取总数
        int s = baseSportMapper.selectCount(wrapper);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        BaseSportEntity entity = new BaseSportEntity();
        entity.setId(UUIDUtil.getShortUUID());
        entity.setSport(vo.getSport());
        entity.setRemark(vo.getRemark());
        entity.setCreateBy(vo.getUserId());
        entity.setCreateDate(LocalDateTime.now());
        entity.setDeleted("0");
        entity.setLastModifiedBy(vo.getUserId());
        entity.setLastModifiedDate(LocalDateTime.now());
        baseSportMapper.insert(entity);
        return ResultUtil.success();
    }

    @Override
    public Result edit(VoSaveSport vo) {
        QueryWrapper<BaseSportEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getSportId());
        wrapper.eq("deleted", "0");
        wrapper.select("Sport");
        BaseSportEntity entity = baseSportMapper.selectOne(wrapper);
        if (entity == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!entity.getSport().equals(vo.getSport())){
            //查重
            wrapper.clear();
            wrapper.eq("Sport", vo.getSport());
            wrapper.eq("deleted", "0");
            //获取总数
            int s = baseSportMapper.selectCount(wrapper);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        BaseSportEntity b = new BaseSportEntity();
        b.setId(vo.getSportId());
        b.setSport(vo.getSport());
        b.setRemark(vo.getRemark());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        baseSportMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result deleteSport(String userId, String id) {
        int num = userDetailMapper.selectCount(new QueryWrapper<UserDetail>().eq("deleted", 0).eq("sportId", id));
        if (num>0){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"有未删除得用户使用该运动方式");
        }
        BaseSportEntity b = new BaseSportEntity();
        b.setId(id);
        b.setDeleted("1");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseSportMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result getSport(String id) {
        QueryWrapper<BaseSportEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("deleted", "0");
        List<BaseSportEntity> baseSportEntities = baseSportMapper.selectList(wrapper);
        //成功
        return ResultUtil.success(baseSportEntities);
    }

    @Override
    public Result getSportList(String sport, String currentPage) {
        QueryWrapper<BaseSportEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted",0);
        if (StringUtils.isNotEmpty(sport)){
            wrapper.eq("sport",sport);
        }
        List<BaseSportEntity> list;
        long total;
        if (StringUtils.isNotEmpty(currentPage)){
            Page<BaseSportEntity> page = new Page<>(Integer.valueOf(currentPage), PageInfo.pageSize);
            Page<BaseSportEntity> pageList = baseSportMapper.selectPage(page, wrapper);
            total = pageList.getTotal();
            list = pageList.getRecords();
        }else {
            list = baseSportMapper.selectList(wrapper);
            total = list.size();
        }
        ResponseDto dto = ResponseDto.<BaseSportEntity>builder().total((int)total).resList(list).build();
        return ResultUtil.success(dto);
    }
}
