package com.knd.manage.admin.dto;

import lombok.Data;

@Data
public class ButtonDto {
    //按钮id对应的数据id
    private String id;
    //按钮名称
    private String buttonName;
    //按钮id
    private String buttonId;
    //权限flag,0是没有，1是有
    private String powerFlag;
}
