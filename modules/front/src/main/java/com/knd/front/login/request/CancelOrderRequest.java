package com.knd.front.login.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CancelOrderRequest {
    @NotBlank
    private String id;
    @NotBlank
    @Pattern(regexp = "^(1|2|3|4|5)$")
    private String status;
}
