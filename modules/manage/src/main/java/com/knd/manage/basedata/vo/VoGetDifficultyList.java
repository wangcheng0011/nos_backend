package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoGetDifficultyList {
    @Size(max = 32)
    private String difficulty;
    private String type;
    private String current;
}
