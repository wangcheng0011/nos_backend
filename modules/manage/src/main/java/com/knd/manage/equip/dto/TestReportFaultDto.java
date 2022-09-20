package com.knd.manage.equip.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestReportFaultDto {

    private String faultDesc;
    private String faultCode;
    private String causeAnalysis;
    private String solution;
}
