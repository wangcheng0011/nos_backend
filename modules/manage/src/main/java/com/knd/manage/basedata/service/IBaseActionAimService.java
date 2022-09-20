package com.knd.manage.basedata.service;

import com.knd.manage.basedata.dto.AimSettingDto;
import com.knd.manage.basedata.vo.VoAimSetting;
import com.knd.manage.basedata.entity.BaseActionAim;
import com.knd.mybatis.SuperService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface IBaseActionAimService extends SuperService<BaseActionAim> {

    //新增动作目标信息
    void add(String userId, String actionId,List<VoAimSetting> voAimSettingList);

    //遍历更新 到 动作目标信息基础表
    void edit(String userId, String actionId, List<VoAimSetting> voAimSettingList);

    //删除
    void delete(String userId, String actionId);

    //根据动作id获取列表
    List<AimSettingDto> getList(String actionId);


}
