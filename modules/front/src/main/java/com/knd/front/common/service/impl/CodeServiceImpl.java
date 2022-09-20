package com.knd.front.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.front.entity.Code;
import com.knd.front.common.mapper.CodeMapper;
import com.knd.front.common.service.ICodeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements ICodeService {

    @Override
    public Code insertReturnEntity(Code entity) {
        return null;
    }

    @Override
    public Code updateReturnEntity(Code entity) {
        return null;
    }
}
