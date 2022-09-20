package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.vo.VoGetLabelList;
import com.knd.manage.basedata.vo.VoSaveLabel;

/**
 * 标签
 */
public interface IBaseLabelService{

    //获取标签列表
    Result getLabelList(VoGetLabelList vo);

    //获取标签
    Result getLabel(String id);

    //新增标签
    Result add(String userId, VoSaveLabel vo);

    //更新标签
    Result edit(String userId, VoSaveLabel vo);

    //删除标签
    Result delete(String userId, String id);

}
