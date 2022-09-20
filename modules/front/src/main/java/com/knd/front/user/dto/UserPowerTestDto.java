package com.knd.front.user.dto;


import com.knd.front.entity.BasePowerStandardUse;
import lombok.Data;


/**
 * @author will
 */
@Data
public class UserPowerTestDto {
    private UserTestActionDto userTestActionDto;
    private BasePowerStandardUse basePowerStandardUse;
}