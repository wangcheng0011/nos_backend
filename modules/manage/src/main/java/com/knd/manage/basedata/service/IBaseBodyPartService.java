package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.entity.BaseBodyPart;
import com.knd.mybatis.SuperService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface IBaseBodyPartService extends SuperService<BaseBodyPart> {
    //新增部位
    Result add(String userId, String part, String remark);

    //更新部位
    Result edit(String userId, String part, String remark, String partId);

    //删除部位
    Result deletePart(String userId, String id);

    //获取部位
    Result GetPart(String id);

    //获取部位列表
    Result getPartList(String part, String currentPage);

}
