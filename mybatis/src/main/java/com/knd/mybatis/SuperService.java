package com.knd.mybatis;


import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 演示 mapper 父类，注意这个类不要让 mp 扫描到！！
 */
public interface SuperService<T> extends IService<T> {

    // 这里可以放一些公共的方法
    /**
     * 新增并返回entity
     * @param entity
     * @return
     */
    T insertReturnEntity(T entity);

    /**
     * 修改并返回entity
     * @param entity
     * @return
     */
    T updateReturnEntity(T entity);

}
