package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.entity.BaseFrequencyEntity;
import com.knd.manage.basedata.mapper.BaseFrequencyMapper;
import com.knd.manage.basedata.service.BaseFrequencyService;
import com.knd.manage.basedata.vo.VoSaveFrequency;
import com.knd.manage.common.dto.ResponseDto;
import com.knd.manage.user.entity.UserDetail;
import com.knd.manage.user.mapper.UserDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zm
 */
@Service
@RequiredArgsConstructor
public class BaseFrequencyServiceImpl implements BaseFrequencyService {

    private final BaseFrequencyMapper frequencyMapper;
    private final UserDetailMapper userDetailMapper;

    @Override
    public Result add(VoSaveFrequency vo) {
        //查重
        QueryWrapper<BaseFrequencyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("frequency",vo.getFrequency());
        wrapper.eq("deleted", "0");
        //获取总数
        int s = frequencyMapper.selectCount(wrapper);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        BaseFrequencyEntity entity = new BaseFrequencyEntity();
        entity.setId(UUIDUtil.getShortUUID());
        entity.setFrequency(vo.getFrequency());
        entity.setRemark(vo.getRemark());
        entity.setCreateBy(vo.getUserId());
        entity.setCreateDate(LocalDateTime.now());
        entity.setDeleted("0");
        entity.setLastModifiedBy(vo.getUserId());
        entity.setLastModifiedDate(LocalDateTime.now());
        frequencyMapper.insert(entity);
        return ResultUtil.success();
    }

    @Override
    public Result edit(VoSaveFrequency vo) {
        QueryWrapper<BaseFrequencyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getFrequencyId());
        wrapper.eq("deleted", "0");
        wrapper.select("frequency");
        BaseFrequencyEntity entity = frequencyMapper.selectOne(wrapper);
        if (entity == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!entity.getFrequency().equals(vo.getFrequency())){
            //查重
            wrapper.clear();
            wrapper.eq("frequency", vo.getFrequency());
            wrapper.eq("deleted", "0");
            //获取总数
            int s = frequencyMapper.selectCount(wrapper);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        BaseFrequencyEntity b = new BaseFrequencyEntity();
        b.setId(vo.getFrequencyId());
        b.setFrequency(vo.getFrequency());
        b.setRemark(vo.getRemark());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        frequencyMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result deleteFrequency(String userId, String id) {
        int num = userDetailMapper.selectCount(new QueryWrapper<UserDetail>().eq("deleted", 0).eq("frequencyId", id));
        if (num>0){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"有未删除得用户使用该运动频率");
        }
        BaseFrequencyEntity b = new BaseFrequencyEntity();
        b.setId(id);
        b.setDeleted("1");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        frequencyMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result getFrequency(String id) {
        QueryWrapper<BaseFrequencyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("deleted", "0");
        List<BaseFrequencyEntity> baseFrequencyEntities = frequencyMapper.selectList(wrapper);
        //成功
        return ResultUtil.success(baseFrequencyEntities);
    }

    @Override
    public Result getFrequencyList(String frequency, String currentPage) {
        QueryWrapper<BaseFrequencyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted",0);
        if (StringUtils.isNotEmpty(frequency)){
            wrapper.eq("frequency",frequency);
        }
        wrapper.orderByDesc("length(frequency)","frequency");
        List<BaseFrequencyEntity> list;
        long total;
        if (StringUtils.isNotEmpty(currentPage)){
            Page<BaseFrequencyEntity> page = new Page<>(Integer.valueOf(currentPage), PageInfo.pageSize);
            Page<BaseFrequencyEntity> pageList = frequencyMapper.selectPage(page, wrapper);
            total = pageList.getTotal();
            list = pageList.getRecords();
        }else {
            list = frequencyMapper.selectList(wrapper);
            total = list.size();
        }
        ResponseDto dto = ResponseDto.<BaseFrequencyEntity>builder().total((int)total).resList(list).build();
        return ResultUtil.success(dto);
    }
}
