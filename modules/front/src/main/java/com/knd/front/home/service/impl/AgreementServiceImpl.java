package com.knd.front.home.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.Agreement;
import com.knd.front.home.mapper.AgreementMapper;
import com.knd.front.home.service.IAgreementService;
import org.springframework.stereotype.Service;

/**
 * 协议
 * @author will
 */
@Service
public class AgreementServiceImpl extends ServiceImpl<AgreementMapper, Agreement> implements IAgreementService {



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
    public Agreement insertReturnEntity(Agreement entity) {
        return null;
    }

    @Override
    public Agreement updateReturnEntity(Agreement entity) {
        return null;
    }
}
