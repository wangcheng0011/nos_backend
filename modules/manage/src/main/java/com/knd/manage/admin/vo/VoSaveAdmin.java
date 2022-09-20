package com.knd.manage.admin.vo;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class VoSaveAdmin {
    private String userId;
    @NotBlank
    @Size(max = 20)
    private String userName;
    @Size(max = 32)
    private String password;
    @NotBlank
    @Size(max = 8)
    private String nickName;
    @NotBlank
    @Size(max = 64)
    private String mobile;
    private List<VoRole> roleList;
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;
    @Size(max = 64)
    private String adminId;

    private String areaId;


}
