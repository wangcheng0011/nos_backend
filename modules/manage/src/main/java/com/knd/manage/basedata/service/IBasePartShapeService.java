package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.entity.BasePartShape;
import com.knd.manage.basedata.vo.VoSaveShape;
import com.knd.mybatis.SuperService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface IBasePartShapeService extends SuperService<BasePartShape> {
    //新增部位
    Result add(VoSaveShape vo);

    //更新部位
    Result edit(VoSaveShape vo);

    //删除部位
    Result deleteShape(String userId, String id);

    //获取部位
    Result GetShape(String id);

    //获取部位列表
    Result getShapeList(String shape, String currentPage);

}
