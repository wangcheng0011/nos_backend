package com.knd.front.train.dto;

import com.knd.front.home.dto.BaseTargetDto;
import lombok.Data;

import java.util.List;

@Data
public class ActionArrayTrainDetailDto {
    private List<FreeTrainDetailDto> freeTrainDetailDtoList;
}