package com.knd.front.home.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.BaseCourseType;
import com.knd.front.home.dto.CourseDto;
import com.knd.front.home.dto.CourseListDto;
import com.knd.front.home.mapper.BaseCourseTypeMapper;
import com.knd.front.home.service.IBaseCourseTypeService;
import com.knd.front.train.dto.FilterCourseLabelSettingsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-01
 */
@Service
public class BaseCourseTypeServiceImpl extends ServiceImpl<BaseCourseTypeMapper, BaseCourseType> implements IBaseCourseTypeService {
    @Autowired
    private BaseCourseTypeMapper baseCourseTypeMapper;

    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    @Value("${upload.FileVideoPath}")
    private String filVideoPath;

    @Override
    public BaseCourseType insertReturnEntity(BaseCourseType entity) {

        return null;
    }

    @Override
    public BaseCourseType updateReturnEntity(BaseCourseType entity) {
        return null;
    }

    @Override
    public Result getHomeCourseList(String userId) {
        Integer isVip;
        if (StringUtils.isEmpty(userId)) {
            isVip = 0;
        } else {
            isVip = 1;
        }
        List<CourseListDto> homeCourseList = baseCourseTypeMapper.getHomeCourseList(userId, isVip);
        List<CourseListDto> homeCourseListNotNull =
                homeCourseList.stream().filter(x -> (x.getTypeCourseList().size() > 0)).collect(Collectors.toList());
        if (StringUtils.isEmpty(homeCourseList)) {
            return ResultUtil.success(new ArrayList<>());
        }
        homeCourseListNotNull.forEach(item -> {
            ListSort(item.getTypeCourseList());
            item.getTypeCourseList().sort(Comparator.comparingInt(y -> Integer.parseInt((y.getChSort()))));

            item.getTypeCourseList().forEach(two -> {
                two.setPicAttachUrl(fileImagesPath + two.getPicAttachUrl());
                two.setVideoAttachUrl(filVideoPath + two.getVideoAttachUrl());

            });
        });


        return ResultUtil.success(homeCourseListNotNull);
    }

    private static void ListSort(List<CourseDto> list) {
        Collections.sort(list, new Comparator<CourseDto>() {
            @Override
            //定义一个比较器
            public int compare(CourseDto o1, CourseDto o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getLastModifiedDate());
                    Date dt2 = format.parse(o2.getLastModifiedDate());
                    if (dt1.before(dt2)) {
                        return 1;
                    } else if (dt1.after(dt2) ) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    @Override
    public Result getFilterCourseLabelSettings(String userId) {
        FilterCourseLabelSettingsDto filterCourseLabelSettingsDto = new FilterCourseLabelSettingsDto();
        filterCourseLabelSettingsDto.setTargetList(baseCourseTypeMapper.getBaseTargetList());
        filterCourseLabelSettingsDto.setPartList(baseCourseTypeMapper.getBaseBodyPartList());
        filterCourseLabelSettingsDto.setTypeList(baseCourseTypeMapper.getBaseCourseType());
        filterCourseLabelSettingsDto.setDifficultyList(baseCourseTypeMapper.getBaseDifficultyList());
        filterCourseLabelSettingsDto.setLabelList(baseCourseTypeMapper.getBaseLabelList());
        return ResultUtil.success(filterCourseLabelSettingsDto);
    }


}
