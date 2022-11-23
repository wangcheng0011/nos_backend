package com.knd.front.wallpaper.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.front.wallpaper.dto.UserWallPaperDto;
import com.knd.front.wallpaper.entity.WallPaperAttachEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author wangcheng
 */
public interface WallPaperAttachMapper extends BaseMapper<WallPaperAttachEntity> {

    @Select("select wa.id as id, wa.wallpaperName as wallpaperName,wa.type as type,wa.attachUrlId as attachUrlId,wa.sort as sort,wa.selected as selected,uw.userId from user_wallpaper uw "+
            "LEFT JOIN wallpaper_attach wa ON wa.id = uw.wallpaperId and wa.deleted = '0' "+
            "${ew.customSqlSegment}")
    Page<UserWallPaperDto> getUserWallPaperList(Page<UserWallPaperDto> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    //筛选出系统壁纸
    @Select("select id, wallpaperName ,type, attachUrlId, sort, selected  from wallpaper_attach where type = '1'")
    Page<UserWallPaperDto> getSysWallPaperList(Page<UserWallPaperDto> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
