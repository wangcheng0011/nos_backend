package com.knd.manage.admin.dto;

import com.knd.manage.admin.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class RoleListDto {
    //总数
    private int total;
    //权限列表
    private List<Role> roleList;

}
