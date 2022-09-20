package com.knd.front.user.dto;

import com.knd.front.user.request.UserActionPowerTestRequest;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * @author will
 */
@Data
public class UserActionPowerTestDto{

    @NotEmpty(message = "测试列表不能为空")
    @Valid
   private List<UserActionPowerTestRequest> userActionPowerTestRequests;

}
