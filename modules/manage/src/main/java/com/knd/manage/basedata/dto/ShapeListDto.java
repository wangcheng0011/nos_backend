package com.knd.manage.basedata.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
@Builder
public class ShapeListDto {
    //总数
    private int total;
    //列表
    private List<PartShapeDto> shapeList;
}
