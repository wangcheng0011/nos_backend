package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoGetMusicList {
    @Size(max = 32)
    private String name;

    @NotBlank
    private String current;

    @NotBlank
    private String size;
}
