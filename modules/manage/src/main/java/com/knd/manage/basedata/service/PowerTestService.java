package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.vo.VoSavePowerTest;
import com.knd.manage.common.vo.VoId;

/**
 * @author zm
 * 力量测试维护接口
 */
public interface PowerTestService {

    /**
     * 获取力量测试列表
     * @return
     */
    Result getPowerTestList(String gender,String difficultyId, String current);

    /**
     * 获取单个力量测试详情
     * @param id
     * @return
     */
    Result getPowerTest(String id);

    /**
     * 新增力量测试
     * @param vo
     * @return
     */
    Result addPowerTest(VoSavePowerTest vo);

    /**
     * 更新力量测试
     * @param vo
     * @return
     */
    Result editPowerTest(VoSavePowerTest vo);

    /**
     * 删除力量测试
     * @param vo
     * @return
     */
    Result deletePowerTest(VoId vo);
}
