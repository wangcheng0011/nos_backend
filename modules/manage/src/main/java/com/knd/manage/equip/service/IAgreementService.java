package com.knd.manage.equip.service;

import com.knd.common.response.Result;
import com.knd.manage.equip.entity.Agreement;
import com.knd.manage.equip.entity.EquipmentInfo;
import com.knd.mybatis.SuperService;

import java.io.InputStream;


/**
 * 协议
 * @author will
 */
public interface IAgreementService extends SuperService<Agreement> {

    //新增协议
    Result add(String userId, String agreementName, String agreementContent);

    //更新协议
    Result edit(String userId, String agreementName, String agreementContent, String id);

    //删除协议
    Result delete(String userId, String id);

    //获取协议
    Result getAgreement(String agreementName);

    //获取协议列表
    Result getAgreementList(String agreementName, String currentPage);

}
