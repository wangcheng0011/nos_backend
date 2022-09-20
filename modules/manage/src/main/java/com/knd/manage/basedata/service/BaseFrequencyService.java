package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.vo.VoSaveFrequency;

/**
 * @author zm
 */
public interface BaseFrequencyService {

    /**
     * 新增运动频率
     */
    Result add(VoSaveFrequency vo);

    /**
     * 更新运动频率
     * @param vo
     * @return
     */
    Result edit(VoSaveFrequency vo);

    /**
     * 删除运动频率
     * @param userId
     * @param id
     * @return
     */
    Result deleteFrequency(String userId, String id);

    /**
     * 获取运动频率
     * @param id
     * @return
     */
    Result getFrequency(String id);

    /**
     * 获取运动频率列表
     * @param frequency
     * @param currentPage
     * @return
     */
    Result getFrequencyList(String frequency, String currentPage);
}
