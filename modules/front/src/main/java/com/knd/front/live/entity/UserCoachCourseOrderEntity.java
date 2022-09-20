package com.knd.front.live.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教练课程预约表
 * @author zm
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lb_user_coach_course_order")
@ApiModel(value="UserCoachCourseOrderEntity对象", description="")
public class UserCoachCourseOrderEntity extends BaseEntity {

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "教练用户id")
    private String coachUserId;

    @ApiModelProperty(value = "教练时间id")
    private String coachTimeId;

    @ApiModelProperty(value = "预约标识0预约中 1已预约")
    private String isOrder;

    @ApiModelProperty(value = "预约用户id")
    private String orderUserId;

}
