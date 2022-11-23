package com.knd.manage.wallpaper.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.wallpaper.dto.WallPaperDto;
import com.knd.manage.wallpaper.entity.WallPaperAttachEntity;
import com.knd.manage.wallpaper.mapper.UserWallPaperMapper;
import com.knd.manage.wallpaper.mapper.WallPaperAttachMapper;
import com.knd.manage.wallpaper.request.WallPaperRequest;
import com.knd.manage.wallpaper.service.IWallPaperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
@Slf4j
public class IWallPaperServiceImp extends ServiceImpl<WallPaperAttachMapper, WallPaperAttachEntity> implements IWallPaperService {

    @Resource
    private IAttachService attachService;

    @Resource
    private WallPaperAttachMapper wallPaperAttachMapper;
    @Resource
    private UserWallPaperMapper userWallPaperMapper;

    //新增
    @Override
    public Result add(WallPaperRequest wallPaperRequest) {
        log.info("add wallPaperRequest:{{}}", wallPaperRequest);
        if (StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachName())
                && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachNewName())
                && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachSize())) {
            //保存选中图片
            Attach imgAPi = attachService.saveAttach(UserUtils.getUserId(), wallPaperRequest.getPicAttachUrl().getPicAttachName()
                    , wallPaperRequest.getPicAttachUrl().getPicAttachNewName(), wallPaperRequest.getPicAttachUrl().getPicAttachSize());
            WallPaperAttachEntity wallPaperAttachEntity = new WallPaperAttachEntity();
            wallPaperAttachEntity.setId(UUIDUtil.getShortUUID());
            wallPaperAttachEntity.setWallpaperName(wallPaperRequest.getWallpaperName());
            wallPaperAttachEntity.setAttachUrlId(imgAPi.getId());
            //系统壁纸
            wallPaperAttachEntity.setType("1");
            wallPaperAttachEntity.setSort(wallPaperRequest.getSort());
            wallPaperAttachEntity.setSelected(wallPaperRequest.getSelected());
            wallPaperAttachEntity.setUserId(UserUtils.getUserId());
            wallPaperAttachEntity.setCreateBy(UserUtils.getUserId());
            wallPaperAttachEntity.setCreateDate(LocalDateTime.now());
            wallPaperAttachEntity.setDeleted("0");
            wallPaperAttachMapper.insert(wallPaperAttachEntity);
        }
        return ResultUtil.success();
    }

    //修改
    @Override
    public Result edit(WallPaperRequest wallPaperRequest) {
        log.info("add wallPaperRequest:{{}}", wallPaperRequest);
        WallPaperAttachEntity wallPaperAttachEntity = new WallPaperAttachEntity();
        if (StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachName())
                && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachNewName())
                && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachSize())) {
            //保存选中图片
            WallPaperAttachEntity wallPaperAttach = wallPaperAttachMapper.selectById(wallPaperRequest.getId());
            attachService.deleteFile(wallPaperAttach.getAttachUrlId(), UserUtils.getUserId());
            Attach imgAPi = attachService.saveAttach(UserUtils.getUserId(), wallPaperRequest.getPicAttachUrl().getPicAttachName()
                    , wallPaperRequest.getPicAttachUrl().getPicAttachNewName(), wallPaperRequest.getPicAttachUrl().getPicAttachSize());
            wallPaperAttachEntity.setId(wallPaperRequest.getId());
            wallPaperAttachEntity.setWallpaperName(wallPaperRequest.getWallpaperName());
            wallPaperAttachEntity.setAttachUrlId(imgAPi.getId());
            //系统壁纸
            wallPaperAttachEntity.setType("1");
            wallPaperAttachEntity.setSort(wallPaperRequest.getSort());
            wallPaperAttachEntity.setSelected(wallPaperRequest.getSelected());
            wallPaperAttachEntity.setUserId(UserUtils.getUserId());
            wallPaperAttachEntity.setCreateBy(UserUtils.getUserId());
            wallPaperAttachEntity.setCreateDate(LocalDateTime.now());
            wallPaperAttachEntity.setDeleted("0");
            wallPaperAttachMapper.updateById(wallPaperAttachEntity);
        }
        return ResultUtil.success(wallPaperAttachEntity);
    }

    //删除
    @Override
    public Result delete(String id) {
        WallPaperAttachEntity wallPaperAttachEntity = wallPaperAttachMapper.selectById(id);
        attachService.deleteFile(wallPaperAttachEntity.getAttachUrlId(), UserUtils.getUserId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("wallpaperId",id);
        map.put("deleted",0);
        userWallPaperMapper.deleteByMap(map);
        baseMapper.deleteById(wallPaperAttachEntity);
        //成功
        return ResultUtil.success();
    }


    //获取壁纸
    @Override
    public Result getWallPaper(String id) {
        QueryWrapper<WallPaperAttachEntity> wa = new QueryWrapper<WallPaperAttachEntity>();
        wa.eq("id", id);
        wa.eq("deleted", "0");
        WallPaperAttachEntity wallPaperAttachEntity = baseMapper.selectOne(wa);
        WallPaperDto wallPaperDto = new WallPaperDto();
        BeanUtils.copyProperties(wallPaperAttachEntity, wallPaperDto);
        wallPaperDto.setImageUrl(attachService.getImgDto(wallPaperAttachEntity.getAttachUrlId()));
        return ResultUtil.success(wallPaperDto);
    }

    //获取壁纸列表
    @Override
    public Result getWallPaperList(String name, String type, Integer size, String current) {
        QueryWrapper<WallPaperAttachEntity> qw = new QueryWrapper<WallPaperAttachEntity>();
        if (StringUtils.isNotEmpty(name)) {
            qw.like("wallpaperName", name);
        }
        if (StringUtils.isNotEmpty(type)) {
            qw.eq("type", type);
        }
        qw.eq("deleted", "0");
        qw.orderByDesc("createDate");
        if (StringUtils.isEmpty(size)) {
            size = PageInfo.pageSize;
        }
        //分页
        Page<WallPaperAttachEntity> partPage = new Page<>(Long.parseLong(current), size);
        partPage = baseMapper.selectPage(partPage, qw);
        List<WallPaperAttachEntity> wallPaperAttachEntityList = partPage.getRecords();
        Page<WallPaperDto> wallPaperDtoPage = new Page<>(Long.parseLong(current), size);
        List<WallPaperDto> wallPaperDtoList = new ArrayList<WallPaperDto>();
        log.info("getWallPaperList wallPaperDtoList:{{}}", wallPaperDtoList);
        wallPaperAttachEntityList.stream().forEach(wallPaperAttachEntity -> {
            WallPaperDto wallPaperDto = new WallPaperDto();
            BeanUtils.copyProperties(wallPaperAttachEntity, wallPaperDto);
            wallPaperDto.setImageUrl(attachService.getImgDto(wallPaperAttachEntity.getAttachUrlId()));
            wallPaperDtoList.add(wallPaperDto);
        });
        wallPaperDtoPage.setRecords(wallPaperDtoList);
        wallPaperDtoPage.setTotal(partPage.getTotal());
        wallPaperDtoPage.setCurrent(partPage.getCurrent());
        //成功
        return ResultUtil.success(wallPaperDtoPage);
    }


}
