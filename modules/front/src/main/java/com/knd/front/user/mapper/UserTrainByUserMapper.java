package com.knd.front.user.mapper;

import com.knd.front.user.dto.UserTrainDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

/**
 * @author Lenovo
 */
public interface UserTrainByUserMapper {


    /**
     * 总力量
     * @param userId
     * @param date
     * @return
     */
    @Select("select u.id as userId,u.nickName,pic.filePath as headPicUrl," +
            " IFNULL(SUM(total.finishTotalPower),'0') as trainNum,   " +
            " IFNULL(praise.praise,'1') as praise," +
            " IFNULL(praiseNum.num,'0') as praiseNum  " +
            "from user u " +
            "LEFT JOIN user_detail ud on u.id = ud.userId and ud.deleted=0 " +
            "LEFT JOIN attach pic on ud.headPicUrlId = pic.id and pic.deleted=0 " +
            "LEFT JOIN(   " +
            " select userId,finishTotalPower from train_action_array_head    " +
            " where deleted = 0    " +
            " and date_format(trainEndTime, '%Y-%m') = date_format(#{date}, '%Y-%m')  " +
            " union all   " +
            " select tchi.userId,tchi.finishTotalPower finishTotalPower from train_course_head_info tchi " +
            " LEFT JOIN train_course_block_info tcbi ON tcbi.trainCourseHeadInfoId = tchi.id AND tcbi.deleted = 0 "+
            " LEFT JOIN train_course_act_info tcai on tcai.trainCourseBlockInfoId = tcbi.id AND tcai.deleted = 0 "+
            " where tchi.deleted = 0   " +
            " and date_format(tchi.vedioEndTime, '%Y-%m') = date_format(#{date}, '%Y-%m') GROUP BY tchi.id " +
            " union all   " +
            " select userId,finishTotalPower from train_free_head    " +
            " where deleted = 0    " +
            " and date_format(vedioEndTime, '%Y-%m') = date_format(#{date}, '%Y-%m')  " +
            " union all   " +
            " select userId,finishTotalPower from train_free_train_head    " +
            " where deleted = 0    " +
            " and date_format(createDate, '%Y-%m') = date_format(#{date}, '%Y-%m')   " +
            " ) total on total.userId = u.id   " +
            " left join train_user_praise praise    " +
            " on praise.UserId = #{userId}  " +
            " and praise.praiseUserId = u.id   " +
            " and praise.praiseType='0'  " +
            " and date_format(praise.createDate, '%Y-%m') = date_format(#{date}, '%Y-%m')   " +
            " left join (   " +
            "  select praiseUserId,count(1) as num from train_user_praise   " +
            "  where date_format(createDate, '%Y-%m') = date_format(#{date}, '%Y-%m')   " +
            "   and praise = '0' and praiseType='0' and deleted='0'   " +
            "  GROUP BY praiseUserId) praiseNum on u.id = praiseNum.praiseUserId   " +
            " where u.id = #{userId} ")
    UserTrainDto getUserPower(@Param("userId") String userId,
                                   @Param("date") LocalDate date);

