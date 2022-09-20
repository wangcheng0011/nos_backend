package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.entity.BaseDifficulty;
import com.knd.manage.basedata.vo.VoGetDifficultyList;
import com.knd.manage.basedata.vo.VoSaveDifficulty;
import com.knd.mybatis.SuperService;

public interface IBaseDifficultyService extends SuperService<BaseDifficulty> {

    //获取难度列表
    Result getDifficultyList(VoGetDifficultyList vo);

    //获取难度
    Result getDifficulty(String id);

    //新增难度
    Result add(String userId, VoSaveDifficulty vo);

    //更新难度
    Result edit(String userId, VoSaveDifficulty vo);

    //删除难度
    Result delete(String userId, String id);

}
