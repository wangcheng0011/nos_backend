package com.knd.manage.equip.dto;

import com.knd.manage.equip.entity.EquipmentReportInfo;
import lombok.Data;

import java.util.List;

@Data
public class ReportInfoListDto {
    private int total;
    private List<EquipmentReportInfo> reportInfoList;

}
