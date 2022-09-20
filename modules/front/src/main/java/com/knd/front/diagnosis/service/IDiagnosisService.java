package com.knd.front.diagnosis.service;

import com.knd.common.response.Result;
import com.knd.front.diagnosis.request.TestingReportRequest;
import com.knd.front.entity.TestingReportHead;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-01
 */
public interface IDiagnosisService extends SuperService<TestingReportHead> {
   Result testingReport(TestingReportRequest  testingReportRequest);
}
