package com.knd.front.login.service.impl;

import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.front.login.dto.ElementDto;
import com.knd.front.login.dto.FloorDto;
import com.knd.front.login.dto.PageDto;
import com.knd.front.login.entity.ElementEntity;
import com.knd.front.login.entity.FloorEntity;
import com.knd.front.login.entity.PageEntity;
import com.knd.front.login.entity.PageFloorEntity;
import com.knd.front.login.mapper.PageMapper;
import com.knd.front.login.service.IPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PageServiceImpl implements IPageService {

    private final PageMapper pageMapper;

    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;


    @Override
    public Result getPage(String key,String version,String platform) {
        log.info("getPage key:{{}}",key);
        log.info("getPage version:{{}}",version);
        log.info("getPage platform:{{}}",platform);
        PageEntity page = null;
        page = pageMapper.getPage(key,version);
        log.info("getPage page:{{}}",page);
        if (StringUtils.isEmpty(page)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"该页面数据不存在："+page);
        }
        List<FloorDto> floorDtoList = new ArrayList<>();
        List<PageFloorEntity> pageFloorList = pageMapper.getPageFloorList(page.getId());
        for(PageFloorEntity pf : pageFloorList){
            String floorId = pf.getFloorId();
            List<ElementDto> elementDtoList = new ArrayList<>();
            List<ElementEntity> elementList = pageMapper.getElementList(floorId);
            for(ElementEntity element : elementList){
                ElementDto elementDto = new ElementDto();
                if(null!=elementDto){
                    BeanUtils.copyProperties(element,elementDto);
                    if(StringUtils.isNotEmpty(element.getImageUrl())){
                        elementDto.setImageUrl(fileImagesPath + element.getImageUrl());
                    }
                    if(StringUtils.isNotEmpty(element.getBackgroundUrl())){
                        elementDto.setBackgroundUrl(fileImagesPath + element.getBackgroundUrl());
                    }
                }
              elementDtoList.add(elementDto);
            }

            FloorEntity floor = pageMapper.getFloor(floorId);
            log.info("getPage floor:{{}}",floor);
            log.info("getPage platform:{{}}",platform);
            log.info("getPage floor.getPlatform:{{}}",floor.getPlatform());
            if(floor.getPlatform().isEmpty()||floor.getPlatform().indexOf(platform)!=-1){
                FloorDto floorDto = new FloorDto();
                    BeanUtils.copyProperties(floor,floorDto);
                    floorDto.setSort(pf.getSort());
                    if(StringUtils.isNotEmpty(floor.getImageUrl())){
                        floorDto.setImageUrl(fileImagesPath + floor.getImageUrl());
                    }
                    if(StringUtils.isNotEmpty(floor.getBackgroundUrl())){
                        floorDto.setBackgroundUrl(fileImagesPath + floor.getBackgroundUrl());
                    }
                floorDto.setElementDtoList(elementDtoList);
                log.info("getPage floorDto:{{}}",floorDto);
                floorDtoList.add(floorDto);
            }

        }
        log.info("getPage floorDtoList:{{}}",floorDtoList);
        PageDto pageDto = new PageDto();
        BeanUtils.copyProperties(page,pageDto);
        if(StringUtils.isNotEmpty(page.getImageUrl())){
            pageDto.setImageUrl(fileImagesPath + page.getImageUrl());
        }
        if(StringUtils.isNotEmpty(page.getBackgroundUrl())){
            pageDto.setBackgroundUrl(fileImagesPath + page.getBackgroundUrl());
        }
        pageDto.setFloorDtoList(floorDtoList);
        log.info("getPage pageDto:{{}}",pageDto);
        return ResultUtil.success(pageDto);
    }
}
