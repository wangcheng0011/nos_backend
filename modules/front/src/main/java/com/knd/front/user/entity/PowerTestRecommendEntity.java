package com.knd.front.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.front.entity.User;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("power_test_recommend")
public class PowerTestRecommendEntity {

    private static final Long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    protected String id;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "推荐难度")
    private String recommendDifficulty;
    @ApiModelProperty(value = "推荐部位")
    private String recommendBodyPart;



}
