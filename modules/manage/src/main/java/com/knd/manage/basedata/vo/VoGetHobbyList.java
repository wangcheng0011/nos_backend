package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoGetHobbyList {
    @Size(max = 32)
    private String hobby;
    private String current;
}
