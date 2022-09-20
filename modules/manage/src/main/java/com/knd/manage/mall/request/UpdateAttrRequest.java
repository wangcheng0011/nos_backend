package com.knd.manage.mall.request;

import lombok.Data;

@Data
public class UpdateAttrRequest {
    //userId从token获取
    private String userId ;

    private String id;

    private String categoryId ;

    //List<String> attrNameList;
    private String attrName;

    private String sort;

    private String postType;

}
