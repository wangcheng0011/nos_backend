package com.knd.manage.course.vo;

import com.knd.manage.basedata.vo.VoUrl;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class VoSaveSeries {

    private String userId;

    @NotBlank
    @Size(max = 64)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String synopsis;

    @NotBlank
    @Size(max = 200)
    private String introduce;

    @NotBlank
    @Size(max = 64)
    private String difficulty;

    private String consume;

    private VoUrl picAttachUrl;
    private List<VoUrl> attachUrl;
    private List<String> courseIdList;

    @Size(max = 64)
    private String seriesId;

    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;
}
