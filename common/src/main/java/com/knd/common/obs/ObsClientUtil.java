package com.knd.common.obs;

import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;

public class ObsClientUtil {
    /**
     * 创建/连接客户端
     */
    public static ObsClient createObsClient(String ak, String sk, String endPoint) {
        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);
        return new ObsClient(ak, sk, config);
    }

}
