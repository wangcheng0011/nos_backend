package com.knd.manage.basedata.vo;

import com.knd.common.page.PageInfo;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoGetPartList {
    @Size(max = 32)
    private String part;
    private String current;
}
