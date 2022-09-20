package com.knd.auth.entity;

import lombok.Data;

import java.util.List;
@Data
public class PermissionInfo {
    private String roleId;
    private String permissionId;
    private String permissionnName;
    private List<ActionEntity> ActionEntitySet;









}
