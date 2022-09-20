package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.vo.VoSaveSport;

/**
 * @author zm
 */
public interface BaseSportService {

    /**
     * 新增运动方式
     */
    Result add(VoSaveSport vo);

    /**
     * 更新运动方式
     * @param vo
     * @return
     */
    Result edit(VoSaveSport vo);

    /**
     * 删除运动方式
     * @param userId
     * @param id
     * @return
     */
    Result deleteSport(String userId, String id);

    /**
     * 获取运动方式
     * @param id
     * @return
     */
    Result getSport(String id);

    /**
     * 获取运动方式列表
     * @param sport
     * @param currentPage
     * @return
     */
    Result getSportList(String sport, String currentPage);
}
