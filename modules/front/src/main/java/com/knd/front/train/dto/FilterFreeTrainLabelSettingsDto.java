package com.knd.front.train.dto;

import com.knd.front.home.dto.BaseBodyPartDto;
import com.knd.front.home.dto.BaseTargetDto;
import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/9
 * @Version 1.0
 */
@Data
public class FilterFreeTrainLabelSettingsDto {
    private List<BaseTargetDto> targetList;
    private List<BaseBodyPartDto> partList;
    private List<BaseEquipmentDto> equipmentList;
}