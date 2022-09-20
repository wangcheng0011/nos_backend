package com.knd.manage.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminListDto {

    private int total;
    private List<MAdminDto> adminList;



}
