package com.knd.manage.equip.vo;

import com.knd.common.userutil.UserUtils;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class VoSaveReportInfo {
    //userId从token获取
    private String userId ;
    @NotBlank
    @Size(max = 64)
    private String equipmentNo;
    @NotBlank
    @Size(max = 32)
    private String turnOnTime;
    @NotBlank
    @Size(max = 32)
    private String hardVersion;
    @NotBlank
    @Size(max = 32)
    private String mainboardVersion;
    @NotBlank
    @Size(max = 128)
    private String positionInfo;
    @NotBlank
    @Size(max = 32)
    private String appVersion;
}
