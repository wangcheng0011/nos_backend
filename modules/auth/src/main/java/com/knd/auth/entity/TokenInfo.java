package com.knd.auth.entity;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TokenInfo {
     private String id;
     private String name;
     private String username;
     private String password;
     private String avatar;
     private String status;
     private String telephone;
     private String lastLoginIp;
     private String creatorId;
     private LocalDateTime createTime;
     private String deleted;
     private String roleId;
     private List<RoleInfo> role;









}
