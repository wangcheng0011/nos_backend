package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoSaveHobby {
    @NotBlank
    @Size(max = 32)
    private String hobby;
    @NotBlank
    @Size(max = 256)
    private String remark;
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;
    @Size(max = 64)
    private String hobbyId;

    private String partId;

    private VoUrl selectUrlDto;

    private VoUrl unSelectUrlDto;
}
