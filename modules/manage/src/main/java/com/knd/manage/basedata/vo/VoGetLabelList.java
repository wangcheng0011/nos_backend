package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoGetLabelList {
    @Size(max = 32)
    private String label;
    private String type;
    private String current;
}
