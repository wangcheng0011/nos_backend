package com.knd.manage.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class PowerListDto {
    //权限列表
    private List<MpowerDto> powerList;
}
