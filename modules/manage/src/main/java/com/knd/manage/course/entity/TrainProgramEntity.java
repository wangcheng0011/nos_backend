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
@TableName("train_program")
public class TrainProgramEntity extends BaseEntity {

    private static final long serialVersionUID=1L;
    private String id;
    private String programName;
    private String userId;
    private String beginTime;
    private String trainWeekNum;
    private String trainWeekDayNum;
    private String picAttachId;
    private String source;
    private String type;
    private String deleted;
}
