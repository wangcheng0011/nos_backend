package com.knd.front.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.knd.front.user.dto.UserTrainDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lenovo
 */
public interface UserTrainMapper {

    /**
     * 毅力
     * @param userId
     * @param beginDate
     * @param endDate
     * @return
     */
    @Select("select u.id as userId,u.nickName,pic.filePath as headPicUrl," +
            " IFNULL(SUM(total.actTrainSeconds),0) as trainNum,   " +
            " IFNULL(praise.praise,'1') as praise," +
            " IFNULL(praiseNum.num,'0') as praiseNum  " +
            "from user u " +
            "LEFT JOIN user_detail ud on u.id = ud.userId and ud.deleted=0 " +
            "LEFT JOIN attach pic on ud.headPicUrlId = pic.id and pic.deleted=0 " +
            "LEFT JOIN(   " +
            " select userId,actTrainSeconds from train_action_array_head    " +
            " where deleted = 0    " +
            " and createDate BETWEEN #{beginDate} and #{endDate}   " +
            " union all   " +
            " select userId,actualTrainSeconds from train_course_head_info    " +
            " where deleted = 0    " +
            " and createDate BETWEEN #{beginDate} and #{endDate}   " +
            " union all   " +
            " select userId,actTrainSeconds from train_free_head    " +
            " where deleted = 0    " +
            " and createDate BETWEEN #{beginDate} and #{endDate}   " +
            " ) total on total.userId = u.id   " +
            " left join train_user_praise praise    " +
            " on praise.UserId = #{userId}   " +
            " and praise.praiseUserId = u.id   " +
            " and praise.praiseType='1'  " +
            " and praise.createDate BETWEEN #{beginDate} and #{endDate}   " +
            " left join (   " +
            "  select praiseUserId,count(1) as num from train_user_praise   " +
            "  where createDate BETWEEN #{beginDate} and #{endDate}   " +
            "   and praise = '0' and praiseType='1' and deleted='0'   " +
            "  GROUP BY praiseUserId) praiseNum on u.id = praiseNum.praiseUserId   " +
            " WHERE u.deleted=0 " +
            " GROUP BY u.id ORDER BY trainNum desc,praiseNum desc LIMIT ${size}")
    List<UserTrainDto> getUserWill(@Param("userId") String userId,
                                       @Param("beginDate") LocalDateTime beginDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("size") Long size);

    /**
     * 拼接参数获取排行榜
     * @param userId
     * @param beginDate
     * @param endDate
     * @param paramType
     * @param praiseType
     * @return
     */
    @Select("select u.id as userId,u.nickName,pic.filePath as headPicUrl," +
            " IFNULL(SUM(total.${paramType}),0) as trainNum,  " +
            " IFNULL(praise.praise,'1') as praise," +
            " IFNULL(praiseNum.num,'0') as praiseNum  " +
            "from user u " +
            "LEFT JOIN user_detail ud on u.id = ud.userId and ud.deleted=0 " +
            "LEFT JOIN attach pic on ud.headPicUrlId = pic.id and pic.deleted=0 " +
            "LEFT JOIN(  " +
            " select userId,${paramType} from train_action_array_head   " +
            " where deleted = 0   " +
            " and createDate BETWEEN #{beginDate} and #{endDate}  " +
            "union all  " +
            " select userId,${paramType} from train_course_head_info   " +
            " where deleted = 0   " +
            " and createDate BETWEEN #{beginDate} and #{endDate}  " +
            "union all  " +
            " select userId,${paramType} from train_free_head   " +
            " where deleted = 0   " +
            " and createDate BETWEEN #{beginDate} and #{endDate}  " +
            ") total on total.userId = u.id  " +
            "left join train_user_praise praise   " +
            " on praise.UserId = #{userId}   " +
            " and praise.praiseUserId = u.id  " +
            " and praise.praiseType=#{praiseType}  " +
            " and praise.createDate BETWEEN #{beginDate} and #{endDate}  " +
            "left join (  " +
            "  select praiseUserId,count(1) as num from train_user_praise  " +
            "  where createDate BETWEEN #{beginDate} and #{endDate}  " +
            "    and praise = '0' and praiseType=#{praiseType} and deleted='0'  " +
            "  GROUP BY praiseUserId) praiseNum on u.id = praiseNum.praiseUserId  " +
            " ${ew.customSqlSegment} ")
    List<UserTrainDto> getUserTrainByParam(@Param("userId") String userId,
                                           @Param("beginDate") LocalDateTime beginDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           @Param("paramType") String paramType,
                                           @Param("praiseType") String praiseType,
                                           @Param(Constants.WRAPPER) Wrapper wrapper);
}
