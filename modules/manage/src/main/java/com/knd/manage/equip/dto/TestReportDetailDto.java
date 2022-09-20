package com.knd.manage.equip.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TestReportDetailDto {

    //正常项目集合
    private List<String> normalList;
    //故障集合
    private List<TestReportFaultDto> faultList;
}
