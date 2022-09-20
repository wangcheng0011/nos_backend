package com.knd.manage.mall.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author will
 */
@Data
public class CreateAttrRequest {
    //userId从token获取
    private String userId;

    @NotBlank(message = "操作类型不可空")
    @Pattern(regexp = "^(1|2)$", message = "操作类型错误")
    private String postType;
    @Size(max = 64, message = "id最大长度为64")
    private String id;
    @NotBlank(message = "属性名称不可空")
    private List<String> attrNameList;

    @NotBlank(message = "分类id不可空")
    @Size(max = 64, message = "分类id最大长度为64")
    private String categoryId;



}
