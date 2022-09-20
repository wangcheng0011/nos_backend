package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoGetElementList {
    @Size(max = 64)
    private String keyValue;

    @NotBlank
    private String floorId;

    @NotBlank
    private String current;
}
