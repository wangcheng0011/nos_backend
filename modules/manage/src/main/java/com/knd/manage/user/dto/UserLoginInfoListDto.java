package com.knd.manage.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserLoginInfoListDto {
    //总数
    private int total;
    //集合
    private List<LoginInfoDto> loginInfoList;
}
