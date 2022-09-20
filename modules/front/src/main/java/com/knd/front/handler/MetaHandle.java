package com.knd.front.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.mybatis.BaseEntity;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/8/2
 * @Version 1.0
 */
@Component
public class MetaHandle implements MetaObjectHandler {

    /**
     * 插入数据
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof BaseEntity) {
            if(UserUtils.getToken()!=null){
                this.setFieldValByName("createBy", UserUtils.getUserId(), metaObject);
                this.setFieldValByName("lastModifiedBy", UserUtils.getUserId(), metaObject);
            }

            this.setFieldValByName("id", UUIDUtil.getShortUUID(), metaObject);
            if(StringUtils.isEmpty(((BaseEntity) metaObject.getOriginalObject()).getCreateDate())){
                this.setFieldValByName("createDate", DateUtils.getCurrentLocalDateTime(), metaObject);
            }
            this.setFieldValByName("lastModifiedDate", DateUtils.getCurrentLocalDateTime(), metaObject);
            this.setFieldValByName("deleted", "0", metaObject);
        }
    }

    /**
     * 更新数据
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof BaseEntity) {
            if(UserUtils.getToken()!=null){
                this.setFieldValByName("lastModifiedBy", UserUtils.getUserId(), metaObject);
            }
            this.setFieldValByName("lastModifiedDate", DateUtils.getCurrentLocalDateTime(), metaObject);
        }
    }
}