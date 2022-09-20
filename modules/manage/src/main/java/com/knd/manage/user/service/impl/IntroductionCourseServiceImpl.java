package com.knd.manage.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.manage.course.entity.CourseHead;
import com.knd.manage.course.mapper.CourseHeadMapper;
import com.knd.manage.course.mapper.CourseTypeMapper;
import com.knd.manage.user.dto.IntroductionCourseDto;
import com.knd.manage.user.dto.UserCourseListDto;
import com.knd.manage.user.entity.IntroductionCourse;
import com.knd.manage.user.mapper.IntroductionCourseMapper;
import com.knd.manage.user.service.IIntroductionCourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-08
 */
@Service
public class IntroductionCourseServiceImpl extends ServiceImpl<IntroductionCourseMapper, IntroductionCourse> implements IIntroductionCourseService {

    @Override
    public IntroductionCourse insertReturnEntity(IntroductionCourse entity) {
        return null;
    }

    @Override
    public IntroductionCourse updateReturnEntity(IntroductionCourse entity) {
        return null;
    }

    @Resource
    private CourseTypeMapper courseTypeMapper;

    @Resource
    private CourseHeadMapper courseHeadMapper;

    //查询注册会员课程列表
    @Override
    public Result queryUserCourseList(String nickName, String mobile, String equipmentNo, String trainTimeBegin, String trainTimeEnd, String current) throws ParseException {
       //如果页码小于1，则返回第一页数据
        IPage<IntroductionCourseDto> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //分页查询
        page = baseMapper.selectUserCoursePageBySome(page, nickName == null || nickName.equals("") ? null : "%" + nickName + "%",
                mobile == null || mobile.equals("") ? null : "%" + mobile + "%",
                equipmentNo == null || equipmentNo.equals("") ? null : "%" + equipmentNo + "%",
                trainTimeBegin == null ? null : sdf.parse(trainTimeBegin),
                trainTimeEnd == null ? null : sdf.parse(trainTimeEnd));
        List<IntroductionCourseDto> introductionCourseDtos = page.getRecords();
        //根据课程id 获取该课程的分类
        if (!introductionCourseDtos.isEmpty()) {
            for (IntroductionCourseDto c : introductionCourseDtos) {
                List<String> ls = courseTypeMapper.selectNameListByCourseid(c.getId());
                String type = "";
                if (!ls.isEmpty()) {
                    //拼接课程类型，多个类型以逗号隔开
                    StringBuilder s = new StringBuilder();
                    for (String str : ls) {
                        s.append(str).append(",");
                    }
                    if (s.length() > 0) {
                        type = s.substring(0, s.length() - 1);
                    }
                }
                c.setType(type);
            }
        }
        UserCourseListDto userCourseListDto = new UserCourseListDto();
        userCourseListDto.setTotal((int) page.getTotal());
        userCourseListDto.setIntroductionCourseList(introductionCourseDtos);
        return ResultUtil.success(userCourseListDto);
    }

    @Override
    public Result queryCourseList(String current) {
        Page<CourseHead> page = new Page<>(Integer.valueOf(current), PageInfo.pageSize);
        QueryWrapper<CourseHead> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted","0");
        Page<CourseHead> courseHeadPage = courseHeadMapper.selectPage(page, wrapper);
        return ResultUtil.success(courseHeadPage);
    }
}
