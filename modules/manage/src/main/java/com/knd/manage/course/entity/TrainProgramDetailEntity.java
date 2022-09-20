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
public class TrainProgramDetailEntity{

    private String weekdayName;
    private String itemType;
    private String itemName;
}
