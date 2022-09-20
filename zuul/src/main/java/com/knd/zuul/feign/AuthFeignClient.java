package com.knd.zuul.feign;

import com.knd.zuul.dto.EquipmentInfo;
import com.knd.zuul.dto.PassUri;
import com.knd.zuul.dto.Token;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value = "auth")
public interface AuthFeignClient {

    @GetMapping("/queryToken")
    public Token queryToken(@RequestParam("token") String token);

    @GetMapping("/queryPassList")
    public List<PassUri> queryPassList();

    @GetMapping("/queryUserState")
    public String queryUserState(@RequestParam("userId") String userId);

    @GetMapping("/queryEquipmentNo")
    public EquipmentInfo queryEquipmentNo(@RequestParam("equipmentNo") String equipmentNo);

}
