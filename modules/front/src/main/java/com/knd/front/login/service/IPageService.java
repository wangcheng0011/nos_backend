package com.knd.front.login.service;

import com.knd.common.response.Result;

/**
 * @author Lenovo
 */
public interface IPageService {

    Result getPage(String key,String version,String platform);
}
