package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fault_template")
@ApiModel(value="FaultTemplate对象", description="")
public class FaultTemplate extends BaseEntity {

    private static final long serialVersionUID=1L;

    private String faultCode;
    private String faultDesc;
    private String causeAnalysis;
    private String solution;


}
