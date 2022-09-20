package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.vo.VoSaveSportType;

/**
 * @author zm
 */
public interface BaseSportTypeService {

    /**
     * 新增运动类型
     */
    Result add(VoSaveSportType vo);

    /**
     * 更新运动类型
     * @param vo
     * @return
     */
    Result edit(VoSaveSportType vo);

    /**
     * 删除运动类型
     * @param userId
     * @param id
     * @return
     */
    Result deleteSportType(String userId, String id);

    /**
     * 获取运动类型
     * @param id
     * @return
     */
    Result getSportType(String id);

    /**
     * 获取运动类型列表
     * @param type
     * @param currentPage
     * @return
     */
    Result getSportTypeList(String type, String currentPage);
}
