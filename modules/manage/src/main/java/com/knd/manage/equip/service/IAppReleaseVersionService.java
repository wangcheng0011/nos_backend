package com.knd.manage.equip.service;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.equip.entity.AppReleaseVersion;
import com.knd.manage.equip.vo.VoGetApkInfoList;
import com.knd.manage.equip.vo.VoSaveApkInfo;
import com.knd.mybatis.SuperService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-09
 */
public interface IAppReleaseVersionService extends SuperService<AppReleaseVersion> {
    //新增apk版本信息
    Result add(VoSaveApkInfo vo);

    //更新apk版本信息
    Result edit(VoSaveApkInfo vo);

    //获取apk版本信息
    Result getApkInfo(String id);

    //删除apk版本信息
    Result deleteApkInfo(VoId vo);

    //获取apk版本信息列表
    Result getApkInfoList(VoGetApkInfoList vo);

    //修改apk版本发布状态
    Result saveReleaseStatus(String userId, String id,String releaseStatus);


}
