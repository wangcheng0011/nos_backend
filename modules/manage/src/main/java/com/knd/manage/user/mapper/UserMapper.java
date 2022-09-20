package com.knd.manage.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.knd.manage.user.dto.TrainDto;
import com.knd.manage.user.dto.UserDto;
import com.knd.manage.user.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
public interface UserMapper extends BaseMapper<User> {
    //查询注册会员列表
    IPage<UserDto> selectPageBySome(IPage<UserDto> page, @Param("nickName") String nickName,
                                    @Param("mobile") String mobile, @Param("frozenFlag") String frozenFlag,
                                    @Param("registTimeBegin") Date registTimeBegin, @Param("registTimeEnd") Date registTimeEnd);

    //查询注册会员训练列表
    IPage<TrainDto> selectTrainPageBySome(IPage<TrainDto> page, @Param("nickName") String nickName, @Param("mobile") String mobile,
                                          @Param("equipmentNo") String equipmentNo, @Param("trainTimeBegin") Date trainTimeBegin,
                                          @Param("trainTimeEnd") Date trainTimeEnd);

    //查询注册会员训练列表--课程训练
    IPage<TrainDto> selectTrainPageBySome2(IPage<TrainDto> page, @Param("nickName") String nickName, @Param("mobile") String mobile,
                                          @Param("equipmentNo") String equipmentNo, @Param("trainTimeBegin") Date trainTimeBegin,
                                          @Param("trainTimeEnd") Date trainTimeEnd);
    //查询注册会员训练列表--自由训练
    IPage<TrainDto> selectTrainPageBySome3(IPage<TrainDto> page, @Param("nickName") String nickName, @Param("mobile") String mobile,
                                           @Param("equipmentNo") String equipmentNo, @Param("trainTimeBegin") Date trainTimeBegin,
                                           @Param("trainTimeEnd") Date trainTimeEnd);

    @Select(" SELECT " +
            " COUNT(*) AS total," +
            " SUM(CASE  WHEN vipStatus = 0 THEN 1 ELSE 0 END) AS  noneVip," +
            " 100.0 * SUM(CASE  WHEN vipStatus = 0 THEN 1 ELSE 0 END)/COUNT(*) AS noneVipPercent," +
            " SUM(CASE  WHEN vipStatus IN(1,2,3) THEN 1 ELSE 0 END) AS vip ," +
            "100.0 * SUM(CASE  WHEN vipStatus IN(1,2,3) THEN 1 ELSE 0 END)/COUNT(*) AS vipPercent" +
            " FROM user")
    Map<String,Double> queryPercentOfVip();


    /**
     * 获取付费用户总数
     * @return
     */
    @Select("select count(DISTINCT userId)  from tb_order where orderType=1 and status=2 and deleted=0")
    String getPayingUser();

    /**
     * 获取每个月付费用户总数
     * @param beginDate
     * @param endDate
     * @return
     */
    @Select("select count(DISTINCT userId)  from tb_order " +
            "where orderType=1 and status=2 and deleted=0 and paymentTime BETWEEN #{beginDate} and #{endDate}")
    int getPayingUserGrowth(@Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);

    /**
     * 续费用户总数
     * @return
     */
    @Select("select count(1) from (select userId,count(1)  from tb_order where orderType=1 and status=2 and deleted=0 GROUP BY userId HAVING count(1) >1) a")
    String getRenewalUser();

    /**
     * 每个月续费用户数
     * @param beginDate
     * @param endDate
     * @return
     */
    @Select("select count(DISTINCT userId) from tb_order  " +
            "where orderType=1 and status=2 and deleted=0 and paymentTime BETWEEN #{beginDate} and #{endDate}  " +
            "AND userId in(select distinct userId from tb_order where orderType=1 and status=2 and deleted=0 and paymentTime < #{beginDate})")
    int getRenewalUserByMonth(@Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);

    @Select(" SELECT " +
            "COUNT(u.id) AS total," +
            "SUM(CASE  WHEN d.gender = 1 THEN 1 ELSE 0 END) AS  man," +
            "100.0 * SUM(CASE  WHEN d.gender = 1 THEN 1 ELSE 0 END) / COUNT(u.id) AS manPercent," +
            "SUM(CASE  WHEN d.gender = 2 THEN 1 ELSE 0 END) AS woman ," +
            "100.0 * SUM(CASE  WHEN d.gender = 2 THEN 1 ELSE 0 END) / COUNT(u.id) AS womanPercent " +
            "FROM user u,user_detail d where u.id=d.userId")
    Map<String,Double> queryGenderPercent();

    @Select("select (case" +
            " when a.age between 0 and 18 then '0-18岁'" +
            " when a.age between 18 and 28 then '18-28岁'" +
            " when a.age between 29 and 39 then '29-39岁'" +
            " when a.age between 40 and 50 then '40-50岁'" +
            " when a.age between 50 and 60 then '50-60岁'" +
            " when a.age >= 60 then '60岁以上'" +
            " end) as name," +
          " count(*) as count" +
            //" count(*)/(select count(id) from user_detail)*100 percent" +
            " from (select timestampdiff(year, str_to_date(birthDay,'%Y-%m-%d'),CURDATE()) age from user_detail) a" +
            " group by name")
    List<Map<String,Double>> queryAgePercent();

