package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoSaveFloor {
    private String userId;

    @NotBlank
    @Size(max = 64)
    private String keyValue;

    @NotBlank
    @Size(max = 64)
    private String version;


    @NotBlank
    private String floorType;

    @Size(max = 256)
    private String floorName;

    private String floorDetail;
    private String floorNote;

    private VoUrl imageUrl;

    private VoUrl backgroundUrl;

    private String platform;
    private String skipUrl;
    private String searchUrl;

    private VoUrl showUrl;
    private String description;

    @Size(max = 64)
    private String floorId;

    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;
}
