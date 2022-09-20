package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.vo.VoSaveKey;

/**
 * @author zm
 */
public interface BaseKeyService {

    /**
     * 新增Key
     */
    Result add(VoSaveKey vo);

    /**
     * 更新Key
     * @param vo
     * @return
     */
    Result edit(VoSaveKey vo);

    /**
     * 删除Key
     * @param userId
     * @param id
     * @return
     */
    Result deleteKey(String userId, String id);

    /**
     * 获取Key
     * @param id
     * @return
     */
    Result getKey(String id);

    /**
     * 获取Key列表
     * @param keyValue
     * @param currentPage
     * @return
     */
    Result getKeyList(String keyValue, String currentPage);
}
