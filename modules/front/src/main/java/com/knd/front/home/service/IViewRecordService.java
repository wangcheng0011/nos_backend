package com.knd.front.home.service;

import com.knd.common.response.Result;
import com.knd.front.entity.Agreement;
import com.knd.front.entity.ViewRecordEntity;
import com.knd.mybatis.SuperService;


/**
 * 浏览类型
 * @author will
 */
public interface IViewRecordService extends SuperService<ViewRecordEntity> {


    /**
     * 新增浏览记录
     * @author will
     * @date 2021/8/31 16:32
     * @param type 参考ViewTypeEnum枚举类
     * @param itemId
     */
    void addViewRecord(Integer type,String itemId);


}
