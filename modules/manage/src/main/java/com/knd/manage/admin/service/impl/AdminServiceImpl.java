package com.knd.manage.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.password.PasswordHash;
import com.knd.common.password.PasswordUtil;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.admin.dto.AdminDto;
import com.knd.manage.admin.dto.AdminListDto;
import com.knd.manage.admin.dto.MAdminDto;
import com.knd.manage.admin.dto.RoleDto;
import com.knd.manage.admin.entity.Admin;
import com.knd.manage.admin.entity.AdminRole;
import com.knd.manage.admin.entity.RefreshPasswordRecord;
import com.knd.manage.admin.entity.Role;
import com.knd.manage.admin.mapper.*;
import com.knd.manage.admin.service.IAdminService;
import com.knd.manage.admin.vo.VoRole;
import com.knd.manage.admin.vo.VoSaveAdmin;
import com.knd.manage.admin.vo.VoSaveFrozenFlag;
import com.knd.manage.basedata.entity.UserCoachEntity;
import com.knd.manage.basedata.entity.UserLabel;
import com.knd.manage.basedata.mapper.UserCoachMapper;
import com.knd.manage.basedata.mapper.UserLabelMapper;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.course.entity.UserCoachCourseEntity;
import com.knd.manage.course.mapper.UserCoachCourseMapper;
import com.knd.manage.mall.entity.UserCoachTimeEntity;
import com.knd.manage.mall.mapper.UserCoachTimeMapper;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.mapper.UserMapper;
import com.knd.mybatis.SuperServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-09
 */
