package com.knd.front.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.domain.CourseTypeEnum;
import com.knd.front.user.dto.UserCoursePurchasedDto;
import com.knd.front.user.entity.UserCourseEntity;
import com.knd.front.user.mapper.UserCourseMapper;
import com.knd.front.user.mapper.UserPurchasedMapper;
import com.knd.front.user.service.IUserPurchasedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserPurchasedServiceImpl implements IUserPurchasedService {
    private final UserPurchasedMapper userPurchasedMapper;
    private final UserCourseMapper userCourseMapper;

    @Override
    public Result getPurchasedCourse(String userId, String currentPage) {
        Page<UserCoursePurchasedDto> page = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        List<UserCoursePurchasedDto> list = new ArrayList<>();
        List<UserCourseEntity> entities = userPurchasedMapper.getPurchasedCourseList(page,userId);
        for(UserCourseEntity entity : entities){
            UserCoursePurchasedDto dto = new UserCoursePurchasedDto();
            BeanUtils.copyProperties(entity,dto);
            //dto.setCourseType(CourseTypeEnum.values()[Integer.valueOf(dto.getCourseType())].getDisplay());
            dto.setCourseType(dto.getCourseType());
            Integer trainNum = userCourseMapper.getTrainNum(userId,entity.getId());
            dto.setTrainNum(trainNum);
            list.add(dto);
        }
        page.setRecords(list);
        return ResultUtil.success(page);
    }
}
