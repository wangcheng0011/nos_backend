package com.knd.manage.equip.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("testing_report_detail")
@ApiModel(value="TestingReportDetail", description="")
public class TestingReportDetail extends BaseEntity {

    private static final long serialVersionUID=1L;
//    private String testingReportHeadId;
    private String faultDesc;
    private String testingReportStatus;
    private String faultCode;
    private String causeAnalysis;
    private String solution;


}
