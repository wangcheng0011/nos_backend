package com.knd.manage.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.manage.mall.dto.InstallPersonDto;
import com.knd.manage.mall.entity.OrderIcEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderIcMapper extends BaseMapper<OrderIcEntity> {

    @Select("select a.id,a.nickName,IFNULL(d.count,0) as count1, IFNULL(e.count,0) as count2,IFNULL(f.count,0) as count3 " +
            " from admin a   " +
            " join admin_role b on a.id = b.userId and b.deleted=0  " +
            " join role c on b.roleId = c.id and c.deleted=0  " +
            " LEFT JOIN   " +
            " (select personId,count(1) as count   " +
            " from tb_order_ic as ic  " +
            " join tb_order as o on ic.tbOrderId = o.id " +
            " where o.installStatus =2 group by ic.personId) d on a.id = d.personId  " +
            " LEFT JOIN " +
            " (select personId,count(1) as count   " +
            " from tb_order_ic as ic  " +
            " join tb_order as o on ic.tbOrderId = o.id " +
            " where o.installStatus =4 group by ic.personId) e on a.id = e.personId  " +
            " LEFT JOIN  " +
            " (select personId,count(1) as count   " +
            " from tb_order_ic as ic  " +
            " join tb_order as o on ic.tbOrderId = o.id " +
            " where o.installStatus =3 group by ic.personId) f on a.id = f.personId  " +
            " where c.name = '技师' and c.deleted=0 and a.areaId = #{areaId} " +
            " group by a.id  " +
            " ORDER BY count1 asc")
    List<InstallPersonDto> getInstallPersonList(@Param("areaId") String areaId);

    @Select(" select o.lastModifiedDate from tb_order_ic as ic  " +
            " join tb_order as o on ic.tbOrderId = o.id " +
            " where o.installStatus in (1,2,3) and ic.personId = #{personId} " +
            " order by o.lastModifiedDate desc LIMIT 1")
    LocalDateTime getTime(@Param("personId") String personId);

}
