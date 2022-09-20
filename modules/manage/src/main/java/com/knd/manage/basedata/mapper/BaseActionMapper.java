package com.knd.manage.basedata.mapper;

import com.knd.manage.basedata.dto.ActionInfoDto;
import com.knd.manage.basedata.entity.BaseAction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface BaseActionMapper extends BaseMapper<BaseAction> {
    //全部模糊查询所有
    List<ActionInfoDto> selectAllByLike(@Param("actionType") String actionType,@Param("target") String target, @Param("part") String part, @Param("action") String action);

    //分页
    Page<ActionInfoDto> selectPageByLike(Page<ActionInfoDto> page,@Param("actionType") String actionType, @Param("target") String target, @Param("part") String part, @Param("action") String action);

    //检查是否有未删除的动作使用该目标
    int selectActionCountByTargetId(String id);

    //检查是否有未删除的动作使用该部位
    int selectActionCountByPartId(String id);

    //根据日期分组每天得自由训练数量
    @Select("SELECT day_list.day as date,IFNULL(data.allNum, 0) as count from  " +
            " (SELECT " +
            "   a.time time, " +
            "   a.allNum allNum " +
            " FROM " +
            "   ( " +
            "   SELECT " +
            "     a.time, " +
            "     SUM( b.count ) AS allNum  " +
            "   FROM " +
            "     (SELECT str_to_date(createDate,'%Y-%m-%d') as time , count(id) as count FROM base_action where deleted=0 GROUP BY  time ) a " +
            "     JOIN (  SELECT str_to_date(createDate,'%Y-%m-%d') as time , count(id) as count FROM base_action where deleted=0 GROUP BY  time) b ON a.time >= b.time  " +
            "   GROUP BY " +
            "     time  " +
            "   ORDER BY " +
            "   time ASC  " +
            "   ) a) data " +
            " right join  " +
            " (SELECT @date := DATE_ADD(@date, interval 1 day) day from  " +
            " (SELECT @date := DATE_ADD(#{beginDate}, interval -1 day) from base_action) " +
            "  days limit #{days}) day_list on day_list.day = data.time ORDER BY date ASC")
    List<Map<String, Object>> queryFreeTrainingTotal(Integer days, String beginDate, String endDate);

}
