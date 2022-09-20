package com.knd.auth.service.impl;

import com.knd.auth.entity.PassUri;
import com.knd.auth.mapper.PassUriMapper;
import com.knd.auth.service.IPassUriService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.mybatis.SuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-10
 */
@Service
public class PassUriServiceImpl extends SuperServiceImpl<PassUriMapper, PassUri> implements IPassUriService {

}
