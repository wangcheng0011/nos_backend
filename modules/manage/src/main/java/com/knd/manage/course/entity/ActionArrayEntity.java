package com.knd.manage.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Lenovo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("action_array")
public class ActionArrayEntity extends BaseEntity {

    private static final long serialVersionUID=1L;
    private String id;
    private String actionArrayName;
    private String actionQuantity;
    private String totalDuration;
}
