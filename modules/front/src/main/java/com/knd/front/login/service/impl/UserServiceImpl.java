package com.knd.front.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.Md5Utils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.PlatformEnum;
import com.knd.common.em.VipEnum;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.mapper.VerifyCodeMapper;
import com.knd.front.entity.*;
import com.knd.front.live.entity.UserCoachCourseEntity;
import com.knd.front.live.entity.UserCoachEntity;
import com.knd.front.live.entity.UserCoachTimeEntity;
import com.knd.front.live.mapper.UserCoachCourseMapper;
import com.knd.front.live.mapper.UserCoachMapper;
import com.knd.front.live.mapper.UserCoachTimeMapper;
import com.knd.front.login.dto.*;
import com.knd.front.login.entity.BaseEquipment;
import com.knd.front.login.mapper.*;
import com.knd.front.login.request.*;
import com.knd.front.login.service.IUserDetailService;
import com.knd.front.login.service.IUserService;
import com.knd.front.social.entity.UserLabelEntity;
import com.knd.front.social.mapper.UserLabelMapper;
import com.knd.front.user.mapper.UserReceiveAddressMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final UserMapper userMapper;
    private final VerifyCodeMapper verifyCodeMapper;
    private final IUserDetailService iUserDetailService;
    private final VipMenuMapper vipMenuMapper;
    private final ShapeAndHobbyMapper shapeAndHobbyMapper;
    private final BaseFrequencyMapper frequencyMapper;
    private final BaseSportMapper sportMapper;
    private final TbOrderMapper tbOrderMapper;
    private final UserReceiveAddressMapper userReceiveAddressMapper;
    private final BaseEquipmentMapper baseEquipmentMapper;
    private final UserCoachMapper userCoachMapper;
    private final UserCoachTimeMapper userCoachTimeMapper;
    private final UserCoachCourseMapper userCoachCourseMapper;
    private final UserLabelMapper userLabelMapper;

    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    @Override
    public User insertReturnEntity(User entity) {
        return null;
    }

    @Override
    public User updateReturnEntity(User entity) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result registerUser(RegisterRequest registerRequest) {
        log.info("---------------我要开始注册啦-----------------------------");
        log.info("registerUser registerRequest:{{}}", registerRequest);
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("mobile", registerRequest.getMobile());
        userWrapper.eq("deleted", 0);
        //验证手机号是否存在
        User mobile = userMapper.selectOne(userWrapper);
        log.info("registerUser mobile:{{}}", mobile);
        if (mobile != null) {
            return ResultUtil.error(ResultEnum.EXIST_PHONE_ERROR);
        }
        QueryWrapper<VerifyCode> verifyCodeWrapper = new QueryWrapper();
        verifyCodeWrapper.eq("mobile", registerRequest.getMobile());
        verifyCodeWrapper.eq("id", registerRequest.getVerifyCodeId());
        verifyCodeWrapper.eq("code", registerRequest.getCode());
        verifyCodeWrapper.eq("deleted", 0);
        verifyCodeWrapper.eq("codeType", 10);
        List<VerifyCode> verifyCodeList = verifyCodeMapper.selectList(verifyCodeWrapper);
        log.info("registerUser verifyCodeList:{{}}", verifyCodeList);
        //查询验证码是否有效
        if (StringUtils.isEmpty(verifyCodeList)) {
            return ResultUtil.error(ResultEnum.VERIFY_CODE_ERROR);
        }
        //查询验证码是否失效
        for (VerifyCode verifyCode : verifyCodeList) {
            if (verifyCode.getExpireTime().isBefore(DateUtils.getCurrentLocalDateTime())) {
                return ResultUtil.error(ResultEnum.CODE_TIME_OUT);
            }
        }
        try {
            //删除验证码
            verifyCodeMapper.updateByPrimaryKeyList(verifyCodeList);
            //新增用户数据
            User user = new User();
            BeanUtils.copyProperties(registerRequest, user);
            user.setId(UUIDUtil.getShortUUID());
            user.setSid(null);
            user.setDeleted("0");
            user.setMobile(registerRequest.getMobile());
            if (StringUtils.isNotEmpty(registerRequest.getPassword())) {
                user.setPassword(Md5Utils.md5(registerRequest.getPassword()));
            }
            user.setRegistTime(DateUtils.getCurrentDateTimeStr());
            user.setVipStatus(VipEnum.ORDINARY_VIP.getCode());
            user.setFrozenFlag("0");
            //TODO 注册送永久会员
            if("Quinnoid".equals(registerRequest.getPlatform())){
                QueryWrapper<User> queryWrapper = new QueryWrapper();
                queryWrapper.eq("sid", registerRequest.getSid());
                queryWrapper.eq("deleted", 0);
                queryWrapper.select("mobile", "deleted",
                        "password", "id", "nickName", "frozenFlag",
                        "vipStatus", "masterId", "vipBeginDate", "vipEndDate");
                User login = baseMapper.selectOne(queryWrapper);
                log.info("registerUser login:{{}}",login);
                if(null==login){
                    user.setVipBeginDate(LocalDate.now());
                    user.setVipEndDate(LocalDate.now().plusYears(100));
                    user.setVipStatus(VipEnum.INDIVIDUAL.getCode());
                    user.setSid(registerRequest.getSid());
                }
            }
            log.info("registerUser user:{{}}", user);

            userMapper.insert(user);

            //维护用户详情数据
            if (StringUtils.isNotEmpty(registerRequest.getUserDetailRequest())) {
                registerRequest.getUserDetailRequest().setUserId(user.getId());
                iUserDetailService.addOrUpdateUserDetail(registerRequest.getUserDetailRequest());
            }

            //关联该手机号未注册前的订单及收货地址
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

            UserIdDto userIdDto = new UserIdDto();
            userIdDto.setUserId(user.getId());
            log.info("registerUser userIdDto:{{}}", userIdDto);
            log.info("---------------我注册结束啦-----------------------------");
            return ResultUtil.success(userIdDto);
        } catch (Exception e) {
            throw new CustomResultException("注册失败");
        }
    }

    @Override
    public User createVirtualAccount(String userId) {
        User user = baseMapper.selectById(userId);
        User virtualUser = new User();
        if (StringUtils.isNotEmpty(user)) {
            BeanUtils.copyProperties(user,virtualUser);
            boolean b = true;
            while (b) {
                String virtualAccount= String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
                virtualUser.setVirtualAccount(virtualAccount);
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("virtualAccount", virtualAccount);
                userQueryWrapper.eq("deleted", 0);
                User virtualuser = userMapper.selectOne(userQueryWrapper);
                if (StringUtils.isNotEmpty(virtualuser)) {
                   break;
                }
                String virtualPassword = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
                virtualUser.setVirtualPassword(virtualPassword);
                virtualUser.setParentUser(user.getId());
                virtualUser.setId(UUIDUtil.getShortUUID());
                baseMapper.insert(virtualUser);
                b =false;
            }
        }
        return virtualUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resetPassword(ResetRequest resetRequest, List existCode) {
        try {
            verifyCodeMapper.updateByPrimaryKeyList(existCode);
            return userMapper.updateReset(resetRequest);
        } catch (Exception e) {
            throw new CustomResultException("失败");
        }


    }

    @Override
    public Result changeVipType(ChangeVipTypeRequest changeVipTypeRequest) {
        log.info("changeVipType changeVipTypeRequest:{{}}", changeVipTypeRequest);
        User user = userMapper.selectById(changeVipTypeRequest.getUserId());
        QueryWrapper<User> slaveUserQueryWrapper = new QueryWrapper<>();
        slaveUserQueryWrapper.eq("masterId", changeVipTypeRequest.getUserId());
        slaveUserQueryWrapper.eq("deleted", 0);
        List<User> slaveUsers = userMapper.selectList(slaveUserQueryWrapper);
        log.info("changeVipType user:{{}}", user);
        VipMenu vipMenu = vipMenuMapper.selectById(changeVipTypeRequest.getVipMenuId());
        log.info("changeVipType vipMenu:{{}}", vipMenu);
        if (user != null) {
            if ("0".equals(vipMenu.getVipType())) {
                user.setVipStatus(vipMenu.getVipType());
            } else {
                if ("1".equals(vipMenu.getVipType())) {
                    log.info("changeVipType VipType:{{}}", vipMenu.getVipType());
                    user.setMasterId(null);
                    log.info("changeVipType user:{{}}", user);
                    //开通个人会员
                    //升级重置一年
                    if (user.getVipEndDate() != null && DateUtils.getCurrentLocalDate().isBefore(user.getVipEndDate()) && !VipEnum.FAMILY_SLAVE.getCode().equals(user.getVipStatus())) {
                        //升级会员
                        user.setVipEndDate(user.getVipEndDate().plusYears(1).plusDays(1));
                        user.setVipStatus(vipMenu.getVipType());
                        slaveUsers.stream().forEach(i -> {
                            log.info("changeVipType i:{{}}", i);
                            i.setMasterId("");
                            i.setVipBeginDate(null);
                            i.setVipEndDate(null);
                            i.setVipStatus(VipEnum.ORDINARY_VIP.getCode());
                            baseMapper.updateById(i);
                            log.info("changeVipType i:{{}}", i);
                        });
                        log.info("changeVipType user:{{}}", user);
                    } else {
                        //开通会员
                        user.setVipBeginDate(DateUtils.getCurrentLocalDate());
                        user.setVipEndDate(user.getVipBeginDate().plusYears(1).plusDays(1));
                        user.setVipStatus(vipMenu.getVipType());
                        slaveUsers.stream().forEach(i -> {
                            log.info("changeVipType i:{{}}", i);
                            i.setMasterId("");
                            i.setVipBeginDate(null);
                            i.setVipEndDate(null);
                            i.setVipStatus(VipEnum.ORDINARY_VIP.getCode());
                            baseMapper.updateById(i);
                            log.info("changeVipType i:{{}}", i);
                        });
                        log.info("changeVipType user:{{}}", user);
                    }
                } else if ("2".equals(vipMenu.getVipType())) {
                    log.info("changeVipType VipType:{{}}", vipMenu.getVipType());
                    user.setMasterId(null);
                    log.info("changeVipType user:{{}}", user);
                    //开通家庭会员
                    //升级重置一年
                    if (user.getVipEndDate() != null && DateUtils.getCurrentLocalDate().isBefore(user.getVipEndDate()) && !VipEnum.FAMILY_SLAVE.getCode().equals(user.getVipStatus())) {
                        //升级会员
                        user.setVipEndDate(user.getVipEndDate().plusYears(1).plusDays(1));
                        user.setVipStatus(vipMenu.getVipType());
                        slaveUsers.stream().forEach(i -> {
                            log.info("changeVipType slaveUsers:{{}}", i);
                            i.setVipEndDate(user.getVipEndDate());
                            i.setVipStatus(VipEnum.FAMILY_SLAVE.getCode());
                            baseMapper.updateById(i);
                            log.info("changeVipType slaveUsers:{{}}", i);
                        });
                        log.info("changeVipType user:{{}}", user);
                    } else {
                        //开通会员
                        user.setVipBeginDate(DateUtils.getCurrentLocalDate());
                        user.setVipEndDate(user.getVipBeginDate().plusYears(1).plusDays(1));
                        user.setVipStatus(vipMenu.getVipType());
                        slaveUsers.stream().forEach(i -> {
                            log.info("changeVipType slaveUsers:{{}}", i);
                            i.setVipBeginDate(DateUtils.getCurrentLocalDate());
                            i.setVipEndDate(user.getVipBeginDate());
                            i.setVipStatus(VipEnum.FAMILY_SLAVE.getCode());
                            baseMapper.updateById(i);
                            log.info("changeVipType slaveUsers:{{}}", i);
                        });
                        log.info("changeVipType user:{{}}", user);
                    }
                }
            }
            baseMapper.updateById(user);
            log.info("changeVipType user:{{}}", user);
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.NOT_PHONE_ERROR.getCode(), "会员或手机号不存在");
        }
    }

    @Override
    public Result checkBeforeBindingSecondary(BindingSecondaryRequest bindingSecondaryRequest) {
        String userId = UserUtils.getUserId();
        User masterUser = baseMapper.selectById(userId);
        if (VipEnum.FAMILY_MASTER.getCode().equals(masterUser.getVipStatus()) && DateUtils.getCurrentLocalDate().isBefore(masterUser.getVipEndDate())) {
            QueryWrapper<User> queryWrapper = new QueryWrapper();
            queryWrapper.eq("masterId", userId);
            queryWrapper.eq("deleted", "0");
            Integer integer = baseMapper.selectCount(queryWrapper);
            if (integer >= 3) {
                return ResultUtil.error(ResultEnum.VALID_ERROR.getCode(), "可绑定副会员数已满");
            }
            QueryWrapper<User> slaveUeryWrapper = new QueryWrapper();
            slaveUeryWrapper.eq("mobile", bindingSecondaryRequest.getSecondaryMobile());
            slaveUeryWrapper.eq("deleted", "0");
            User slaveUser = baseMapper.selectOne(slaveUeryWrapper);
            if (slaveUser == null) {
                return ResultUtil.error(ResultEnum.VALID_ERROR.getCode(), "待添加副会员号码尚未注册");
            }
            if (!VipEnum.ORDINARY_VIP.getCode().equals(slaveUser.getVipStatus())) {
                return ResultUtil.error(ResultEnum.VALID_ERROR.getCode(), "此账号已经添加");
            }

            return ResultUtil.success();

        } else {
            return ResultUtil.error(ResultEnum.VALID_ERROR.getCode(), "请开通家庭会员");
        }
    }

    @Override
    public Result bindingSecondary(BindingSecondaryRequest bindingSecondaryRequest) {
        Result result = checkBeforeBindingSecondary(bindingSecondaryRequest);
        if (!ResultEnum.SUCCESS.getCode().equals(result.getCode())) {
            return result;
        }
        if (bindingSecondaryRequest.getMajorMobile().equals(bindingSecondaryRequest.getSecondaryMobile())) {
            return ResultUtil.error("U0999", "主卡不能绑定自己账号");
        }
        QueryWrapper<User> majorUeryWrapper = new QueryWrapper();
        majorUeryWrapper.eq("mobile", bindingSecondaryRequest.getMajorMobile());
        majorUeryWrapper.eq("deleted", "0");
        User majorUery = baseMapper.selectOne(majorUeryWrapper);
        QueryWrapper<User> slaveUeryWrapper = new QueryWrapper();
        slaveUeryWrapper.eq("mobile", bindingSecondaryRequest.getSecondaryMobile());
        slaveUeryWrapper.eq("deleted", "0");
        User slaveUser = baseMapper.selectOne(slaveUeryWrapper);
        slaveUser.setMasterId(UserUtils.getUserId());
        slaveUser.setVipStatus(VipEnum.FAMILY_SLAVE.getCode());
        slaveUser.setVipBeginDate(majorUery.getVipBeginDate());
        slaveUser.setVipEndDate(majorUery.getVipEndDate());
        baseMapper.updateById(slaveUser);
        return ResultUtil.success();
    }

    @Override
    public Result unBindingSecondary(BindingSecondaryRequest bindingSecondaryRequest) {
        log.info("----------------家庭会员解绑副号开始啦----------------");
        QueryWrapper<User> slaveUeryWrapper = new QueryWrapper();
        slaveUeryWrapper.eq("mobile", bindingSecondaryRequest.getSecondaryMobile());
        slaveUeryWrapper.eq("deleted", "0");
        User slaveUser = baseMapper.selectOne(slaveUeryWrapper);
        log.info("unBindingSecondary slaveUser:{{}}", slaveUser);
        userMapper.update(
                null,
                Wrappers.<User>lambdaUpdate()
                        .set(User::getMasterId, "")
                        .set(User::getVipStatus, VipEnum.ORDINARY_VIP.getCode())
                        .set(User::getVipBeginDate, null)
                        .set(User::getVipEndDate, null)
                        .eq(User::getId, slaveUser.getId())
        );
        log.info("unBindingSecondary slaveUser:{{}}", slaveUser);
        log.info("----------------家庭会员解绑副号结束啦----------------");
        return ResultUtil.success();
    }

    @Override
    public Result getBindingSecondaryList(getBindingSecondaryListRequest getBindingSecondaryListRequest) {
        List<BindingSecondaryListDto> dtoList = new ArrayList<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("masterId", UserUtils.getUserId());
        queryWrapper.eq("deleted", "0");
        queryWrapper.select("id", "nickName", "mobile");
        List<User> users = baseMapper.selectList(queryWrapper);
        for (User u : users) {
            BindingSecondaryListDto dto = new BindingSecondaryListDto();
            BeanUtils.copyProperties(u, dto);

            Result userDetail = iUserDetailService.getUserDetail(u.getId());
            UserDetailDto detail = (UserDetailDto) userDetail.getData();
            dto.setUserHeadUrl(detail.getHeadPicUrl());
            dtoList.add(dto);
        }
        return ResultUtil.success(dtoList);
    }

    @Override
    public Result getShareAndHobby() {
        List<HobbyDto> hobbyList = shapeAndHobbyMapper.getHobbyList();
        for (HobbyDto dto : hobbyList) {
            dto.setSelectUrlPath(fileImagesPath + dto.getSelectUrlPath());
            dto.setUnSelectUrlPath(fileImagesPath + dto.getUnSelectUrlPath());
        }
        List<ShapeDto> manShapeList = shapeAndHobbyMapper.getShapeList("男");
        for (ShapeDto dto : manShapeList) {
            dto.setSelectUrlPath(fileImagesPath + dto.getSelectUrlPath());
            dto.setUnSelectUrlPath(fileImagesPath + dto.getUnSelectUrlPath());
        }
        List<ShapeDto> womanShapeList = shapeAndHobbyMapper.getShapeList("女");
        for (ShapeDto dto : womanShapeList) {
            dto.setSelectUrlPath(fileImagesPath + dto.getSelectUrlPath());
            dto.setUnSelectUrlPath(fileImagesPath + dto.getUnSelectUrlPath());
        }
        ShapeAndHobbyDto shapeAndHobbyDtoBuilder = ShapeAndHobbyDto.builder()
                .hobbyList(hobbyList)
                .manShapeList(manShapeList)
                .womanShapeList(womanShapeList).build();
        return ResultUtil.success(shapeAndHobbyDtoBuilder);
    }

    @Override
    public Result getSportAndFrequency() {
        List<SportDto> sportList = new ArrayList<>();
        List<FrequencyDto> frequencyList = new ArrayList<>();
        List<BaseFrequencyEntity> baseFrequencyEntities = frequencyMapper.selectList(new QueryWrapper<BaseFrequencyEntity>().eq("deleted", "0").orderByAsc("frequency"));
        for (BaseFrequencyEntity entity : baseFrequencyEntities) {
            FrequencyDto frequencyDto = new FrequencyDto();
            BeanUtils.copyProperties(entity, frequencyDto);
            frequencyList.add(frequencyDto);
        }
        List<BaseSportEntity> baseSportEntities = sportMapper.selectList(new QueryWrapper<BaseSportEntity>().eq("deleted", "0"));
        for (BaseSportEntity entity : baseSportEntities) {
            SportDto sportDto = new SportDto();
            BeanUtils.copyProperties(entity, sportDto);
            sportList.add(sportDto);
        }

        SportAndFrequencyDto dto = SportAndFrequencyDto.builder()
                .sportList(sportList)
                .frequencyList(frequencyList).build();
        return ResultUtil.success(dto);
    }

    @Override
    public Result getEquipmentList() {
        List<EquipmentListDto> dtoList = new ArrayList<>();
        List<BaseEquipment> equipmentList = baseEquipmentMapper.selectList(new QueryWrapper<BaseEquipment>().eq("deleted", 0));
        for (BaseEquipment equipment : equipmentList) {
            EquipmentListDto dto = new EquipmentListDto();
            BeanUtils.copyProperties(equipment, dto);
            dtoList.add(dto);
        }
        return ResultUtil.success(dtoList);
    }


    public User sendToMembers(LoginRequest loginRequest) {
        log.info("------------------------送一年会员开始---------------------------");
        log.info("loginAccount loginRequest:{{}}", loginRequest);
        log.info("loginAccount platform:{{}}", loginRequest.getPlatform());
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        //用户绑定的机器编号
        queryWrapper.eq("sid", loginRequest.getSid());
        queryWrapper.eq("deleted", "0");
        log.info("loginAccount sid:{{}}", loginRequest.getSid());
        User sidLogin = baseMapper.selectOne(queryWrapper);
        log.info("loginAccount sidLogin:{{}}", sidLogin);
        QueryWrapper<User> queryWrapper1 = new QueryWrapper();
        //手机号
        queryWrapper1.eq("mobile", loginRequest.getMobile());
        queryWrapper1.eq("deleted", "0");
        User mobileLogin = baseMapper.selectOne(queryWrapper1);
        log.info("loginAccount mobileLogin:{{}}", mobileLogin);
        //大屏首次登录赠送一年会员
        if (null == sidLogin && PlatformEnum.QUINNOID.getName().equals(loginRequest.getPlatform())) {
            log.info("--------------------------机器首次激活登陆，送一年会员--------------------------------");
            //机器首次激活登陆，送一年会员
            if (null == mobileLogin.getVipBeginDate()) {
                log.info("loginAccount VipBeginDate:{{}}", LocalDate.now());
                mobileLogin.setVipBeginDate(LocalDate.now());
            } else {
                log.info("loginAccount VipBeginDate:{{}}", mobileLogin.getVipBeginDate().plusYears(1));
                mobileLogin.setVipEndDate(mobileLogin.getVipBeginDate().plusYears(1));
            }
            if (null == mobileLogin.getVipEndDate()) {
                log.info("loginAccount VipEndDate:{{}}", LocalDate.now().plusYears(1));
                mobileLogin.setVipEndDate(LocalDate.now().plusYears(1));
            } else {
                log.info("loginAccount VipEndDate:{{}}", mobileLogin.getVipEndDate().plusYears(1));
                mobileLogin.setVipEndDate(mobileLogin.getVipEndDate().plusYears(1));
            }
            //2家庭会员-主
            mobileLogin.setVipStatus(VipEnum.FAMILY_MASTER.getCode());
            mobileLogin.setSid(loginRequest.getSid());
            log.info("loginAccount mobileLogin:{{}}", mobileLogin);
            baseMapper.updateById(mobileLogin);
        }
        log.info("------------------------送一年会员结束---------------------------");
        return mobileLogin;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerUser2(RegisterRequest registerRequest) {
        log.info("----------------------------我要开始注册啦----------------------");
        try {
            //新增用户数据
            User user = new User();
            user.setId(UUIDUtil.getShortUUID());
            BeanUtils.copyProperties(registerRequest, user);
            if (StringUtils.isNotEmpty(registerRequest.getPassword())) {
                user.setPassword(Md5Utils.md5(registerRequest.getPassword()));
            }
            user.setRegistTime(DateUtils.getCurrentDateTimeStr());
            user.setDeleted("0");
            user.setFrozenFlag("0");
            if (PlatformEnum.ANDROID_PLATFORM.getName().equals(registerRequest.getPlatform())) {
                // TODO: 2022/1/2 如果注册只能通过登陆实现，这里的代码就跟登陆controller里面重复了
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
                    user.setVipStatus("2");
                    user.setSid(registerRequest.getSid());
                }
            }
            log.info("registerUser user:{{}}", user);
            userMapper.insert(user);

            //维护用户详情数据
            if (StringUtils.isNotEmpty(registerRequest.getUserDetailRequest())) {
                registerRequest.getUserDetailRequest().setUserId(user.getId());
                iUserDetailService.addOrUpdateUserDetail(registerRequest.getUserDetailRequest());
            }

            //关联该手机号未注册前的订单及收货地址
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
        } catch (Exception e) {
            throw new CustomResultException("注册失败");
        }
    }

    @Override
    public Result logout(LogoutRequest logoutRequest) {
        baseMapper.deleteById(logoutRequest.getUserId());

        QueryWrapper<UserCoachEntity> userCoachEntityQueryWrapper = new QueryWrapper<>();
        userCoachEntityQueryWrapper.eq("userId", logoutRequest.getUserId());
        userCoachEntityQueryWrapper.eq("deleted", "0");
        UserCoachEntity userCoachEntity = userCoachMapper.selectOne(userCoachEntityQueryWrapper);
        if (StringUtils.isNotEmpty(userCoachEntity)) {
            UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(userCoachEntity.getId());
            if (StringUtils.isNotEmpty(userCoachCourseEntity)) {
                userCoachCourseEntity.setDeleted("1");
                userCoachCourseEntity.setLastModifiedBy(logoutRequest.getUserId());
                userCoachCourseEntity.setLastModifiedDate(LocalDateTime.now());
                userCoachCourseMapper.updateById(userCoachCourseEntity);
                UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(new QueryWrapper<UserCoachTimeEntity>().eq("coachCourseId", userCoachCourseEntity.getId()).eq("deleted", "0"));
                if (StringUtils.isNotEmpty(userCoachTimeEntity)) {
                    userCoachTimeEntity.setDeleted("1");
                    userCoachTimeEntity.setLastModifiedBy(logoutRequest.getUserId());
                    userCoachTimeEntity.setLastModifiedDate(LocalDateTime.now());
                    userCoachTimeMapper.updateById(userCoachTimeEntity);
                }
            }
        }
        QueryWrapper<UserLabelEntity> userLabelQueryWrapper = new QueryWrapper<>();
        userLabelQueryWrapper.eq("userId", logoutRequest.getUserId());
        userLabelQueryWrapper.eq("deleted", logoutRequest.getUserId());
        userLabelMapper.delete(userLabelQueryWrapper);
        return ResultUtil.success();
    }


    @Override
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    @Override
    public User getUserByMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", mobile);
        queryWrapper.eq("deleted", 0);
        return userMapper.selectOne(queryWrapper);
    }
}