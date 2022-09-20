package com.knd.front.config.qiniu;

import com.knd.common.qiniu.Auth;
import com.knd.common.qiniu.RtcRoomManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author will
 * @date 2021年05月19日 15:50
 */
@Configuration
public class QiniuConfig {
    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Bean
    public Auth auth(){
        return Auth.create(accessKey,secretKey);
    }

    @Bean
    public RtcRoomManager rtcRoomManager(Auth auth){
        return new RtcRoomManager(auth);
    }
}
