package com.knd.manage.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminDto {
    private String id;
    private String userName;
    private String nickName;
    private String mobile;
    private String frozenFlag;
    private String areaId;
    private List<RoleDto> roleList;


}
