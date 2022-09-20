package com.knd.manage.basedata.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zm
 */
@Data
public class MusicListDto {
    private String id;
    private String type;
    private String name;
    private String remark;
    private ImgDto musicUrl;

}
