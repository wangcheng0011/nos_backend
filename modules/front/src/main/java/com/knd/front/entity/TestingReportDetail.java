package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("testing_report_detail")
@ApiModel(value="TestingReportDetail", description="")
public class TestingReportDetail extends BaseEntity {

    private static final long serialVersionUID=1L;
    private String testingReportHeadId;
    private String faultDesc;
    private String testingReportStatus;
    private String faultCode;
    private String causeAnalysis;
    private String solution;


}