    /**
     * 毅力
     * @param userId
     * @param date
     * @return
     */
    @Select("select u.id as userId,u.nickName,pic.filePath as headPicUrl," +
            " IFNULL(SUM(total.actTrainSeconds),'0') as trainNum,   " +
            " IFNULL(praise.praise,'1') as praise," +
            " IFNULL(praiseNum.num,'0') as praiseNum  " +
            "from user u " +
            "LEFT JOIN user_detail ud on u.id = ud.userId and ud.deleted=0 " +
            "LEFT JOIN attach pic on ud.headPicUrlId = pic.id and pic.deleted=0 " +
            "LEFT JOIN(   " +
            " select userId,actTrainSeconds from train_action_array_head    " +
            " where deleted = 0    " +
            " and date_format(trainEndTime, '%Y-%m') = date_format(#{date}, '%Y-%m')   " +
            " union all   " +
            " select tchi.userId,tchi.actualTrainSeconds from train_course_head_info tchi " +
            " LEFT JOIN train_course_block_info tcbi ON tcbi.trainCourseHeadInfoId = tchi.id AND tcbi.deleted = 0 "+
            " LEFT JOIN train_course_act_info tcai on tcai.trainCourseBlockInfoId = tcbi.id AND tcai.deleted = 0 "+
            " where tchi.deleted = 0   " +
            " and date_format(tchi.vedioEndTime, '%Y-%m') = date_format(#{date}, '%Y-%m') GROUP BY tchi.id " +
            " union all   " +
            " select userId,actTrainSeconds from train_free_head    " +
            " where deleted = 0    " +
            " and date_format(vedioEndTime, '%Y-%m') = date_format(#{date}, '%Y-%m')   " +
            " union all   " +
            " select userId,actTrainSeconds from train_free_train_head    " +
            " where deleted = 0    " +
            " and date_format(createDate, '%Y-%m') = date_format(#{date}, '%Y-%m')   " +
            " ) total on total.userId = u.id   " +
            " left join train_user_praise praise    " +
            " on praise.UserId = #{userId}   " +
            " and praise.praiseUserId = u.id   " +
            " and praise.praiseType='1'  " +
            " and date_format(praise.createDate, '%Y-%m') = date_format(#{date}, '%Y-%m')   " +
            " left join (   " +
            "  select praiseUserId,count(1) as num from train_user_praise   " +
            "  where date_format(createDate, '%Y-%m') = date_format(#{date}, '%Y-%m')   " +
            "   and praise = '0' and praiseType='1' and deleted='0'   " +
            "  GROUP BY praiseUserId) praiseNum on u.id = praiseNum.praiseUserId   " +
            " where u.id = #{userId} ")
    UserTrainDto getUserWill(@Param("userId") String userId,
                                       @Param("date") LocalDate date);

    /**
     * 爆发力
     * @param userId
     * @param date
     * @return
     */
    @Select("select u.id as userId,u.nickName,pic.filePath as headPicUrl," +
            " IFNULL(SUM(total.maxExplosiveness),'0') as trainNum,   " +
            " IFNULL(praise.praise,'1') as praise," +
            " IFNULL(praiseNum.num,'0') as praiseNum   " +
            "from user u  " +
            "LEFT JOIN user_detail ud on u.id = ud.userId and ud.deleted=0 " +
            "LEFT JOIN attach pic on ud.headPicUrlId = pic.id and pic.deleted=0 " +
            "LEFT JOIN(   " +
            " select userId,maxExplosiveness from train_action_array_head    " +
            " where deleted = 0    " +
            " and date_format(trainEndTime, '%Y-%m') = date_format(#{date}, '%Y-%m')  " +
            "union all   " +
            " select tchi.userId,tchi.maxExplosiveness from train_course_head_info tchi " +
            " LEFT JOIN train_course_block_info tcbi ON tcbi.trainCourseHeadInfoId = tchi.id AND tcbi.deleted = 0 "+
            " LEFT JOIN train_course_act_info tcai on tcai.trainCourseBlockInfoId = tcbi.id AND tcai.deleted = 0 "+
            " where tchi.deleted = 0   " +
            " and date_format(tchi.vedioEndTime, '%Y-%m') = date_format(#{date}, '%Y-%m') GROUP BY tchi.id " +
            " union all   " +
            " select userId,maxExplosiveness from train_free_head    " +
            " where deleted = 0    " +
            " and date_format(vedioEndTime, '%Y-%m') = date_format(#{date}, '%Y-%m')  " +
            "union all   " +
            " select userId,maxExplosiveness from train_free_train_head    " +
            " where deleted = 0    " +
            " and date_format(createDate, '%Y-%m') = date_format(#{date}, '%Y-%m')  " +
            ") total  on total.userId = u.id   " +
            "left join train_user_praise praise    " +
            " on praise.UserId = #{userId}    " +
            " and praise.praiseUserId = u.id   " +
            " and praise.praiseType='2'   " +
            " and date_format(praise.createDate, '%Y-%m') = date_format(#{date}, '%Y-%m')   " +
            "left join (   " +
            "  select praiseUserId,count(1) as num from train_user_praise   " +
            "  where date_format(createDate, '%Y-%m') = date_format(#{date}, '%Y-%m')  " +
            "    and praise = '0' and praiseType='2' and deleted='0'   " +
            "  GROUP BY praiseUserId) praiseNum on u.id = praiseNum.praiseUserId   " +
            " where u.id = #{userId}  ")
    UserTrainDto getUserMaxPower(@Param("userId") String userId,
                                       @Param("date") LocalDate date);

}
