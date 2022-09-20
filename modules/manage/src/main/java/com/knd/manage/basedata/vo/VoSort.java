package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
/**
 * @author Lenovo
 */
@Data
public class VoSort {

    @NotBlank
    @Size(max = 2)
    private String sort;

    @NotBlank
    private String floorId;
}
