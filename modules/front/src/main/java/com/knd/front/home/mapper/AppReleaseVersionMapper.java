package com.knd.front.home.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.home.dto.GetAppReleaseVersionDto;
import com.knd.front.entity.AppReleaseVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
public interface AppReleaseVersionMapper extends BaseMapper<AppReleaseVersion> {
    List<GetAppReleaseVersionDto> getAppReleaseVersion();


}
