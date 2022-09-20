package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.front.domain.UserPayCheckEnum;
import com.knd.front.entity.*;
import com.knd.front.home.mapper.CourseHeadMapper;
import com.knd.front.home.service.IViewRecordService;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.train.dto.CourseDetailDto;
import com.knd.front.train.dto.CourseNodeDetailDto;
import com.knd.front.train.dto.CourseVideoProgressInfoDto;
import com.knd.front.train.dto.FreeTrainDetailDto;
import com.knd.front.train.mapper.AttachMapper;
import com.knd.front.train.mapper.BaseActionEquipmentMapper;
import com.knd.front.train.mapper.CourseDetailMapper;
import com.knd.front.train.service.ICourseDetailService;
import com.knd.front.user.service.IUserActionPowerTestService;
import com.knd.front.user.service.IUserPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseDetailServiceImpl implements ICourseDetailService {
    private final AttachMapper attachMapper;
    private final IUserPayService userPayService;
    private final UserMapper userMapper;
    private final CourseHeadMapper courseHeadMapper;
    private final CourseDetailMapper courseDetailMapper;
    private final BaseActionEquipmentMapper baseActionEquipmentMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    @Value("${upload.FileVideoPath}")
    private String fileVideoPath;
    
    @Autowired
    private IUserActionPowerTestService iUserActionPowerTestService;


    @Override
    public Result getCourseDetail(String courseId, String userId) {

        // 2021/8/20 增加校验，判断课程是否收费，是否需要购买，是否仅vip可见
        CourseHead courseHead = courseHeadMapper.selectById(courseId);
        if (StringUtils.isEmpty(courseHead)){
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"课程不存在");
        }

        CourseDetailDto courseDetail = courseDetailMapper.getCourseDetail(courseId, userId);
        courseDetail.getBlockList().forEach(x ->{
            x.getActionList().size();
            log.info("长度为:{}",x.getActionList().size());
        });
        if (StringUtils.isEmpty(courseDetail)) {
            return ResultUtil.success(new CourseDetailDto());
        }
        courseDetail.setPicAttachUrl(fileImagesPath + courseDetail.getPicAttachUrl());
        courseDetail.setVideoAttachUrl(fileVideoPath + courseDetail.getVideoAttachUrl());

        //获取个性化力量
        if(courseDetail.getBlockList().size()>0){
            courseDetail.getBlockList().stream()
                    .filter(item ->item.getActionList().size()>0)
                    .map(item -> {
                        return item.getActionList().stream()
                                .map(i -> {
                                    i.setPicAttachUrl(fileImagesPath+i.getPicAttachUrl());
                                    return i;
                                })
                                .filter(i ->!"0".equals(i.getActionType()))
                                .map(i -> {
                                    //获取个性化力量值
                                    String userTrainPower = iUserActionPowerTestService.getUserTrainPower(userId, i.getActionType());
                                    if(!"-1".equals(userTrainPower)) {
                                        i.setBasePower(userTrainPower);
                                    }
                                    return i;
                                }).collect(Collectors.toList());
                    }).collect(Collectors.toList());
        }

        //值为1代表仅会员可见
        String userScope = courseHead.getUserScope();
        //课程单价
        BigDecimal amount = courseHead.getAmount();
        if (amount.compareTo(new BigDecimal(0)) == 0){
            //价格为0代表是免费课程，需要判断是否会员可见
            if ("1".equals(userScope)){
                LocalDate nowDate = LocalDate.now();
                User user = userMapper.selectOne(new QueryWrapper<User>()
                        .eq("id", userId)
                        .ne("vipStatus", "0")
                        .ge("vipEndDate", nowDate)
                        .eq("deleted", "0"));
                if (StringUtils.isNotEmpty(user)){
                    //该用户是会员,说明可以观看
                    courseDetail.setBuyStatus("1");
                }else {
                    //用户不是会员或者会员已到期。需要去购买会员
                    courseDetail.setBuyStatus("2");
                }
            }else {
                //所有人都可以看
                courseDetail.setBuyStatus("1");
            }
        }else {
            //价格不为0，需要判断是否已购买
            Result check = userPayService.check(userId, UserPayCheckEnum.COURSE.ordinal(), courseId);
            boolean isPay = (boolean)check.getData();
            if (isPay){
                courseDetail.setBuyStatus("1");
            }else {
                courseDetail.setBuyStatus("3");
            }
        }
        //课程不是可看得状态，把视频得链接清空，让用户去购买
        /*if (!"1".equals(courseDetail.getBuyStatus())){
            courseDetail.setVideoAttachUrl("");
        }*/
        //保存浏览记录
        //iViewRecordService.addViewRecord(ViewTypeEnum.COURSE.getCode(),courseId);
        return ResultUtil.success(courseDetail);

    }

    @Override
    public Result getCourseNodeDetail(String courseId, String userId) {
        List<CourseNodeDetailDto> courseNodeDetail = courseDetailMapper.getCourseNodeDetail(courseId, userId);
        for (int i = 0; i < courseNodeDetail.size(); i++) {
            if (StringUtils.isEmpty(courseNodeDetail.get(i).getNodeBeginMinutes())) {
                courseNodeDetail.get(i).setNodeBeginMinutes("0");
            }
            if (StringUtils.isEmpty(courseNodeDetail.get(i).getNodeBeginSeconds())) {
                courseNodeDetail.get(i).setNodeBeginSeconds("0");
            }
            if (StringUtils.isEmpty(courseNodeDetail.get(i).getNodeEndMinutes())) {
                courseNodeDetail.get(i).setNodeEndMinutes("0");
            }
            if (StringUtils.isEmpty(courseNodeDetail.get(i).getNodeEndSeconds())) {
                courseNodeDetail.get(i).setNodeEndSeconds("0");
            }
            if ("0".equals(courseNodeDetail.get(i).getTrainingFlag())){
                courseNodeDetail.get(i).setCountMode("");
            }
            if (StringUtils.isEmpty(courseNodeDetail.get(i).getCountMode())){
                courseNodeDetail.get(i).setCountMode("");
            }
            String nodeBeginMinutes =
                    (Integer.parseInt(courseNodeDetail.get(i).getNodeBeginMinutes()) * 60 + Integer.parseInt(courseNodeDetail.get(i).getNodeBeginSeconds())) + "";
            String nodeEndMinutes =
                    (Integer.parseInt(courseNodeDetail.get(i).getNodeEndMinutes()) * 60 + Integer.parseInt(courseNodeDetail.get(i).getNodeEndSeconds())) + "";
            courseNodeDetail.get(i).setNodeBeginTotalSeconds(nodeBeginMinutes);
            courseNodeDetail.get(i).setNodeEndTotalSeconds(nodeEndMinutes);

            Attach attach = attachMapper.selectById(courseNodeDetail.get(i).getMusicUrl());
            courseNodeDetail.get(i).setMusicUrl(attach!=null ? fileVideoPath+attach.getFilePath() : "");
            if (StringUtils.isNotEmpty(courseNodeDetail.get(i).getPicAttachUrl())){
                Attach attachPic = attachMapper.selectById(courseNodeDetail.get(i).getPicAttachUrl());
                courseNodeDetail.get(i).setPicAttachUrl(attachPic!=null ? fileImagesPath + attachPic.getFilePath() : "");
            }
            int count = baseActionEquipmentMapper.selectCount(new QueryWrapper<BaseActionEquipment>().eq("deleted", 0).eq("actionId", courseNodeDetail.get(i).getActionId()));
            courseNodeDetail.get(i).setIsEquipment(count==0 ? "0" : "1");

        }
        return ResultUtil.success(courseNodeDetail);
    }

    @Override
    public Result getFreeTrainDetail(String actionId, String userId) {
        FreeTrainDetailDto freeTrainDetail = courseDetailMapper.getFreeTrainDetail(actionId, userId);
        if (freeTrainDetail == null) {
            return ResultUtil.success(new FreeTrainDetailDto());
        }
        freeTrainDetail.setPicAttachUrl(fileImagesPath + freeTrainDetail.getPicAttachUrl());
        freeTrainDetail.setVideoAttachUrl(fileVideoPath + freeTrainDetail.getVideoAttachUrl());
        if(!"0".equals(freeTrainDetail.getActionType())) {
            //获取个性化力量值
            String userTrainPower = iUserActionPowerTestService.getUserTrainPower(userId, freeTrainDetail.getActionType());
            if(!"-1".equals(userTrainPower)) {
                freeTrainDetail.setBasePower(userTrainPower);
            }
        }
        //保存浏览记录
        //iViewRecordService.addViewRecord(ViewTypeEnum.FREE_TRAIN.getCode(),actionId);
        return ResultUtil.success(freeTrainDetail);
    }

    @Override
    public Result getCourseVideoProgressInfo(String courseId, String userId) {
        //获取一共有几个block(包括没有block的)
        List<CourseNodeDetailDto> courseNodeDetailDtoList = courseDetailMapper.getCourseVideoProgressInfo(courseId, userId);
        //返回的结果集
        List<CourseVideoProgressInfoDto> courseVideoProgressInfoDtoList = new ArrayList<>();
        courseNodeDetailDtoList.stream().forEach(item -> {
            if (item.getBlockId() == null) {
                item.setBlockId("");
            }
        });
        //分组
        Map<String, List<CourseNodeDetailDto>> courseNodeDetailDtoMap =
                courseNodeDetailDtoList.stream().collect(Collectors.groupingBy(CourseNodeDetailDto::getBlockId));

        for (String key : courseNodeDetailDtoMap.keySet()) {
            List<CourseNodeDetailDto> list1 = courseNodeDetailDtoMap.get(key).stream().collect(Collectors.toList());
//            String value = collect1.get(key).toString();
            for (int i = 0; i < list1.size(); i++) {

                if (StringUtils.isEmpty(key)) {
                    CourseVideoProgressInfoDto courseVideoProgressInfoDto = new CourseVideoProgressInfoDto();
                    Integer beginSeconds = Integer.parseInt(list1.get(i).getNodeBeginMinutes()) * 60 + Integer.parseInt(list1.get(i).getNodeBeginSeconds());
                    Integer endSeconds = Integer.parseInt(list1.get(i).getNodeEndMinutes()) * 60 + Integer.parseInt(list1.get(i).getNodeEndSeconds());
                    courseVideoProgressInfoDto.setProgressStartSeconds(beginSeconds + "");
                    courseVideoProgressInfoDto.setProgressEndSeconds(endSeconds + "");
                    courseVideoProgressInfoDto.setNodeSort(list1.get(i).getNodeSort());
                    courseVideoProgressInfoDto.setAimSetNum("1");
                    courseVideoProgressInfoDtoList.add(courseVideoProgressInfoDto);
                } else {

                    CourseVideoProgressInfoDto courseVideoProgressInfoDto = new CourseVideoProgressInfoDto();
                    Integer beginSeconds =
                            Integer.parseInt(list1.get(0).getNodeBeginMinutes()) * 60 + Integer.parseInt(list1.get(0).getNodeBeginSeconds());
                    Integer endSeconds =
                            Integer.parseInt(list1.get(list1.size() - 1).getNodeEndMinutes()) * 60 + Integer.parseInt(list1.get(list1.size() - 1).getNodeEndSeconds());
                    courseVideoProgressInfoDto.setProgressStartSeconds(beginSeconds + "");
                    courseVideoProgressInfoDto.setProgressEndSeconds(endSeconds + "");
                    courseVideoProgressInfoDto.setNodeSort(list1.get(i).getNodeSort());
                    courseVideoProgressInfoDto.setAimSetNum(list1.get(i).getAimSetNum());
                    courseVideoProgressInfoDtoList.add(courseVideoProgressInfoDto);

                }
            }
        }

        List<CourseVideoProgressInfoDto> courseVideoProgressInfoDtoDataList =
                courseVideoProgressInfoDtoList.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparing(CourseVideoProgressInfoDto::getProgressEndSeconds))), ArrayList::new));
        courseVideoProgressInfoDtoDataList.sort(Comparator.comparingInt(x ->Integer.parseInt(x.getNodeSort())));
        //        List<CourseVideoProgressInfoDto> collect = collect2.stream().sorted(comparing(CourseVideoProgressInfoDto::getNodeSort)).collect(Collectors.toList());
//        courseVideoProgressInfoDtoList.sort(comparingInt(x ->Integer.parseInt(x.getNodeSort())));
        return ResultUtil.success(courseVideoProgressInfoDtoDataList);
    }
}