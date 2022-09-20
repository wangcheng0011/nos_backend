package com.knd.front.home.service;

import com.knd.common.response.Result;
import com.knd.front.entity.Agreement;
import com.knd.mybatis.SuperService;


/**
 * 协议
 * @author will
 */
public interface IAgreementService extends SuperService<Agreement> {


    //获取协议
    Result getAgreement(String agreementName);


}
