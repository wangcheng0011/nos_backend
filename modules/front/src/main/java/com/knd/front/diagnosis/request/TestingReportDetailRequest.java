package com.knd.front.diagnosis.request;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Data
public class TestingReportDetailRequest{
    @NotBlank(message = "故障代码")
    private String faultCode;
    @NotBlank(message = "检修项目内容不能为空")
    private String faultDesc;
    @NotBlank(message = "故障原因分析不能为空")
    private String causeAnalysis;
    @NotBlank(message = "方案不能为空")
    private String solution;
    @NotBlank(message = "检修状态不能为空 0正常 1故障")
    private String testingReportStatus;



}
