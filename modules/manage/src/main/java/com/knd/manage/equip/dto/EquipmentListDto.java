package com.knd.manage.equip.dto;


import com.knd.manage.equip.entity.EquipmentInfo;
import lombok.Data;

import java.util.List;

@Data
public class EquipmentListDto {
    private int total;
    private List<EquipmentInfo> equipmentList;

}
