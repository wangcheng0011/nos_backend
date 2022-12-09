package com.knd.manage.equip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.equip.dto.AgreementListDto;
import com.knd.manage.equip.entity.Agreement;
import com.knd.manage.equip.mapper.AgreementMapper;
import com.knd.manage.equip.service.IAgreementService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 协议
 * @author will
 */
@Service
public class AgreementServiceImpl extends ServiceImpl<AgreementMapper, Agreement> implements IAgreementService {

    @Override
    public Result add(String userId, String agreementName, String agreementContent) {
        //查重
        QueryWrapper<Agreement> qw = new QueryWrapper<>();
        qw.eq("agreementName", agreementName);
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        Agreement e = new Agreement();
        e.setId(UUIDUtil.getShortUUID());
        e.setAgreementName(agreementName);
        e.setAgreementContent(agreementContent);
        e.setCreateBy(userId);
        e.setCreateDate(LocalDateTime.now());
        e.setLastModifiedBy(userId);
        e.setLastModifiedDate(LocalDateTime.now());
        e.setDeleted("0");
        baseMapper.insert(e);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result edit(String userId, String agreementName, String agreementContent, String id) {
        //根据id获取名称
        QueryWrapper<Agreement> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("deleted", "0");
       // qw.eq("agreementName", agreementName);
        qw.select("agreementName");
        Agreement eq = baseMapper.selectOne(qw);
        if (eq ==null){
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!eq.getAgreementName().equals(agreementName)) {
            //查重
            qw.clear();
            qw.eq("agreementName", agreementName);
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        //
        Agreement e = new Agreement();
        e.setId(id);
        e.setAgreementName(agreementName);
        e.setAgreementContent(agreementContent);
        e.setLastModifiedBy(userId);
        e.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(e);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result delete(String userId, String id) {
        Agreement b = new Agreement();
        b.setId(id);
        b.setDeleted("1");
        b.setLastModifiedBy(userId);
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result getAgreement(String agreementName) {
        QueryWrapper<Agreement> qw = new QueryWrapper<>();
        qw.eq("agreementName", agreementName);
        qw.select("id", "agreementName", "agreementContent");
        qw.eq("deleted", "0");
        Agreement b = baseMapper.selectOne(qw);
        //成功
        return ResultUtil.success(b);
    }

    @Override
    public Result getAgreementList(String agreementName, String currentPage) {
        QueryWrapper<Agreement> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(agreementName)) {
            qw.like("agreementName", agreementName);
        }
        qw.select("id", "agreementName", "agreementContent","createDate");
        qw.eq("deleted", "0");
        qw.orderByAsc("length(agreementName)","agreementName");
        List<Agreement> agreementList;
        AgreementListDto agreementListDto = new AgreementListDto();
        if (StringUtils.isEmpty(currentPage)) {
            //获取全部
            agreementList = baseMapper.selectList(qw);
            agreementListDto.setTotal(agreementList.size());
        } else {
            //分页
            Page<Agreement> partPage = new Page<>(Integer.parseInt(currentPage), PageInfo.pageSize);
            partPage = baseMapper.selectPage(partPage, qw);
            agreementList = partPage.getRecords();
            agreementListDto.setTotal((int)partPage.getTotal());
        }
        agreementListDto.setAgreementList(agreementList);
        //成功
        return ResultUtil.success(agreementListDto);
    }

    @Override
    public Agreement insertReturnEntity(Agreement entity) {
        return null;
    }

    @Override
    public Agreement updateReturnEntity(Agreement entity) {
        return null;
    }
}
