package com.knd.manage.course.vo;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoGetCoachList {

    @Size(max = 32)
    private String name;
    private String current;
}
