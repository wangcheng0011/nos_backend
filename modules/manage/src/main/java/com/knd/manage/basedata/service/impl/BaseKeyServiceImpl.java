package com.knd.manage.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.entity.BaseKeyEntity;
import com.knd.manage.basedata.entity.BasePage;
import com.knd.manage.basedata.mapper.BaseKeyMapper;
import com.knd.manage.basedata.mapper.BasePageMapper;
import com.knd.manage.basedata.service.BaseKeyService;
import com.knd.manage.basedata.vo.VoSaveKey;
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
public class BaseKeyServiceImpl implements BaseKeyService {

    private final BaseKeyMapper baseKeyMapper;
    private final BasePageMapper basePageMapper;

    @Override
    public Result add(VoSaveKey vo) {
        //查重
        QueryWrapper<BaseKeyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("keyValue",vo.getKeyValue());
        wrapper.eq("deleted", "0");
        //获取总数
        int s = baseKeyMapper.selectCount(wrapper);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        BaseKeyEntity entity = new BaseKeyEntity();
        entity.setId(UUIDUtil.getShortUUID());
        entity.setKeyValue(vo.getKeyValue());
        entity.setRemark(vo.getRemark());
        entity.setCreateBy(vo.getUserId());
        entity.setCreateDate(LocalDateTime.now());
        entity.setDeleted("0");
        entity.setLastModifiedBy(vo.getUserId());
        entity.setLastModifiedDate(LocalDateTime.now());
        baseKeyMapper.insert(entity);
        return ResultUtil.success();
    }

    @Override
    public Result edit(VoSaveKey vo) {
        QueryWrapper<BaseKeyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id",vo.getKeyId());
        wrapper.eq("deleted", "0");
        wrapper.select("keyValue");
        BaseKeyEntity entity = baseKeyMapper.selectOne(wrapper);
        if (entity == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!entity.getKeyValue().equals(vo.getKeyValue())){
            //查重
            wrapper.clear();
            wrapper.eq("keyValue", vo.getKeyValue());
            wrapper.eq("deleted", "0");
            //获取总数
            int s = baseKeyMapper.selectCount(wrapper);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        BaseKeyEntity b = new BaseKeyEntity();
        b.setId(vo.getKeyId());
        b.setKeyValue(vo.getKeyValue());
        b.setRemark(vo.getRemark());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        baseKeyMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result deleteKey(String userId, String id) {
        BaseKeyEntity baseKeyEntity = baseKeyMapper.selectById(id);
        if (StringUtils.isNotEmpty(baseKeyEntity)){
            int num = basePageMapper.selectCount(new QueryWrapper<BasePage>().eq("deleted", 0).eq("keyValue", baseKeyEntity.getKeyValue()));
            if (num>0){
                return ResultUtil.error(ResultEnum.FAIL.getCode(),"有未删除得页面使用该key");
            }
            BaseKeyEntity b = new BaseKeyEntity();
            b.setId(id);
            b.setDeleted("1");
            b.setLastModifiedBy(userId);
            b.setLastModifiedDate(LocalDateTime.now());
            baseKeyMapper.updateById(b);
        }
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result getKey(String id) {
        QueryWrapper<BaseKeyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("deleted", "0");
        List<BaseKeyEntity> baseKeyEntities = baseKeyMapper.selectList(wrapper);
        //成功
        return ResultUtil.success(baseKeyEntities);
    }

    @Override
    public Result getKeyList(String keyValue, String currentPage) {
        QueryWrapper<BaseKeyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted",0);
        if (StringUtils.isNotEmpty(keyValue)){
            wrapper.eq("keyValue",keyValue);
        }
        List<BaseKeyEntity> list;
        long total;
        if (StringUtils.isNotEmpty(currentPage)){
            Page<BaseKeyEntity> page = new Page<>(Integer.valueOf(currentPage), PageInfo.pageSize);
            Page<BaseKeyEntity> pageList = baseKeyMapper.selectPage(page, wrapper);
            total = pageList.getTotal();
            list = pageList.getRecords();
        }else {
            list = baseKeyMapper.selectList(wrapper);
            total = list.size();
        }
        ResponseDto dto = ResponseDto.<BaseKeyEntity>builder().total((int)total).resList(list).build();
        return ResultUtil.success(dto);
    }
}
