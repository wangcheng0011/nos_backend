package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.entity.BaseElement;
import com.knd.manage.basedata.vo.VoGetElementList;
import com.knd.manage.basedata.vo.VoSaveElement;
import com.knd.mybatis.SuperService;

public interface IBaseElementService extends SuperService<BaseElement> {

//    //获取元素列表
//    Result getElementList(VoGetElementList vo);

    //获取元素详情
    Result getElement(String id);

    //删除元素
    Result deleteElement(String userId, String id);

    //新增元素
    Result addElement(String userId, VoSaveElement vo);

    //更新元素
    Result editElement(String userId, VoSaveElement vo);
}