@Log4j2
@Service
@Transactional
public class AdminServiceImpl extends SuperServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private AdminRoleMapper adminRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserLabelMapper userLabelMapper;
    @Resource
    private AdminLoginMapper adminLoginMapper;
    @Resource
    private UserCoachMapper userCoachMapper;
    @Resource
    private UserCoachTimeMapper userCoachTimeMapper;
    @Resource
    private UserCoachCourseMapper userCoachCourseMapper;
    @Resource
    private RefreshPasswordRecordMapper refreshPasswordRecordMapper;

    @Override
    public Result insertAdmin(Admin admin) {
//        // 如果密码为空 则默认密码
//        String password = admin.getPassword();
//        if (StringUtils.isEmpty(admin.getPassword())) {
//            password = "123456";
//        }
//        //盐值加密
//        PasswordHash passwordHash = PasswordUtil.encrypt(password);
//        admin.setPassword(passwordHash.getHexEncoded());
//        admin.setSalt(passwordHash.getSalt());
//        admin.setCreateBy(UserUtils.getUserId());
//        admin.setCreateDate(LocalDateTime.now());
//        admin.setDeleted("0"); // 未删除
//        return ResultUtil.success(insertReturnEntity(admin));
        return null;
    }

    @Override
    public Result updateAdmin(Admin admin) {
//        Admin existAdmin = baseMapper.selectById(admin.getId());
//        if (existAdmin == null) {
//            ResultUtil.error("0001", "用户不存在");
//        }
//        admin.setLastModifiedBy(UserUtils.getUserId());
//        admin.setLastModifiedDate(LocalDateTime.now());
//        baseMapper.updateById(admin);
//        return ResultUtil.success();
        return null;
    }

    //新增
    @Override
    public Result add(VoSaveAdmin vo) {
        //查重用户名
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("userName", vo.getUserName());
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        //密码加密，并获取加密后的数据和盐值
        PasswordHash passwordHash = PasswordUtil.encrypt(vo.getPassword());
        Admin a = new Admin();
        String adminId = UUIDUtil.getShortUUID();
        a.setId(adminId);
        a.setUserName(vo.getUserName());
        a.setPassword(passwordHash.getHexEncoded());
        a.setSalt(passwordHash.getSalt());
        a.setMobile(vo.getMobile());
        a.setNickName(vo.getNickName());
        a.setCreateBy(vo.getUserId());
        a.setCreateDate(LocalDateTime.now());
        a.setLastModifiedBy(vo.getUserId());
        a.setLastModifiedDate(LocalDateTime.now());
        a.setDeleted("0");
        a.setFrozenFlag("0");
        a.setAreaId(vo.getAreaId());
        baseMapper.insert(a);
        if (vo.getRoleList() != null && !vo.getRoleList().isEmpty()) {
            AdminRole ar = new AdminRole();
            ar.setUserId(adminId);
            ar.setCreateBy(vo.getUserId());
            ar.setCreateDate(LocalDateTime.now());
            ar.setLastModifiedBy(vo.getUserId());
            ar.setLastModifiedDate(LocalDateTime.now());
            ar.setDeleted("0");
            //有绑定角色数据
            for (VoRole r : vo.getRoleList()) {
                if (StringUtils.isEmpty(r.getRoleId())) {
                    continue;
                }
                ar.setId(UUIDUtil.getShortUUID());
                ar.setRoleId(r.getRoleId());
                adminRoleMapper.insert(ar);
            }
        }
        //成功
        return ResultUtil.success();
    }

    //更新
    @Override
    public Result edit(VoSaveAdmin vo) {
        //根据id获取名称
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("id", vo.getAdminId());
        qw.eq("deleted", "0");
        qw.select("userName");
        Admin ad = baseMapper.selectOne(qw);
        if (ad == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ad.getUserName().equals(vo.getUserName())) {
            //查重
            qw.clear();
            qw.eq("userName", vo.getUserName());
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        Admin a = new Admin();
        a.setId(vo.getAdminId());
        a.setUserName(vo.getUserName());
        a.setMobile(vo.getMobile());
        a.setNickName(vo.getNickName());
        a.setLastModifiedBy(vo.getUserId());
        a.setLastModifiedDate(LocalDateTime.now());
        a.setAreaId(vo.getAreaId());
        if (vo.getPassword() != null && !StringUtils.isEmpty(vo.getPassword())) {
            //不空，则修改密码
            //密码加密，并获取加密后的数据和盐值
            PasswordHash passwordHash = PasswordUtil.encrypt(vo.getPassword());
            a.setPassword(passwordHash.getHexEncoded());
            a.setSalt(passwordHash.getSalt());
        }
        baseMapper.updateById(a);
        //获取用户权限
        QueryWrapper<AdminRole> qwr = new QueryWrapper<>();
        qwr.eq("userId", vo.getAdminId());
        qwr.eq("deleted", "0");
        qwr.select("roleId");
        List<AdminRole> lrp = adminRoleMapper.selectList(qwr);
        //存储原来的角色id集合
        List<String> lrpm = new ArrayList<>();
        //备份原来的角色id集合
        List<String> temp = new ArrayList<>();
        if (!lrp.isEmpty()) {
            for (AdminRole r : lrp) {
                lrpm.add(r.getRoleId());
                temp.add(r.getRoleId());
            }
        }
        //获取传入的角色数据
        List<String> roleList = new ArrayList<>();
        if (vo.getRoleList() != null && !vo.getRoleList().isEmpty()) {
            //去掉 ""数据 并 去重
            Set<String> set = new HashSet<>();
            for (VoRole r : vo.getRoleList()) {
                String str = (r.getRoleId()).trim();
                if (!"".equals(str) && set.add(str)) {
                    roleList.add(str);
                }
            }
        }
        //差集计算
        //需要删除的集合
        lrpm.removeAll(roleList);
        //需要新增的集合
        roleList.removeAll(temp);
        //删除操作
        if (!lrpm.isEmpty()) {
            UpdateWrapper<AdminRole> up = new UpdateWrapper<>();
            AdminRole r = new AdminRole();
            r.setDeleted("1");
            r.setLastModifiedDate(LocalDateTime.now());
            for (String str : lrpm) {
                up.clear();
                up.eq("userId", vo.getAdminId());
                up.eq("deleted", "0");
                up.eq("roleId", str);
                adminRoleMapper.update(r, up);
            }
        }
        //新增操作
        if (!roleList.isEmpty()) {
            AdminRole rp = new AdminRole();
            rp.setUserId(vo.getAdminId());
            rp.setCreateBy(vo.getUserId());
            rp.setCreateDate(LocalDateTime.now());
            rp.setLastModifiedBy(vo.getUserId());
            rp.setLastModifiedDate(LocalDateTime.now());
            rp.setDeleted("0");
            for (String str : roleList) {
                rp.setId(UUIDUtil.getShortUUID());
                rp.setRoleId(str);
                adminRoleMapper.insert(rp);
            }
        }
        //成功
        return ResultUtil.success();
    }

    //更新用户冻结状态
    @Override
    public Result saveFrozenFlag(VoSaveFrozenFlag vo) {
        log.info("saveFrozenFlag vo:{{}}",vo);
        User user = userMapper.selectById(vo.getUserId());
        user.setFrozenFlag(vo.getFrozenFlag());
        user.setLastModifiedBy(vo.getUserId());
        user.setLastModifiedDate(LocalDateTime.now());
        log.info("saveFrozenFlag user:{{}}",user);
        userMapper.updateById(user);
        Admin b = new Admin();
        b.setId(vo.getId());
        b.setFrozenFlag(vo.getFrozenFlag());
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        log.info("saveFrozenFlag Admin:{{}}",b);
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success(user);
    }

    //删除用户
    @Override
    public Result deleteAdmin(VoId vo) {
        Admin b = new Admin();
        b.setId(vo.getId());
        b.setDeleted("1");
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());

        baseMapper.updateById(b);
        QueryWrapper<UserCoachEntity> userCoachEntityQueryWrapper = new QueryWrapper<>();
        userCoachEntityQueryWrapper.eq("userId", vo.getUserId());
        userCoachEntityQueryWrapper.eq("deleted", "0");
        UserCoachEntity userCoachEntity = userCoachMapper.selectOne(userCoachEntityQueryWrapper);
        if (StringUtils.isNotEmpty(userCoachEntity)) {
            UserCoachCourseEntity userCoachCourseEntity = userCoachCourseMapper.selectById(userCoachEntity.getId());
            if (StringUtils.isNotEmpty(userCoachCourseEntity)) {
                userCoachCourseEntity.setDeleted("1");
                userCoachCourseEntity.setLastModifiedBy(vo.getUserId());
                userCoachCourseEntity.setLastModifiedDate(LocalDateTime.now());
                userCoachCourseMapper.updateById(userCoachCourseEntity);
                UserCoachTimeEntity userCoachTimeEntity = userCoachTimeMapper.selectOne(new QueryWrapper<UserCoachTimeEntity>().eq("coachCourseId", userCoachCourseEntity.getId()).eq("deleted", "0"));
                if (StringUtils.isNotEmpty(userCoachTimeEntity)) {
                    userCoachTimeEntity.setDeleted("1");
                    userCoachTimeEntity.setLastModifiedBy(vo.getUserId());
                    userCoachTimeEntity.setLastModifiedDate(LocalDateTime.now());
                    userCoachTimeMapper.updateById(userCoachTimeEntity);
                }
            }
        }
        QueryWrapper<UserLabel> userLabelQueryWrapper = new QueryWrapper<>();
        userLabelQueryWrapper.eq("userId",vo.getUserId());
        userLabelQueryWrapper.eq("deleted",vo.getUserId());
        userLabelMapper.delete(userLabelQueryWrapper);
        //成功
        return ResultUtil.success();
    }

    //查询用户
    @Override
    public Result getAdmin(String id) {
        //查询该用户的基本信息
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("deleted", "0");
        qw.eq("id", id);
        qw.select("id", "userName", "nickName", "mobile","areaId");
        Admin admin = baseMapper.selectOne(qw);
        //查询该用户的角色
        QueryWrapper<AdminRole> arqw = new QueryWrapper<>();
        arqw.eq("deleted", "0");
        arqw.eq("userId", id);
        arqw.select("roleId");
        List<AdminRole> lar = adminRoleMapper.selectList(arqw);
        //查询所有角色列表
        QueryWrapper<Role> rqw = new QueryWrapper<>();
        rqw.eq("deleted", "0");
        rqw.select("id", "name");
        List<Role> lr = roleMapper.selectList(rqw);
        //拼接出参格式
        AdminDto adminDto = new AdminDto();
        adminDto.setId(id);
        adminDto.setUserName(admin.getUserName());
        adminDto.setNickName(admin.getNickName());
        adminDto.setMobile(admin.getMobile());
        adminDto.setFrozenFlag(admin.getFrozenFlag());
        adminDto.setAreaId(admin.getAreaId());
        List<RoleDto> roleDtos = new ArrayList<>();
        if (!lr.isEmpty()) {
            for (Role er : lr) {
                RoleDto r = new RoleDto();
                r.setRoleId(er.getId());
                r.setName(er.getName());
                r.setRoleFlag("0");
                if (!lar.isEmpty()) {
                    //不空，需要检查角色flag
                    for (AdminRole ar : lar) {
                        if (ar.getRoleId().equals(er.getId())) {
                            r.setRoleFlag("1");
                            break;
                        }
                    }
                }
                roleDtos.add(r);
            }
        }
        adminDto.setRoleList(roleDtos);
        //成功
        return ResultUtil.success(adminDto);
    }

    //查询用户列表
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result getAdminList(String userName, String nickName, String mobile, String frozenFlag, String current) {
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(userName)) {
            //不为空时
            qw.like("userName", userName);
        }
        if (StringUtils.isNotEmpty(nickName)) {
            //不为空时
            qw.like("nickName", nickName);
        }
        if (StringUtils.isNotEmpty(mobile)) {
            //不为空时
            qw.like("mobile", mobile);
        }
        if (StringUtils.isNotEmpty(frozenFlag)) {
            //不为空时
            qw.eq("frozenFlag", frozenFlag);
        }
        qw.select("id", "userName", "nickName", "mobile", "frozenFlag");
        qw.eq("deleted", "0");
        qw.orderByAsc("length(userName)","userName");
        List<Admin> list;
        AdminListDto adminListDto = new AdminListDto();
        if (StringUtils.isEmpty(current)) {
            //获取全部
            list = baseMapper.selectList(qw);
            //总数据条数
            adminListDto.setTotal(list.size());
        } else {
            //分页
            Page<Admin> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
            partPage = baseMapper.selectPage(partPage, qw);
            //获取全部
            list = partPage.getRecords();
            //总数据条数
            adminListDto.setTotal((int) partPage.getTotal());
        }
        List<MAdminDto> lm = new ArrayList<>();
        if (!list.isEmpty()) {
            for (Admin a : list) {
                MAdminDto m = new MAdminDto();
                m.setId(a.getId());
                m.setUserName(a.getUserName());
                m.setNickName(a.getNickName());
                m.setMobile(a.getMobile());
                m.setFrozenFlag(a.getFrozenFlag());
                //根据用户id获取关联的角色名称，以逗号分隔
                List<String> ar = adminRoleMapper.selectNamelist(a.getId());
                if (!ar.isEmpty()) {
                    StringBuilder s = new StringBuilder();
                    for (String str : ar) {
                        if (StringUtils.isEmpty(str)) {
                            continue;
                        }
                        s.append(str).append(",");
                    }
                    if (StringUtils.isNotEmpty(s)) {
                        m.setRoles(s.substring(0, s.length() - 1));
                    }
                }
                LocalDateTime maxLoginTime = adminLoginMapper.getMaxLoginTime(a.getId());
                m.setLoginTime(maxLoginTime);
                lm.add(m);
            }
        }
        adminListDto.setAdminList(lm);
        //成功
        return ResultUtil.success(adminListDto);
    }


    @Override
    public Result updateMaintenanceWorkerPassword(String userId) {
        QueryWrapper<RefreshPasswordRecord> refreshPasswordRecordQueryWrapper = new QueryWrapper<>();
        refreshPasswordRecordQueryWrapper.eq("DATE_FORMAT(createDate,'%Y-%m-%d')", LocalDate.now());
        refreshPasswordRecordQueryWrapper.eq("deleted", "0");
        refreshPasswordRecordQueryWrapper.last("limit 1");
        RefreshPasswordRecord refreshPasswordRecord = refreshPasswordRecordMapper.selectOne(refreshPasswordRecordQueryWrapper);
        if(refreshPasswordRecord == null) {
            refreshPasswordRecord = new RefreshPasswordRecord();
            String randomCode = String.format("%04d", new Random().nextInt(9999));
            refreshPasswordRecord.setCode(randomCode);
            refreshPasswordRecord.setCreateBy(userId);
            refreshPasswordRecord.setCreateDate(LocalDateTime.now());
            refreshPasswordRecord.setLastModifiedBy(userId);
            refreshPasswordRecord.setLastModifiedDate(LocalDateTime.now());
            refreshPasswordRecord.setDeleted("0");
            refreshPasswordRecordMapper.insert(refreshPasswordRecord);
        }


        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name","维修员");
        queryWrapper.eq("deleted", "0");
        Role role = roleMapper.selectOne(queryWrapper);
        QueryWrapper<AdminRole> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("roleId",role.getId());
        queryWrapper1.eq("deleted","0");
        List<AdminRole> adminRoles = adminRoleMapper.selectList(queryWrapper1);
        for(AdminRole adminRole : adminRoles) {
            Admin admin = baseMapper.selectById(adminRole.getUserId());
            if(!refreshPasswordRecord.getCode().equals(admin.getPassword())) {
                admin.setPassword(refreshPasswordRecord.getCode());
                baseMapper.updateById(admin);
            }
        }

        return ResultUtil.success(refreshPasswordRecord);
    }


}
