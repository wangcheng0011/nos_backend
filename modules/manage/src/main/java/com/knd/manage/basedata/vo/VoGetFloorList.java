package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoGetFloorList {
    @Size(max = 64)
    private String keyValue;

    @Size(max = 64)
//    @NotBlank
    private String floorType;

    @NotBlank
    private String current;
}
