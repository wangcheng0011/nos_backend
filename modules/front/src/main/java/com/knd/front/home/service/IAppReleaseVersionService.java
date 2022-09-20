package com.knd.front.home.service;

import com.knd.common.response.Result;
import com.knd.front.home.dto.GetAppReleaseVersionDto;
import com.knd.front.entity.AppReleaseVersion;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
public interface IAppReleaseVersionService extends SuperService<AppReleaseVersion> {
    Result getAppReleaseVersion(String currentAppVersion,String currentFirmwareVersion,String currentSystemVersion);
}
