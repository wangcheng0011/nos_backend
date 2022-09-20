package com.knd.manage.basedata.dto;

import com.knd.manage.basedata.entity.BaseBodyPart;
import com.knd.manage.basedata.entity.BaseEquipment;
import com.knd.manage.basedata.entity.GoodEquipment;
import lombok.Data;

import java.util.List;

@Data
public class EquipmentListDto {
    //总数
    private int total;
    //列表
    private List<BaseEquipment> equipmentList;
//    private List<GoodEquipment> equipmentList;
}
