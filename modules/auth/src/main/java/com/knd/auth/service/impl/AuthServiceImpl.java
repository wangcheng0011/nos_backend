package com.knd.auth.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.auth.dto.AuthUrlDto;
import com.knd.auth.dto.LoginRequestDto;
import com.knd.auth.dto.OrderConsultingDTO;
import com.knd.auth.dto.PowerDto;
import com.knd.auth.entity.*;
import com.knd.auth.mapper.*;
import com.knd.auth.service.AttachService;
import com.knd.auth.service.IAuthService;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.RoleEnum;
import com.knd.common.password.PasswordUtil;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.permission.bean.AuthUrl;
import com.knd.permission.bean.Token;
import com.knd.permission.config.TokenManager;
import com.knd.redis.jedis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {

    @Resource
    protected TokenManager tokenManager;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EquipmentInfoMapper equipmentInfoMapper;

    @Resource
    private AttachService attachService;

    @Resource
    private OrderConsultingMapper orderConsultingMapper;

    @Autowired
    private RedisClient redisClient;

    /**
     * app终端token有效期
     */
    private static final int EXPIRE_SECOND_WEB = 1 * 30 * 60;

    @Override
    @Transactional
    public Result login(LoginRequestDto dto) throws Exception {
        Token token = new Token();
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", dto.getUserName());
        queryWrapper.eq("deleted", "0");
        Admin admin = adminMapper.selectOne(queryWrapper);
        log.info("login admin:{{}}",admin);
        if (admin == null) {
            return ResultUtil.error("0001", "用户不存在");
        } else if (!PasswordUtil.checkPassword(dto.getPassword(), admin.getSalt(), admin.getPassword())) {
            log.info("login checkPassword:{{}}",PasswordUtil.checkPassword(dto.getPassword(), admin.getSalt(), admin.getPassword()));
            return ResultUtil.error("0002", "帐号或密码错误");
        } else if (admin.getFrozenFlag().equals("1")) {
            return ResultUtil.error("0003", "帐号冻结");
        } else {
            String platform = "1"; // 管理端
            BeanUtils.copyProperties(admin, token);
            // 前端权限
            Set<String> roleList = new HashSet<>();
            Set<String> permissionList = new HashSet<>();
            Set<AuthUrl> authUrlList = new HashSet<>();

            roleList = adminMapper.selectRoleList(admin.getId());
            if(roleList.contains(RoleEnum.CUSTOMER_SERVICE.getName())) {
                token.setRoleFlag(RoleEnum.CUSTOMER_SERVICE.getCode());
            }else if(roleList.contains(RoleEnum.SALE.getName())) {
                token.setRoleFlag(RoleEnum.SALE.getCode());
            }else if(roleList.contains(RoleEnum.ERECTOR.getName())) {
                token.setRoleFlag(RoleEnum.ERECTOR.getCode());
            }else if(roleList.contains(RoleEnum.DOMESTIC_CONSUMER.getName())) {
                token.setRoleFlag(RoleEnum.DOMESTIC_CONSUMER.getCode());
            }
            // 将页面权限、后台权限 塞入token
            List<PowerDto> powerDtoList = adminMapper.selectPowerList(admin.getId());
            if (!powerDtoList.isEmpty() && powerDtoList.get(0) != null) {
                powerDtoList.stream().forEach(t -> {
                    if (StringUtils.isNotEmpty(t)) {
                        permissionList.add(t.getButtonId());
                    }
//                    permissionList.add(t.getModuleId());
//                    permissionList.add(t.getPageId());

                });
            }
            token.setPermissionList(permissionList);

            //后端权限
            List<AuthUrlDto> authUrlDtoList = adminMapper.queryAuthUrlList(admin.getId());
            if (authUrlDtoList != null && authUrlDtoList.size() > 0) {
                authUrlDtoList.stream().forEach(t -> {
                    AuthUrl authUrl = new AuthUrl();
                    BeanUtils.copyProperties(t, authUrl);
                    authUrlList.add(authUrl);
                });
            }
            token.setAuthUrlList(authUrlList);
            token.setUserId(admin.getId());
            token.setPlatform(platform);
            String tokenCode = tokenManager.putToken(token);
            token.setToken(tokenCode);
        }
        return ResultUtil.success(token);
    }

    @Override
    public Result checkToken(String token) throws Exception {
        return ResultUtil.success(tokenManager.checkToken(token));
    }

    @Override
    public Result queryToken() throws Exception {
        return ResultUtil.success(tokenManager.queryToken(UserUtils.getTokenCode()));
    }

    @Override
    public Result queryAuthUrlList(String userId) {
        return ResultUtil.success(adminMapper.queryAuthUrlList(userId));
    }

    @Override
    public Token queryTokenInfo(String token) throws Exception {
        log.info("queryTokenInfo token:{{}}",token);
        return tokenManager.queryToken(token);
    }

    //检查用户账号状态
    @Override
    public String queryUserState(String userId) {
        log.info("queryUserState userId:{{}}",userId);
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.select("deleted", "frozenFlag");
        qw.eq("id", userId);
        qw.eq("deleted", "0");
        qw.last(" limit 0,1");
        User u = userMapper.selectOne(qw);
        log.info("queryUserState User:{{}}",u);
//        1:冻结，2：删除，3：正常
        if (u == null || StringUtils.isEmpty(u.getDeleted()) || "1".equals(u.getDeleted())) {
            log.info("queryUserState status:{{}}",2);
            //删除
            return "2";
        }
        if (StringUtils.isNotEmpty(u.getFrozenFlag()) && "1".equals(u.getFrozenFlag())) {
            log.info("queryUserState status:{{}}",1);
            //冻结
            return "1";
        }
        log.info("queryUserState status:{{}}",3);
        //正常
        return "3";
    }

    @Override
    public synchronized EquipmentInfo queryEquipmentNo(String equipmentNo) throws Exception {
        String equipmentInfoStr = redisClient.get(equipmentNo);
        EquipmentInfo equipmentInfo = JSONObject.parseObject(equipmentInfoStr,EquipmentInfo.class);
        if(equipmentInfo != null && "1".equals(equipmentInfo.getStatus())) {
            redisClient.set(equipmentNo,equipmentInfoStr);
            return equipmentInfo;
        }
//        synchronized (this) {
            QueryWrapper<EquipmentInfo> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.eq("equipmentNo",equipmentNo);
            objectQueryWrapper.eq("deleted","0");
            //objectQueryWrapper.eq("status","1");

            EquipmentInfo equipmentInfo1 = equipmentInfoMapper.selectOne(objectQueryWrapper);
            if (equipmentInfo1 != null && "1".equals(equipmentInfo1.getStatus())) {
                redisClient.set(equipmentNo, JSONObject.toJSONString(equipmentInfo1));
                return equipmentInfo1;
            } else {
                if (equipmentInfo1 == null) {
                    EquipmentInfo newEquipmentInfo = new EquipmentInfo();
                    newEquipmentInfo.setEquipmentNo(equipmentNo);
                    newEquipmentInfo.setStatus("0");
                    newEquipmentInfo.setCreateBy("");
                    newEquipmentInfo.setCreateDate(LocalDateTime.now());
                    newEquipmentInfo.setLastModifiedBy("");
                    newEquipmentInfo.setLastModifiedDate(LocalDateTime.now());
                    newEquipmentInfo.setDeleted("0");
                    equipmentInfoMapper.insert(newEquipmentInfo);
                }
                return null;
            }
//        }
    }

    @Override
    public Result info(String userId) throws Exception {

        User user = userMapper.selectById(userId);

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setCreateTime(LocalDateTime.now());
        tokenInfo.setDeleted("0");
        tokenInfo.setAvatar("");
        tokenInfo.setCreatorId(userId);
        tokenInfo.setLastLoginIp("");
        tokenInfo.setName("");
        tokenInfo.setUsername(user.getNickName());
        tokenInfo.setPassword(user.getPassword());
        tokenInfo.setTelephone(user.getMobile());
        tokenInfo.setStatus(user.getFrozenFlag());
        List<RoleInfo> roleInfos = new ArrayList<>();

        List<PermissionInfo> permissionInfos = new ArrayList<>();

        Token token = new Token();
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("userName", user.getNickName());
        adminQueryWrapper.eq("deleted",0);
        Admin admin = adminMapper.selectOne(adminQueryWrapper);


            // 前端权限
            Set<String> roleList = new HashSet<>();
            Set<String> permissionList = new HashSet<>();
            Set<AuthUrl> authUrlList = new HashSet<>();

            roleList = adminMapper.selectRoleList(admin.getId());
            RoleInfo roleInfo = new RoleInfo();

            if(roleList.contains(RoleEnum.CUSTOMER_SERVICE.getName())) {
                tokenInfo.setRoleId(RoleEnum.CUSTOMER_SERVICE.getCode());
                roleInfo.setName(RoleEnum.CUSTOMER_SERVICE.getName());
                roleInfo.setId(RoleEnum.CUSTOMER_SERVICE.getCode());
            }else if(roleList.contains(RoleEnum.SALE.getName())) {
                tokenInfo.setRoleId(RoleEnum.SALE.getCode());
                roleInfo.setName(RoleEnum.SALE.getName());
                roleInfo.setId(RoleEnum.SALE.getCode());
            }else {
                tokenInfo.setRoleId(RoleEnum.ERECTOR.getCode());
                roleInfo.setName(RoleEnum.ERECTOR.getName());
                roleInfo.setId(RoleEnum.ERECTOR.getCode());
            }

            // 将页面权限、后台权限 塞入token
            List<PowerDto> powerDtoList = adminMapper.selectPowerList(admin.getId());

            if (!powerDtoList.isEmpty() && powerDtoList.get(0) != null) {
                powerDtoList.stream().forEach(t -> {
                    if (StringUtils.isNotEmpty(t)) {
                        PermissionInfo permissionInfo = new PermissionInfo();
                        permissionInfo.setRoleId(tokenInfo.getRoleId());
                        permissionInfo.setPermissionnName("");
                        permissionInfo.setPermissionId(t.getButtonId());
                        ArrayList<ActionEntity> actionEntities = new ArrayList<>();
                        //后端权限
                        List<AuthUrlDto> authUrlDtoList = adminMapper.queryAuthUrlList(admin.getId());
                        if (authUrlDtoList != null && authUrlDtoList.size() > 0) {
                            authUrlDtoList.stream().forEach(a -> {
                                ActionEntity actionEntity = new ActionEntity();
                                actionEntity.setAction(a.getUrl());
                                actionEntity.setDescribe(a.getMethod());
                                actionEntities.add(actionEntity);
                            });
                        }
                        permissionInfo.setActionEntitySet(actionEntities);
                        permissionInfos.add(permissionInfo);
                        roleInfo.setPermissions(permissionInfos);
                    }
                });
            }
        roleInfos.add(roleInfo);
        tokenInfo.setRole(roleInfos);
        return ResultUtil.success(tokenInfo);
    }


    @Override
    public Result addOrUpdateOrderConsulting(OrderConsultingDTO orderConsultingRequest) {
        OrderConsultingEntity orderConsultingEntity = new OrderConsultingEntity();
        copyProperties(orderConsultingRequest,orderConsultingEntity);
        orderConsultingEntity.setDeleted("0");
        orderConsultingEntity.setPicAttachId(orderConsultingRequest.getPictureUrlId());
        log.info("addOrUpdateOrderConsulting PictureUrl",orderConsultingRequest.getPictureUrl());
        log.info("addOrUpdateOrderConsulting PicAttachName",orderConsultingRequest.getPictureUrl().getPicAttachName());
        log.info("addOrUpdateOrderConsulting PicAttachNewName",orderConsultingRequest.getPictureUrl().getPicAttachNewName());
        log.info("addOrUpdateOrderConsulting PicAttachSize",orderConsultingRequest.getPictureUrl().getPicAttachSize());
        if(StringUtils.isEmpty(orderConsultingRequest.getPictureUrlId())){
            //保存选中图片
            Attach attach = attachService.saveAttach(orderConsultingRequest.getUserId(), orderConsultingRequest.getPictureUrl().getPicAttachName()
                    , orderConsultingRequest.getPictureUrl().getPicAttachNewName(), orderConsultingRequest.getPictureUrl().getPicAttachSize());
            String attachId = attach.getId();
            log.info("addOrUpdateOrderConsulting attachId",attachId);
            orderConsultingEntity.setPicAttachId(attachId);
        }
        if(null==orderConsultingEntity.getId()){
            orderConsultingEntity.setId(UUIDUtil.getShortUUID());
            orderConsultingEntity.setCreateBy(orderConsultingRequest.getUserId());
            orderConsultingEntity.setCreateDate(LocalDateTime.now());
            orderConsultingMapper.insert(orderConsultingEntity);
        }else {
            orderConsultingEntity.setLastModifiedBy(orderConsultingRequest.getUserId());
            orderConsultingEntity.setLastModifiedDate(LocalDateTime.now());
            orderConsultingMapper.updateById(orderConsultingEntity);
        }

        return ResultUtil.success(orderConsultingEntity);
    }


}
