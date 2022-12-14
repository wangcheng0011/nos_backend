package com.knd.manage.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.Md5Utils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.PlatformEnum;
import com.knd.common.em.VipEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.mall.entity.TbOrder;
import com.knd.manage.mall.entity.UserReceiveAddressEntity;
import com.knd.manage.mall.mapper.TbOrderMapper;
import com.knd.manage.mall.mapper.UserReceiveAddressMapper;
import com.knd.manage.user.dto.*;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.mapper.UserMapper;
import com.knd.manage.user.request.RegisterRequest;
import com.knd.manage.user.service.IUserDetailService;
import com.knd.manage.user.service.IUserService;
import com.knd.manage.user.service.feignInterface.QueryUserTrainInfoService;
import com.knd.manage.user.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * ???????????????
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
@Service
@Transactional
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private QueryUserTrainInfoService queryUserTrainInfoService;
    @Resource
    private IUserDetailService iUserDetailService;
    @Resource
    private UserReceiveAddressMapper userReceiveAddressMapper;
    @Resource
    private TbOrderMapper tbOrderMapper;
    @Resource
    private IAttachService iAttachService;
    @Value("${httpsDomain}")
    private String httpsDomain;

    /**
     * app??????token?????????
     */
    private static final int EXPIRE_SECOND_WEB = 1 * 30 * 60;


    @Override
    public User insertReturnEntity(User entity) {
        return null;
    }

    @Override
    public User updateReturnEntity(User entity) {
        return null;
    }

    //????????????????????????
    @Override
    public Result queryRegistedUserList(String nickName, String mobile, String frozenFlag, String registTimeBegin,
                                        String registTimeEnd, String current) throws ParseException {
        IPage<UserDto> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //????????????
        page = baseMapper.selectPageBySome(page, nickName == null ? "%%" : "%" + nickName + "%", mobile == null ? "%%" : "%" + mobile + "%",
                frozenFlag == null ? "%%" : frozenFlag, registTimeBegin == null ? null : sdf.parse(registTimeBegin),
                registTimeEnd == null ? null : sdf.parse(registTimeEnd));
        //??????????????????
        RegistedUserListDto registedUserListDto = new RegistedUserListDto();
        registedUserListDto.setTotal((int) page.getTotal());
        registedUserListDto.setUserList(page.getRecords());
        return ResultUtil.success(registedUserListDto);
    }

    //??????????????????????????????
    @Override
    public Result queryUserTrainList(String nickName, String mobile, String equipmentNo, String trainTimeBegin,
                                     String trainTimeEnd, String current, String trainType) throws ParseException {
        IPage<TrainDto> page = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //????????????
        if (StringUtils.isEmpty(trainType)) {
            page = baseMapper.selectTrainPageBySome(page, nickName == null ? "%%" : "%" + nickName + "%", mobile == null ? "%%" : "%" + mobile + "%",
                    equipmentNo == null ? "%%" : "%" + equipmentNo + "%", trainTimeBegin == null ? null : sdf.parse(trainTimeBegin),
                    trainTimeEnd == null ? null : sdf.parse(trainTimeEnd));
        } else if (trainType.equals("1")) {
            //????????????
            page = baseMapper.selectTrainPageBySome2(page, nickName == null ? "%%" : "%" + nickName + "%", mobile == null ? "%%" : "%" + mobile + "%",
                    equipmentNo == null ? "%%" : "%" + equipmentNo + "%", trainTimeBegin == null ? null : sdf.parse(trainTimeBegin),
                    trainTimeEnd == null ? null : sdf.parse(trainTimeEnd));
        } else if (trainType.equals("2")) {
            //????????????
            page = baseMapper.selectTrainPageBySome3(page, nickName == null ? "%%" : "%" + nickName + "%", mobile == null ? "%%" : "%" + mobile + "%",
                    equipmentNo == null ? "%%" : "%" + equipmentNo + "%", trainTimeBegin == null ? null : sdf.parse(trainTimeBegin),
                    trainTimeEnd == null ? null : sdf.parse(trainTimeEnd));
        } else {
            //??????????????????
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        log.info("queryUserTrainList page:{{}}", page);
        log.info("queryUserTrainList TrainDto:{{}}", page.getRecords());
        //??????????????????
        UserTrainListDto userTrainListDto = new UserTrainListDto();
        userTrainListDto.setTotal((int) page.getTotal());
        userTrainListDto.setTrainList(page.getRecords());
        log.info("queryUserTrainList userTrainListDto:{{}}", userTrainListDto);
        return ResultUtil.success(userTrainListDto);
    }

    //??????????????????????????????
    @Override
    public Result queryUserTrainInfo(VoQueryUserTrainInfo vo) {
        if (vo.getTrainType().equals("1")) {
            //????????????

            Result userTrainCourseDetail = queryUserTrainInfoService.getUserTrainCourseDetail(vo.getUserId(), vo.getTrainReportId());
            JSONObject jsonObject = (JSONObject) JSON.toJSON(userTrainCourseDetail.getData());
            Map<String, Class> classMap = new HashMap<>();
            classMap.put("data", TrainCourseHeadInfoDto.class);
            classMap.put("blockList", TrainCourseBlockInfoDto.class);
            classMap.put("actionList", TrainCourseActInfoDto.class);
            TrainCourseHeadInfoDto trainCourseHeadInfoData = (TrainCourseHeadInfoDto) JSONObject.toJavaObject(jsonObject,
                    TrainCourseHeadInfoDto.class);
            TrainCourseHeadInfoDto trainCourseHeadInfo = new TrainCourseHeadInfoDto();
            trainCourseHeadInfo.setId(trainCourseHeadInfoData.getId());
            trainCourseHeadInfo.setCommitTrainTime(trainCourseHeadInfoData.getCommitTrainTime());
            trainCourseHeadInfo.setCourse(trainCourseHeadInfoData.getCourse());
            trainCourseHeadInfo.setTotalDurationSeconds(DateUtils.getGapTime(Integer.parseInt(trainCourseHeadInfoData.getTotalDurationSeconds()) * 1000));
            trainCourseHeadInfo.setActualTrainSeconds(DateUtils.getGapTime(Integer.parseInt(trainCourseHeadInfoData.getActualTrainSeconds()) * 1000));
            trainCourseHeadInfo.setBlockList(trainCourseHeadInfoData.getBlockList());
            trainCourseHeadInfo.getBlockList().forEach(x -> {
                x.getActionList().forEach(y -> {
                    if (countMod.Metering.key.equals(y.getActCountMod())) {
                        y.setActCountMod(countMod.Metering.value);
                    }
                    if (countMod.Time.key.equals(y.getActCountMod())) {
                        y.setActCountMod(countMod.Time.value);
                    }
                    if (countMod.MeteringOnTime.key.equals(y.getActCountMod())) {
                        y.setActCountMod(countMod.MeteringOnTime.value);
                    }
                });
            });
            return ResultUtil.success(trainCourseHeadInfo);
        } else {
            //????????????
            Result userTrainFreeDetail = queryUserTrainInfoService.getUserTrainFreeDetail(vo.getUserId(), vo.getTrainReportId());
            JSONObject jsonObject = (JSONObject) JSON.toJSON(userTrainFreeDetail.getData());
            TrainFreeHeadDto trainFreeHeadDtoData = JSON.toJavaObject(jsonObject, TrainFreeHeadDto.class);
            TrainFreeHeadDto trainFreeHeadDto = new TrainFreeHeadDto();
            trainFreeHeadDto.setAction(trainFreeHeadDtoData.getAction());
            trainFreeHeadDto.setActTrainSeconds(DateUtils.getGapTime(Integer.parseInt(trainFreeHeadDtoData.getActTrainSeconds()) * 1000));
            trainFreeHeadDto.setCommitTrainTime(trainFreeHeadDtoData.getCommitTrainTime());
            trainFreeHeadDto.setFinishCounts(trainFreeHeadDtoData.getFinishCounts());
            trainFreeHeadDto.setFinishSets(trainFreeHeadDtoData.getFinishSets());
            trainFreeHeadDto.setFinishTotalPower(trainFreeHeadDtoData.getFinishTotalPower());
            trainFreeHeadDto.setTotalSeconds(DateUtils.getGapTime(Integer.parseInt(trainFreeHeadDtoData.getTotalSeconds()) * 1000));
            trainFreeHeadDto.setTrainFreeItemList(trainFreeHeadDtoData.getTrainFreeItemList());
            trainFreeHeadDto.getTrainFreeItemList().forEach(x -> {
                if (countMod.Metering.key.equals(x.getActCountMod())) {
                    x.setActCountMod(countMod.Metering.value);
                }
                if (countMod.Time.key.equals(x.getActCountMod())) {
                    x.setActCountMod(countMod.Time.value);
                }
                if (countMod.MeteringOnTime.key.equals(x.getActCountMod())) {
                    x.setActCountMod(countMod.MeteringOnTime.value);
                }
            });
            return ResultUtil.success(trainFreeHeadDto);
        }
    }

    //????????????????????????
    @Override
    public Result saveFrozenFlag(String userId, VoSaveFrozenFlag vo) {
        User user = baseMapper.selectById(vo.getUserId());
        user.setFrozenFlag(vo.getFrozenFlag());
        user.setLastModifiedBy(userId);
        user.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(user);
        //??????
        return ResultUtil.success(user);
    }

    //??????????????????
    @Override
    public Result deleteUser(VoDeleteUser vo) {
        User u = new User();
        u.setId(vo.getUid());
        baseMapper.deleteById(u);
        //??????
        return ResultUtil.success();
    }

    public enum countMod {
        /**
         * ??????
         */
        Metering("1", "??????"),
        /**
         * ??????
         */
        Time("2", "??????"),

        /**
         * ????????????
         */
        MeteringOnTime("3", "????????????");
        public String key;
        public String value;


        countMod(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerUser2(RegisterRequest registerRequest) throws Exception {
        log.info("registerUser2 registerRequest:{{}}", registerRequest);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("mobile", registerRequest.getMobile());
        //   try {
        User user = baseMapper.selectOne(userQueryWrapper);
        log.info("registerUser2 user:{{}}", user);
        if (StringUtils.isEmpty(user)) {
            //??????????????????
            user = new User();
            user.setId(UUIDUtil.getShortUUID());
            BeanUtils.copyProperties(registerRequest, user);
            if (StringUtils.isNotEmpty(registerRequest.getPassword())) {
                user.setPassword(Md5Utils.md5(registerRequest.getPassword()));
            }
            user.setRegistTime(DateUtils.getCurrentDateTimeStr());
            user.setFrozenFlag("0");
            user.setDeleted("0");
            user.setCreateDate(LocalDateTime.now());
            user.setCreateBy(user.getId());
            if (PlatformEnum.QUINNOID.getName().equals(registerRequest.getPlatform())) {
                // TODO: 2022/1/2 ??????????????????????????????????????????????????????????????????controller???????????????
                QueryWrapper<User> queryWrapper = new QueryWrapper();
                queryWrapper.eq("sid", registerRequest.getSid());
                queryWrapper.eq("deleted", 0);
                queryWrapper.select("mobile", "deleted",
                        "password", "id", "nickName", "frozenFlag",
                        "vipStatus", "masterId", "vipBeginDate", "vipEndDate");
                User login = baseMapper.selectOne(queryWrapper);
                log.info("registerUser login:{{}}", login);
                if (null == login) {
                    user.setVipBeginDate(LocalDate.now());
                    user.setVipEndDate(LocalDate.now().plusYears(1));
                    user.setVipStatus(VipEnum.FAMILY_MASTER.getCode());
                    user.setSid(registerRequest.getSid());
                }
            }
            log.info("registerUser2 user:{{}}", user);
            baseMapper.insert(user);
        } else {
            BeanUtils.copyProperties(registerRequest, user);
            if (StringUtils.isNotEmpty(registerRequest.getPassword())) {
                user.setPassword(Md5Utils.md5(registerRequest.getPassword()));
            }
            baseMapper.updateById(user);
        }


        //????????????????????????
        if (StringUtils.isNotEmpty(registerRequest.getUserDetailRequest())) {
            registerRequest.getUserDetailRequest().setUserId(user.getId());
            iUserDetailService.addOrUpdateUserDetail(registerRequest.getUserDetailRequest());
        }

        //??????????????????????????????????????????????????????
        List<TbOrder> tbOrderList = tbOrderMapper.selectList(new QueryWrapper<TbOrder>()
                .eq("mobile", user.getMobile()).isNull("userId"));
        if (tbOrderList.size() > 0) {
            for (TbOrder tbOrder : tbOrderList) {
                tbOrder.setUserId(user.getId());
                tbOrderMapper.updateById(tbOrder);
            }
            List<UserReceiveAddressEntity> userReceiveAddressEntities = userReceiveAddressMapper.selectList(new QueryWrapper<UserReceiveAddressEntity>()
                    .eq("phone", user.getMobile()).isNull("userId"));
            for (UserReceiveAddressEntity userReceiveAddressEntity : userReceiveAddressEntities) {
                userReceiveAddressEntity.setUserId(user.getId());
                userReceiveAddressMapper.updateById(userReceiveAddressEntity);
            }
        }
        return user;
        //   } catch (Exception e) {
        //      throw new CustomResultException("????????????");
        //  }
    }


    @Override
    public User getUserByMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", mobile);
        queryWrapper.eq("deleted", 0);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Result updateUserWxInfo(VoSaveUserWxInfo vo) {
        log.info("updateUserWxInfo vo:{{}}", vo);
        User user = baseMapper.selectById(vo.getUserId());
        user.setId(vo.getUserId());
        user.setWxNickname(vo.getWxNickname());
        if (StringUtils.isNotEmpty(vo)) {
            //??????????????????
            Attach attach = iAttachService.saveAttach(vo.getUserId(), vo.getHeadAttachUrl().getPicAttachName()
                    , vo.getHeadAttachUrl().getPicAttachNewName(), vo.getHeadAttachUrl().getPicAttachSize());
            String attachId = attach.getId();
            log.info("updateUserWxInfo attachId:{{}}", attachId);
            user.setWxAvatar(attachId);
        }
        baseMapper.updateById(user);
        log.info("updateUserWxInfo user:{{}}", user);
        return ResultUtil.success(user);
    }

    @Override
    public Result uploadWxPicture(VoSaveUserWxPicInfo vo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ????????????????????????????????????
        File directory = new File("web");
        String path1 = directory.getPath();
        log.info("picture uploadPicture:{{}}",path1);
        String path = directory.getCanonicalPath() + "/upload/";
        log.info("uploadPicture path:{{}}",path);
        // ?????????????????????????????????????????????(??????????????????)
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        request.setCharacterEncoding("utf-8"); // ????????????
        JSONArray jsonArray = new JSONArray();
        String imgName= "";
        String destPath ="";
        String newName = "";
        User user = null;
        try {
            // ??????????????????????????????
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            Iterator<String> iterator = req.getFileNames();
            while (iterator.hasNext()) {
                HashMap<String, Object> res = new HashMap<String, Object>();
                MultipartFile file = req.getFile(iterator.next());
                // ???????????????
                String fileNames = file.getOriginalFilename();
                log.info("uploadPicture fileNames:{{}}",fileNames);
                int split = fileNames.lastIndexOf(".");
                // ???????????????????????????
                String extName = fileNames.substring(split + 1, fileNames.length());
                log.info("uploadPicture extName:{{}}",extName);
                //??????UUID
                String uuid = UUID.randomUUID().toString().replace("-", "");
                // ????????????????????????
                newName = uuid + "." + extName;
                log.info("uploadPicture newName:{{}}",newName);
                res.put("url", newName);
                jsonArray.add(res);
                // ?????????????????????????????????
                imgName= jsonArray.toString();
                destPath = path + newName;
                log.info("uploadPicture destPath:{{}}",destPath);
                // ???????????????
                // ???????????????????????????????????????????????????????????????file?????????
                File file1 = new File(destPath);
                OutputStream out = new FileOutputStream(file1);
                out.write(file.getBytes());
                out.close();
            }
            log.info("uploadPicture userId:{{}}",vo.getUserId());
            user = baseMapper.selectById(vo.getUserId());
            log.info("uploadPicture user:{{}}",user);
            user.setSmallRoutineHeadPic(httpsDomain+"/upload/"+newName);
            log.info("uploadPicture smallRoutineHeadPic:{{}}",httpsDomain+"/upload/"+newName);
            baseMapper.updateById(user);
        } catch (Exception e) {
            log.error("??????????????????", e);
        }
        log.info("uploadPicture httpsDomain:",httpsDomain);
        return ResultUtil.success(user.getSmallRoutineHeadPic());
        // ??????????????????????????????
   /*     PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        printWriter.write(imgName);
        printWriter.flush();*/
    }


}
