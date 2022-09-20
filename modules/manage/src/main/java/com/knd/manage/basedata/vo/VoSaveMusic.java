package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author zm
 */
@Data
public class VoSaveMusic {
    private String userId;

    @NotBlank
    @Size(max = 32)
    private String name;

    @NotBlank
    @Size(max = 32)
    private String type;

    @NotBlank
    @Size(max = 256)
    private String remark;

    private VoUrl musicUrl;
}
