package com.knd.front.login.mapper;

import com.knd.front.login.entity.ElementEntity;
import com.knd.front.login.entity.FloorEntity;
import com.knd.front.login.entity.PageEntity;
import com.knd.front.login.entity.PageFloorEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Lenovo
 */
public interface PageMapper {

    @Select("select a.*,b.filePath as imageUrl,b.filePath as backgroundUrl from base_page a  " +
            " LEFT JOIN attach b on a.imageUrlId = b.id " +
            " LEFT JOIN attach c on a.backgroundUrlId = c.id " +
            " where a.keyValue =#{key} and a.version<=#{version} and a.deleted=0 " +
            " ORDER BY a.version desc LIMIT 1 ")
    PageEntity getPage(@Param("key") String key,
                       @Param("version") String version);

    @Select("select a.*,b.filePath as imageUrl,b.filePath as backgroundUrl from base_page a  " +
            " LEFT JOIN attach b on a.imageUrlId = b.id " +
            " LEFT JOIN attach c on a.backgroundUrlId = c.id " +
            " where a.keyValue =#{key} and a.version<=#{version} and a.platform like #{platform} or trim(a.platform) = '' and a.deleted=0 " +
            " ORDER BY a.version desc LIMIT 1 ")
    PageEntity getPagePlatform(@Param("key") String key,
                               @Param("version") String version,
                               @Param("platform") String platform);

    @Select("select a.floorId,a.sort from base_page_floor a  " +
            " where a.pageId = #{pageId} and a.deleted = 0 " +
            " order by  CONVERT(a.sort,SIGNED) asc")
    List<PageFloorEntity> getPageFloorList(@Param("pageId") String pageId);

    @Select("select a.*,b.filePath as imageUrl,b.filePath as backgroundUrl " +
            " from base_floor a " +
            " LEFT JOIN attach b on a.imageUrlId = b.id " +
            " LEFT JOIN attach c on a.backgroundUrlId = c.id " +
            " where a.id=#{id} and a.deleted=0")
    FloorEntity getFloor(@Param("id") String id);

    @Select("select a.*,b.filePath as imageUrl,a.imageUrlId,c.filePath as backgroundUrl " +
            " FROM base_element a " +
            " LEFT JOIN attach b on a.imageUrlId = b.id " +
            " LEFT JOIN attach c on a.backgroundUrlId = c.id " +
            " where a.floorId=#{floorId} and a.deleted=0 " +
            " order by  CONVERT(a.sort,SIGNED) asc ")
    List<ElementEntity> getElementList(@Param("floorId") String floorId);



}
