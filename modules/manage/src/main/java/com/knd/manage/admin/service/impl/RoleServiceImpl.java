package com.knd.manage.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.admin.dto.RoleListDto;
import com.knd.manage.admin.entity.Role;
import com.knd.manage.admin.entity.RolePower;
import com.knd.manage.admin.mapper.AdminRoleMapper;
import com.knd.manage.admin.mapper.RoleMapper;
import com.knd.manage.admin.mapper.RolePowerMapper;
import com.knd.manage.admin.service.IRoleService;
import com.knd.manage.admin.vo.VoSaveRole;
import com.knd.manage.admin.vo.VoSaveRolePower;
import com.knd.manage.common.vo.VoId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-17
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RolePowerMapper rolePowerMapper;

    @Resource
    private AdminRoleMapper adminRoleMapper;

    @Override
    public Role insertReturnEntity(Role entity) {
        return null;
    }

    @Override
    public Role updateReturnEntity(Role entity) {
        return null;
    }

    //新增
    @Override
    public Result add(VoSaveRole vo) {
        //查重
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.eq("name", vo.getName());
        qw.eq("deleted", "0");
        //获取总数
        int s = baseMapper.selectCount(qw);
        if (s != 0) {
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        Role role = new Role();
        String roleId = UUIDUtil.getShortUUID();
        role.setId(roleId);
        role.setName(vo.getName());
        role.setMemo(vo.getMemo());
        role.setCreateBy(vo.getUserId());
        role.setCreateDate(LocalDateTime.now());
        role.setLastModifiedBy(vo.getUserId());
        role.setLastModifiedDate(LocalDateTime.now());
        role.setDeleted("0");
        baseMapper.insert(role);
//        if (vo.getPowerList() != null && !vo.getPowerList().isEmpty()) {
//            //权限列表不空
//            RolePower rp = new RolePower();
//            rp.setRoleId(roleId);
//            rp.setCreateBy(vo.getUserId());
//            rp.setCreateDate(LocalDateTime.now());
//            rp.setLastModifiedBy(vo.getUserId());
//            rp.setLastModifiedDate(LocalDateTime.now());
//            rp.setDeleted("0");
//            for (String p : vo.getPowerList()) {
//                if (StringUtils.isEmpty(p)) {
//                    continue;
//                }
//                rp.setId(UUIDUtil.getShortUUID());
//                rp.setPowerId(p);
//                rolePowerMapper.insert(rp);
//            }
//        }
        //成功
        return ResultUtil.success();
    }

    //更新
    @Override
    public Result edit(VoSaveRole vo) {
        //根据id获取名称
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.eq("id", vo.getRoleId());
        qw.eq("deleted", "0");
        qw.select("name");
        Role ro = baseMapper.selectOne(qw);
        if (ro == null) {
            //没有该id的内容
            //参数异常，
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (!ro.getName().equals(vo.getName())) {
            //查重
            qw.clear();
            qw.eq("name", vo.getName());
            qw.eq("deleted", "0");
            //获取总数
            int s = baseMapper.selectCount(qw);
            if (s != 0) {
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        Role role = new Role();
        role.setId(vo.getRoleId());
        role.setName(vo.getName());
        role.setMemo(vo.getMemo());
        role.setLastModifiedBy(vo.getUserId());
        role.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(role);
        //成功
        return ResultUtil.success();
    }

    //删除角色
    @Override
    public Result deleteRole(VoId vo) {
        //根据角色id查看绑定的用户数量
        int count = adminRoleMapper.selectCountByAdminId(vo.getId());
        if (count != 0) {
            return ResultUtil.error(ResultEnum.ROLE_DELETE_ERROR);
        }
        Role b = new Role();
        b.setId(vo.getId());
        b.setDeleted("1");
        b.setLastModifiedBy(vo.getUserId());
        b.setLastModifiedDate(LocalDateTime.now());
        baseMapper.updateById(b);
        //成功
        return ResultUtil.success();
    }

    //查询角色信息
    @Override
    public Result getRole(String id) {
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.eq("deleted", "0");
        qw.eq("id", id);
        qw.select("id", "name", "memo");
        Role role = baseMapper.selectOne(qw);
        //成功
        return ResultUtil.success(role);
    }

    //查询角色列表
    @Override
    public Result getRoleList(String name, String current) {
        QueryWrapper<Role> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            //不为空时
            qw.like("name", name);
        }
        qw.select("id", "name", "memo");
        qw.eq("deleted", "0");
        qw.orderByAsc("length(name)","name");

        List<Role> list;
        RoleListDto roleListDto = new RoleListDto();
        if (StringUtils.isEmpty(current)) {
            //获取全部
            list = baseMapper.selectList(qw);
            //总数据条数
            roleListDto.setTotal(list.size());
        } else {
            //分页
            Page<Role> partPage = new Page<>(Integer.parseInt(current), PageInfo.pageSize);
            partPage = baseMapper.selectPage(partPage, qw);
            //获取全部
            list = partPage.getRecords();
            //总数据条数
            roleListDto.setTotal((int) partPage.getTotal());
        }
        roleListDto.setRoleList(list);
        //成功
        return ResultUtil.success(roleListDto);
    }

    //维护角色权限
    @Override
    public Result saveRolePower(VoSaveRolePower vo) {
        //获取用户权限
        QueryWrapper<RolePower> qwr = new QueryWrapper<>();
        qwr.eq("roleId", vo.getRoleId());
        qwr.eq("deleted", "0");
        qwr.select("powerId");
        List<RolePower> lrp = rolePowerMapper.selectList(qwr);
        List<String> lrpm = new ArrayList<>();
        //备份原来的权限数据
        List<String> temp = new ArrayList<>();
        if (!lrp.isEmpty()) {
            for (RolePower r : lrp) {
                lrpm.add(r.getPowerId());
                temp.add(r.getPowerId());
            }
        }
        //获取传入的权限数据
        List<String> powerList = vo.getPowerList();
        //去掉 ""数据 并 去重
        Iterator<String> it = powerList.iterator();
        Set<String> set = new HashSet<>();
        while (it.hasNext()) {
            String str = (it.next()).trim();
            if ("".equals(str) || !set.add(str)) {
                it.remove();
            }
        }
        //差集计算
        //需要删除的集合
        lrpm.removeAll(powerList);
        //需要新增的集合
        powerList.removeAll(temp);
        //删除操作
        if (!lrpm.isEmpty()) {
            UpdateWrapper<RolePower> up = new UpdateWrapper<>();
            RolePower r = new RolePower();
            r.setDeleted("1");
            for (String str : lrpm) {
                up.clear();
                up.eq("roleId", vo.getRoleId());
                up.eq("deleted", "0");
                up.eq("powerId", str);
                rolePowerMapper.update(r, up);
            }
        }
        //新增操作
        if (!powerList.isEmpty()) {
            RolePower rp = new RolePower();
            rp.setRoleId(vo.getRoleId());
            rp.setCreateBy(vo.getUserId());
            rp.setCreateDate(LocalDateTime.now());
            rp.setLastModifiedBy(vo.getUserId());
            rp.setLastModifiedDate(LocalDateTime.now());
            rp.setDeleted("0");
            for (String str : powerList) {
                rp.setId(UUIDUtil.getShortUUID());
                rp.setPowerId(str);
                rolePowerMapper.insert(rp);
            }
        }
        //成功
        return ResultUtil.success();
    }


}
