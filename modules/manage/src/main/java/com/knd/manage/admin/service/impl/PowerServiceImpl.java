package com.knd.manage.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.manage.admin.dto.PowerListDto;
import com.knd.manage.admin.entity.Power;
import com.knd.manage.admin.entity.RolePower;
import com.knd.manage.admin.mapper.PowerMapper;
import com.knd.manage.admin.mapper.RolePowerMapper;
import com.knd.manage.admin.service.IPowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.manage.admin.dto.ButtonDto;
import com.knd.manage.admin.dto.MpowerDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-17
 */
@Service
public class PowerServiceImpl extends ServiceImpl<PowerMapper, Power> implements IPowerService {

    @Override
    public Power insertReturnEntity(Power entity) {
        return null;
    }

    @Override
    public Power updateReturnEntity(Power entity) {
        return null;
    }

    @Resource
    private RolePowerMapper rolePowerMapper;

    //查询权限信息
    @Override
    public Result getPowerList(String roleId) {
        //查询所有pageId不重复的权限信息
        QueryWrapper<Power> qw = new QueryWrapper<>();
        qw.eq("deleted", "0");
        qw.select("moduleName", "moduleId", "pageName", "pageId");
        qw.orderByAsc("length(moduleId)","moduleId","length(pageName)", "pageName");
        qw.groupBy("pageId");
        List<Power> lp = baseMapper.selectList(qw);
        PowerListDto powerListDto = new PowerListDto();
        List<MpowerDto> lm = new ArrayList<>();
        if (!lp.isEmpty()) {
            //是否需要检查角色权限
            boolean powerFlag = false;
            //临时存储角色的权限id
            List<RolePower> lrp = null;
            if (StringUtils.isNotEmpty(roleId)) {
                //获取该角色所有的权限id
                QueryWrapper<RolePower> qw2 = new QueryWrapper<>();
                qw2.eq("roleId", roleId);
                qw2.eq("deleted", "0");
                qw2.select("powerId");
                lrp = rolePowerMapper.selectList(qw2);
                if (!lrp.isEmpty()) {
                    powerFlag = true;
                }
            }
            for (Power p : lp) {
                //根据页面id获取所有按钮信息
                qw.clear();
                qw.eq("deleted", "0");
                qw.eq("pageId", p.getPageId());
                qw.select("buttonName", "buttonId", "id");
                qw.orderByAsc("length(id + 0)","id + 0");
                List<Power> lb = baseMapper.selectList(qw);
                //拼接出参格式
                MpowerDto m = new MpowerDto();
                m.setModuleName(p.getModuleName());
                m.setModuleId(p.getModuleId());
                m.setPageName(p.getPageName());
                m.setPageId(p.getPageId());
                List<ButtonDto> mlb = new ArrayList<>();
                if (!lb.isEmpty()) {
                    for (Power p2 : lb) {
                        ButtonDto b = new ButtonDto();
                        b.setId(p2.getId());
                        b.setButtonName(p2.getButtonName());
                        b.setButtonId(p2.getButtonId());
                        b.setPowerFlag("0");
                        if (powerFlag) {
                            for (RolePower rp : lrp) {
                                if (rp.getPowerId().equals(p2.getId())) {
                                    b.setPowerFlag("1");
                                    break;
                                }
                            }
                        }
                        mlb.add(b);
                    }
                }
                m.setButtonList(mlb);
                lm.add(m);
            }
        }
        powerListDto.setPowerList(lm);
        return ResultUtil.success(powerListDto);
    }
}
