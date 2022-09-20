package com.knd.batch.service;

import com.knd.batch.entity.Attach;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-10-09
 */
public interface IAttachService extends SuperService<Attach> {
    //批量删除obs的文件
    void deleteObsFile();

    //批量刷新访问路径
    void refreshUrl();

}
