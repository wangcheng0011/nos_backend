package com.knd.front.wallpaper.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.service.AttachService;
import com.knd.front.entity.Attach;
import com.knd.front.wallpaper.dto.UserWallPaperDto;
import com.knd.front.wallpaper.dto.WallPaperDto;
import com.knd.front.wallpaper.entity.UserWallPaperEntity;
import com.knd.front.wallpaper.entity.WallPaperAttachEntity;
import com.knd.front.wallpaper.mapper.UserWallPaperMapper;
import com.knd.front.wallpaper.mapper.WallPaperAttachMapper;
import com.knd.front.wallpaper.request.SaveUserWallPaperRequest;
import com.knd.front.wallpaper.request.SaveWallPaperRequest;
import com.knd.front.wallpaper.request.UpdateUserWallPaperRequest;
import com.knd.front.wallpaper.request.UpdateWallPaperRequest;
import com.knd.front.wallpaper.service.IWallPaperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@Service
@Slf4j
public class IWallPaperServiceImp extends ServiceImpl<WallPaperAttachMapper, WallPaperAttachEntity> implements IWallPaperService {

    @Resource
    private AttachService attachService;

    @Resource
    private WallPaperAttachMapper wallPaperAttachMapper;

    @Resource
    private UserWallPaperMapper userWallPaperMapper;

