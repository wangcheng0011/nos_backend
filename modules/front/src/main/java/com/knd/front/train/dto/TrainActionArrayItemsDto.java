package com.knd.front.train.dto;

import com.knd.front.entity.TrainActionArrayAction;
import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/3
 * @Version 1.0
 */
@Data
public class TrainActionArrayItemsDto {
    private String actionArraySetNum;
    private List<TrainActionArrayActionDto> trainActionArrayActionDtoList;
}