    @Select(" SELECT day_list.day as date,IFNULL(data.allNum, 0) as count from \n" +
            "(SELECT\n" +
            "    a.time time,\n" +
            "   -- a.count '每天增加',\n" +
            "    a.allNum allNum\n" +
            "FROM\n" +
            "    (\n" +
            "    SELECT\n" +
            "        a.time,\n" +
            "        -- a.count,\n" +
            "        SUM( b.count ) AS allNum \n" +
            "\t\t\t -- (a.count/b.count) AS allNum \n" +
            "    FROM\n" +
            "        (SELECT str_to_date(registTime,'%Y-%m-%d') as time , count(id) as count FROM user GROUP BY  time ) a\n" +
            "        JOIN (  SELECT str_to_date(registTime,'%Y-%m-%d') as time , count(id) as count FROM user GROUP BY  time) b ON a.time >= b.time \n" +
            "    GROUP BY\n" +
            "        time \n" +
            "    ORDER BY\n" +
            "    time ASC \n" +
            "    ) a) data\n" +
            "right join \n" +
            "(SELECT @date := DATE_ADD(@date, interval 1 day) day from \n" +
            "(SELECT @date := DATE_ADD(#{beginDate}, interval -1 day) from user)\n" +
            " days limit #{days}) day_list on day_list.day = data.time ORDER BY date ASC")
    List<Map<String, Object>> queryRegistUserGrowthOfweeks(Integer days, String beginDate, String endDate);

    @Select(" SELECT day_list.day as date,IFNULL(data.allNum, 0) as count from \n" +
            "(SELECT\n" +
            "    a.time time,\n" +
            "   -- a.count '每天增加',\n" +
            "    a.allNum allNum\n" +
            "FROM\n" +
            "    (\n" +
            "    SELECT\n" +
            "        a.time,\n" +
            "        -- a.count,\n" +
            "        SUM( b.count ) AS allNum \n" +
            "\t\t\t -- (a.count/b.count) AS allNum \n" +
            "    FROM\n" +
            "        (SELECT vipBeginDate as time , count(id) as count FROM user where vipStatus='1' GROUP BY  time ) a\n" +
            "        JOIN (  SELECT vipBeginDate as time , count(id) as count FROM user where vipStatus='1' GROUP BY  time) b ON a.time >= b.time \n" +
            "    GROUP BY\n" +
            "        time \n" +
            "    ORDER BY\n" +
            "    time ASC \n" +
            "    ) a) data\n" +
            "right join \n" +
            "(SELECT @date := DATE_ADD(@date, interval 1 day) day from \n" +
            "(SELECT @date := DATE_ADD(#{beginDate}, interval -1 day) from user)\n" +
            " days limit #{days}) day_list on day_list.day = data.time ORDER BY date ASC")
    List<Map<String, Object>> queryVipUserGrowthOfweeks(Integer days, String beginDate, String endDate);

    @Select(" SELECT day_list.day as date,IFNULL(data.allNum, 0) as count from \n" +
            "(SELECT " +
            "    a.time time,\n" +
            "    a.allNum allNum\n" +
            "FROM\n" +
            "    (\n" +
            "    SELECT\n" +
            "        a.time,\n" +
            "        -- a.count,\n" +
            "        SUM( b.count ) AS allNum \n" +
            "\t\t\t -- (a.count/b.count) AS allNum \n" +
            "    FROM\n" +
            "        (SELECT vipBeginDate as time , count(id) as count FROM user where vipStatus='2' GROUP BY  time ) a\n" +
            "        JOIN (  SELECT vipBeginDate as time , count(id) as count FROM user where vipStatus='2' GROUP BY  time) b ON a.time >= b.time \n" +
            "    GROUP BY\n" +
            "        time \n" +
            "    ORDER BY\n" +
            "    time ASC \n" +
            "    ) a) data\n" +
            "right join \n" +
            "(SELECT @date := DATE_ADD(@date, interval 1 day) day from \n" +
            "(SELECT @date := DATE_ADD(#{beginDate}, interval -1 day) from user)\n" +
            " days limit #{days}) day_list on day_list.day = data.time ORDER BY date ASC")
    List<Map<String, Object>> queryFamilyVipUserGrowthOfweeks(Integer days, String beginDate, String endDate);

    @Select("select a.goodsId,b.goodsName,a.quantity " +
            "from (select goodsId,sum(quantity) quantity from tb_order_item " +
            "GROUP BY goodsId) a,pms_goods b,pms_category c\n" +
            "where a.goodsId = b.id AND b.categoryId = c.id AND c.id='2' ORDER BY goodsName ASC")
    List<Map<String, Integer>> getPartsPurchase();

    @Select("select trd.faultDesc,count(id) count \n" +
            "from testing_report_detail trd \n" +
            "GROUP BY trd.faultDesc HAVING trd.faultDesc \n" +
            "NOT LIKE '%预留%' ORDER BY trd.faultDesc")
    List<Map<String, Object>> getFaultAnalysis();

    @Select(" SELECT \n" +
            "    SUM(CASE  WHEN type = 1 THEN 1 ELSE 0 END) AS  freetrain, \n" +
            "    SUM(CASE  WHEN type = 2 THEN 1 ELSE 0 END) AS course ,\n" +
            "\t\tSUM(CASE  WHEN type = 3 THEN 1 ELSE 0 END) AS trainProgram ,\n" +
            "\t\tSUM(CASE  WHEN type = 4 THEN 1 ELSE 0 END) AS seriesCourse ,\n" +
            "\t\tSUM(CASE  WHEN type = 5 THEN 1 ELSE 0 END) AS live \n" +
            "FROM view_record where createDate >=str_to_date(#{beginDate},'%Y-%m-%d') and createDate<=str_to_date(#{endDate},'%Y-%m-%d')")
    List<Map<String, Object>> getViewRecords(String beginDate, String endDate);

    @Select(" select c.goodsName partsName,count(c.id) count from (SELECT a.id,a.equipmentId,b.goodsName from base_action_equipment a,pms_goods b where a.equipmentId=b.id and a.deleted='0') c  GROUP BY c.goodsName")
    List<Map<String, Object>> getPartsUsageAnalysis();


}