    //新增
    @Override
    public Result add(SaveWallPaperRequest wallPaperRequest) {
        log.info("wallPaperAdd wallPaperRequest:{{}}", wallPaperRequest);
        WallPaperAttachEntity wallPaperAttachEntity = new WallPaperAttachEntity();
            if (StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachName())
                    && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachNewName())
                    && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachSize())) {
                //保存选中图片
                Attach imgAPi = attachService.saveAttach(UserUtils.getUserId(), wallPaperRequest.getPicAttachUrl().getPicAttachName()
                        , wallPaperRequest.getPicAttachUrl().getPicAttachNewName(), wallPaperRequest.getPicAttachUrl().getPicAttachSize());
                log.info("wallPaperAdd imgAPi:{{}}", imgAPi);
                wallPaperAttachEntity.setId(UUIDUtil.getShortUUID());
                wallPaperAttachEntity.setAttachUrlId(imgAPi.getId());
                //自定义壁纸
                wallPaperAttachEntity.setType("2");
                wallPaperAttachEntity.setSort(wallPaperRequest.getSort());
                wallPaperAttachEntity.setSelected(wallPaperRequest.getSelected());
                wallPaperAttachEntity.setUserId(UserUtils.getUserId());
                wallPaperAttachEntity.setCreateBy(UserUtils.getUserId());
                wallPaperAttachEntity.setCreateDate(LocalDateTime.now());
                wallPaperAttachEntity.setDeleted("0");
                log.info("wallPaperAdd wallPaperAttachEntity:{{}}", wallPaperAttachEntity);
                wallPaperAttachMapper.insert(wallPaperAttachEntity);
            }
        return ResultUtil.success(attachService.getImgDto(wallPaperAttachEntity.getAttachUrlId()));
    }

    //修改
    @Override
    public Result edit(UpdateWallPaperRequest wallPaperRequest) {
        log.info("add wallPaperRequest:{{}}", wallPaperRequest);
        WallPaperAttachEntity wallPaperAttachEntity = new WallPaperAttachEntity();
            if (StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachName())
                    && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachNewName())
                    && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachSize())) {
                //保存选中图片
                WallPaperAttachEntity wallPaperAttach= wallPaperAttachMapper.selectById(wallPaperRequest.getId());
                attachService.deleteFile(wallPaperAttach.getAttachUrlId(),UserUtils.getUserId());
                Attach imgAPi = attachService.saveAttach(UserUtils.getUserId(), wallPaperRequest.getPicAttachUrl().getPicAttachName()
                        , wallPaperRequest.getPicAttachUrl().getPicAttachNewName(), wallPaperRequest.getPicAttachUrl().getPicAttachSize());
                wallPaperAttachEntity.setId(wallPaperRequest.getId());
                wallPaperAttachEntity.setAttachUrlId(imgAPi.getId());
                //自定义壁纸
                wallPaperAttachEntity.setType("2");
                wallPaperAttachEntity.setSort(wallPaperRequest.getSort());
                wallPaperAttachEntity.setSelected(wallPaperRequest.getSelected());
                wallPaperAttachEntity.setUserId(UserUtils.getUserId());
                wallPaperAttachEntity.setCreateBy(UserUtils.getUserId());
                wallPaperAttachEntity.setCreateDate(LocalDateTime.now());
                wallPaperAttachEntity.setDeleted("0");
                wallPaperAttachMapper.updateById(wallPaperAttachEntity);
            }
        return ResultUtil.success(attachService.getImgDto(wallPaperAttachEntity.getAttachUrlId()));
    }

    //删除
    @Override
    public Result delete(String ids) {
        log.info("wallPaperDelete ids:{{}}", ids);
        String[] split = ids.split(",");
        List<String> idList = Arrays.asList(split);
        log.info("wallPaperDelete idList:{{}}", idList);
        idList.stream().forEach(id->{
            log.info("wallPaperDelete id:{{}}", id);
            WallPaperAttachEntity wallPaperAttachEntity = wallPaperAttachMapper.selectById(id);
            attachService.deleteFile(wallPaperAttachEntity.getAttachUrlId(),UserUtils.getUserId());
            log.info("wallPaperDelete wallPaperAttachEntity:{{}}", wallPaperAttachEntity);
            baseMapper.deleteById(wallPaperAttachEntity);
            HashMap<String, Object> map = new HashMap<>();
            map.put("wallpaperId",id);
            map.put("deleted",0);
            userWallPaperMapper.deleteByMap(map);
        });
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
    public Result getWallPaperList(String type, Integer size, String current) {
        QueryWrapper<WallPaperAttachEntity> qw = new QueryWrapper<WallPaperAttachEntity>();
        if(StringUtils.isNotEmpty(type)){
            qw.eq("type", type);
            if("2".equals(type)){
                qw.eq("userId", UserUtils.getUserId());
            }
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
        log.info("getWallPaperList wallPaperDtoList:{{}}",wallPaperDtoList);
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

    @Override
    public Result saveUserWallPaper(SaveUserWallPaperRequest saveUserWallPaperRequest) {
        QueryWrapper<UserWallPaperEntity> userWallPaperEntityQueryWrapper = new QueryWrapper<>();
        userWallPaperEntityQueryWrapper.eq("userId",UserUtils.getUserId());
        userWallPaperEntityQueryWrapper.eq("deleted",0);
        userWallPaperMapper.delete(userWallPaperEntityQueryWrapper);
        saveUserWallPaperRequest.getUserWallPaperList().stream().forEach(
                userWallPaper->{
                    UserWallPaperEntity userWallPaperEntity = new UserWallPaperEntity();
                    userWallPaperEntity.setId(UUIDUtil.getShortUUID());
                    userWallPaperEntity.setWallpaperId(userWallPaper.getWallpaperId());
                    userWallPaperEntity.setUserId(UserUtils.getUserId());
                    userWallPaperEntity.setDeleted("0");
                    userWallPaperEntity.setCreateBy(UserUtils.getUserId());
                    userWallPaperEntity.setCreateDate(LocalDateTime.now());
                    userWallPaperMapper.insert(userWallPaperEntity);
                }
        );
        return ResultUtil.success();
    }

    @Override
    public Result updateUserWallPaper(UpdateUserWallPaperRequest updateUserWallPaperRequest) {
        UserWallPaperEntity userWallPaperEntity = new UserWallPaperEntity();
        userWallPaperEntity.setId(updateUserWallPaperRequest.getId());
        userWallPaperEntity.setWallpaperId(updateUserWallPaperRequest.getWallpaperId());
        userWallPaperEntity.setUserId(UserUtils.getUserId());
        userWallPaperEntity.setLastModifiedBy(UserUtils.getUserId());
        userWallPaperEntity.setLastModifiedDate(LocalDateTime.now());
        userWallPaperMapper.updateById(userWallPaperEntity);
        return ResultUtil.success(userWallPaperEntity);
    }

    @Override
    public Result deleteUserWallPaper(String ids) {
        log.info("wallPaperDelete ids:{{}}", ids);
        String[] split = ids.split(",");
        List<String> idList = Arrays.asList(split);
        log.info("wallPaperDelete idList:{{}}", idList);
        idList.stream().forEach(i-> {
                    log.info("wallPaperDelete i:{{}}", i);
                    WallPaperAttachEntity wallPaperAttachEntity = wallPaperAttachMapper.selectById(i);
                    attachService.deleteFile(wallPaperAttachEntity.getAttachUrlId(), UserUtils.getUserId());
                });
        userWallPaperMapper.deleteBatchIds(idList);
        //成功
        return ResultUtil.success();
    }

    @Override
    public Result getUserWallPaperList(String type, Integer size, String current) {
        QueryWrapper<UserWallPaperDto> qw = new QueryWrapper<UserWallPaperDto>();
        if(StringUtils.isNotEmpty(type)){
            qw.eq("wa.type", type);
        }
        qw.eq("uw.deleted", 0);
        qw.eq("uw.userId", UserUtils.getUserId());
        qw.orderByAsc("wa.type","wa.createDate");
        //分页
        if (StringUtils.isEmpty(size)) {
            size = PageInfo.pageSize;
        }
        Page<UserWallPaperDto> page = new Page<>(Long.parseLong(current), size);
        Page<UserWallPaperDto> userWallPaperListPage = wallPaperAttachMapper.getUserWallPaperList(page, qw);
        log.info("getUserWallPaperList userWallPaperListPage");
        if(StringUtils.isEmpty(userWallPaperListPage.getRecords())){
            qw.clear();
            userWallPaperListPage = wallPaperAttachMapper.getSysWallPaperList(page, qw);
        }
        userWallPaperListPage.getRecords().stream().forEach(i->{
            i.setImageUrl(attachService.getImgDto(i.getAttachUrlId()));
        });
        return ResultUtil.success(userWallPaperListPage);
    }




}
