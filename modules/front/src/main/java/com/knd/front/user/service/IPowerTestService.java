package com.knd.front.user.service;

import com.knd.common.response.Result;
import com.knd.front.user.dto.PowerTestDto;
import com.knd.front.user.entity.PowerTestEntity;
import com.knd.front.user.request.PowerTestResultRequest;
import com.knd.mybatis.SuperService;

import java.util.List;

/**
 *
 * @author will
 * @date 2021/8/11 11:12
 */
public interface IPowerTestService extends SuperService<PowerTestEntity> {

   public List<PowerTestDto> getPowerTest();

   /**
    * 保存力量测试结果
    * @param request
    * @return
    */
   Result addTestResult(PowerTestResultRequest request);
}
