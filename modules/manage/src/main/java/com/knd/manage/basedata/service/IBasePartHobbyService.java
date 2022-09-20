package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.entity.BasePartHobby;
import com.knd.manage.basedata.vo.VoSaveHobby;
import com.knd.mybatis.SuperService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface IBasePartHobbyService extends SuperService<BasePartHobby> {
    //新增爱好
    Result add(String userId, VoSaveHobby vo);

    //更新爱好
    Result edit(String userId, VoSaveHobby vo);

    //删除爱好
    Result deleteHobby(String userId, String id);

    //获取爱好
    Result getHobby(String id);

    //获取爱好列表
    Result getHobbyList(String hobby, String currentPage);

}
