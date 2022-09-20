package com.knd.mybatis;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;

/**
 * 演示 mapper 父类，注意这个类不要让 mp 扫描到！！
 */
public class SuperServiceImpl<M extends BaseMapper<T>, T extends Model> extends ServiceImpl<M, T> implements SuperService<T> {


    // 这里可以放一些公共的方法

    /**
     * 新增并返回entity
     *
     * @param entity
     * @return
     */
    public T insertReturnEntity(T entity) {
        boolean save = this.save(entity);
        Serializable pkVal = (Serializable) ReflectionKit.getMethodValue(entity, TableInfoHelper.getTableInfo(entity.getClass()).getKeyProperty());
        if (save) entity = baseMapper.selectById(pkVal);
        return entity;
    }

    /**
     * 新增并返回entity
     *
     * @param entity
     * @return
     */
    public T updateReturnEntity(T entity) {
        boolean save = this.updateById(entity);
        Serializable pkVal = (Serializable) ReflectionKit.getMethodValue(entity, TableInfoHelper.getTableInfo(entity.getClass()).getKeyProperty());
        if (save) entity = baseMapper.selectById(pkVal);
        return entity;
    }

}
