package com.knd.auth.entity;

import lombok.Data;

import java.util.List;

@Data
public class RoleInfo {
    private String id;
    private String name;
    private String describe;
    private String status;
    private String creatorId;
    private String createTime;
    private String deleted;
    private List<PermissionInfo> permissions;
}
