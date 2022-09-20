package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.entity.BaseFloor;
import com.knd.manage.basedata.vo.VoGetFloorList;
import com.knd.manage.basedata.vo.VoSaveFloor;
import com.knd.mybatis.SuperService;

public interface IbaseFloorService extends SuperService<BaseFloor> {

    //获取楼层列表
    Result getFloorList(VoGetFloorList vo);

    //获取楼层详情
    Result getFloor(String id);

    //删除楼层
    Result deleteFloor(String userId, String id);

    //新增楼层
    Result addFloor(String userId, VoSaveFloor vo);

    //更新楼层
    Result editFloor(String userId, VoSaveFloor vo);


}
