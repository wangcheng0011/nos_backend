package com.knd.manage.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zm
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lb_user_coach_attach")
@ApiModel(value="UserCoachAttach对象", description="")
public class UserCoachAttach extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "教练id")
    private String coachUserId;

    @ApiModelProperty(value = "教练图片Id")
    private String attachUrlId;

}
