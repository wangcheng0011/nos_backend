package com.knd.manage.equip.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoSaveAgreement {
    //userId从token获取
    private String userId ;
    @NotBlank
    @Size(max = 32)
    private String agreementName;
    @NotBlank
    @Size(max = 65535)
    private String agreementContent;
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;
    @Size(max = 64)
    private String id;

}
