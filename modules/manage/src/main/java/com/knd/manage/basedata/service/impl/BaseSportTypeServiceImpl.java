package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.entity.BaseSportTypeEntity;
import com.knd.manage.basedata.mapper.BaseSportTypeMapper;
import com.knd.manage.basedata.service.BaseSportTypeService;
import com.knd.manage.basedata.vo.VoSaveSportType;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.user.entity.UserDetail;
import com.knd.manage.user.mapper.UserDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangcheng
 */
@Service
@RequiredArgsConstructor
public class BaseSportTypeServiceImpl implements BaseSportTypeService {

    private final BaseSportTypeMapper sportTypeMapper;
    private final UserDetailMapper userDetailMapper;

    @Override
    public Result add(VoSaveSportType vo) {
        //查重
        QueryWrapper<BaseSportTypeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("type",vo.getType());
        wrapper.eq("deleted", "0");
        //获取总数
        int s = sportTypeMapper.selectCount(wrapper);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        BaseSportTypeEntity entity = new BaseSportTypeEntity();
        entity.setId(UUIDUtil.getShortUUID());
        entity.setType(vo.getType());
        entity.setRemark(vo.getRemark());
        entity.setCreateBy(vo.getUserId());
        entity.setCreateDate(LocalDateTime.now());
        entity.setDeleted("0");
        entity.setLastModifiedBy(vo.getUserId());
        entity.setLastModifiedDate(LocalDateTime.now());
        sportTypeMapper.insert(entity);
        return ResultUtil.success();
    }

    @Override
    public Result edit(VoSaveSportType vo) {
        QueryWrapper<BaseSportTypeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getTypeId());
        wrapper.eq("deleted", "0");
        wrapper.select("type");
        BaseSportTypeEntity entity = sportTypeMapper.selectOne(wrapper);
        if (entity == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!entity.getType().equals(vo.getType())){
            //查重
            wrapper.clear();
            wrapper.eq("type", vo.getType());
            wrapper.eq("deleted", "0");
            //获取总数
            int s = sportTypeMapper.selectCount(wrapper);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        BaseSportTypeEntity b = new BaseSportTypeEntity();
        b.setId(vo.getTypeId());
        b.setType(vo.getType());
        b.setRemark(vo.getRemark());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        sportTypeMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result deleteSportType(String userId, String id) {
        int num = userDetailMapper.selectCount(new QueryWrapper<UserDetail>().eq("deleted", 0).eq("sportTypeId", id));

        if (num>0){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"有未删除得用户使用该运动频率");
        }
        BaseSportTypeEntity b = new BaseSportTypeEntity();
        b.setId(id);
        b.setDeleted("1");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        sportTypeMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result getSportType(String id) {
        QueryWrapper<BaseSportTypeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("deleted", "0");
        List<BaseSportTypeEntity> baseSportTypeEntity = sportTypeMapper.selectList(wrapper);
        //成功
        return ResultUtil.success(baseSportTypeEntity);
    }

    @Override
    public Result getSportTypeList(String type, String currentPage) {
        QueryWrapper<BaseSportTypeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted",0);
        if (StringUtils.isNotEmpty(type)){
            wrapper.eq("type",type);
        }
        List<BaseSportTypeEntity> list;
        long total;
        if (StringUtils.isNotEmpty(currentPage)){
            Page<BaseSportTypeEntity> page = new Page<>(Integer.valueOf(currentPage), PageInfo.pageSize);
            Page<BaseSportTypeEntity> pageList = sportTypeMapper.selectPage(page, wrapper);
            total = pageList.getTotal();
            list = pageList.getRecords();
        }else {
            list = sportTypeMapper.selectList(wrapper);
            total = list.size();
        }
        ResponseDto dto = ResponseDto.<BaseSportTypeEntity>builder().total((int)total).resList(list).build();
        return ResultUtil.success(dto);
    }
}
