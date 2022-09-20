package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoGetShapeList {
    @Size(max = 32)
    private String shape;
    private String current;
}
