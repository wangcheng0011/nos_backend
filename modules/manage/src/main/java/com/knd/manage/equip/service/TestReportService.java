package com.knd.manage.equip.service;

import com.knd.common.response.Result;

/**
 * 协议
 * @author zm
 */
public interface TestReportService {

    /**
     * 获取检修列表
     * @param equipmentNo
     * @param currentPage
     * @return
     */
    Result getTestReportList(String equipmentNo,String currentPage);

    /**
     * 获取检修详情
     * @param id
     * @return
     */
    Result getTestReportDetail(String id);

}
