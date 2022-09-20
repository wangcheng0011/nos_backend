package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoSavePart {
    @NotBlank
    @Size(max = 32)
    private String part;
    @NotBlank
    @Size(max = 256)
    private String remark;
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;
    @Size(max = 64)
    private String partId;
    @Size(max = 64)
    private String userId;
}
