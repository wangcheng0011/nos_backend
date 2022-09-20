package com.knd.manage.equip.dto;

import com.knd.manage.equip.entity.TestReportEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TestReportListDto {
    //总数
    private int total;
    //集合
    private List<TestReportEntity> testReportEntityList;
}
