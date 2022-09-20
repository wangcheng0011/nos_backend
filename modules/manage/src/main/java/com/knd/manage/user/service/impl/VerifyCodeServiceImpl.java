package com.knd.manage.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.config.SmsConstants;
import com.knd.manage.mall.dto.CodeDto;
import com.knd.manage.mall.mapper.VerifyCodeMapper;
import com.knd.manage.user.dto.MessageIdDto;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.entity.VerifyCode;
import com.knd.manage.user.mapper.UserMapper;
import com.knd.manage.user.service.IVerifyCodeService;
import com.knd.manage.user.util.SendSms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Service
public class VerifyCodeServiceImpl extends ServiceImpl<VerifyCodeMapper, VerifyCode> implements IVerifyCodeService {
     @Resource
    private VerifyCodeMapper verifyCodeMapper;
     @Resource
    private UserMapper userMapper;
    @Value("${time}")
    private String time;

    /*
    app默认token为 eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJzRUo0dkU0UyIsInBsYXRmb3JtIjoiYXBwIiwiaWF0IjoxNTk2MDk5MjQwfQ._LYGVTmcA1izjPUBP3Xqfx3GqhfljZkzBFLN424TVrM
     */


    @Override
    public VerifyCode insertReturnEntity(VerifyCode entity) {
        return null;
    }

    @Override
    public VerifyCode updateReturnEntity(VerifyCode entity) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result getVerificationCode(CodeDto codeDto) {
        if (!codeType.Reset.value.equals(codeDto.getCodeType())
                && !codeType.Register.value.equals(codeDto.getCodeType())
                && !codeType.RecevicingCode.value.equals(codeDto.getCodeType())
                && !codeType.LoginVerifyCode.value.equals(codeDto.getCodeType())) {
            return ResultUtil.error("U1998", "无该验证码类型");
        }
        //检查改手机号是否在1分钟内发送过验证码
        //根据手机号，获取最新的验证发送时间
        QueryWrapper<VerifyCode> qw = new QueryWrapper<>();
        qw.select("createDate");
        qw.eq("mobile", codeDto.getMobile());
        qw.eq("deleted", 0);
        qw.eq("codeType", codeDto.getCodeType());
        qw.orderByDesc("createDate");
        qw.last(" limit 0,1");
        VerifyCode v = verifyCodeMapper.selectOne(qw);
        if (v != null) {
            //有数据
            //比较时间
            if ((new Date()).getTime() - v.getCreateDate().toInstant(ZoneOffset.of("+8")).toEpochMilli() <= 60000) {
                return ResultUtil.error("U1998", "一分钟内只能发送一次短信");
            }
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", codeDto.getMobile());
        wrapper.eq("deleted", 0);
        User user = userMapper.selectOne(wrapper);
        //注册过的不允许再发注册验证码
        if(StringUtils.isEmpty(codeDto.getCode())&&!codeType.LoginVerifyCode.value.equals(codeDto.getCodeType())) {
            if (codeType.Register.value.equals(codeDto.getCodeType())) {
                //注册短信
                if (StringUtils.isNotEmpty(user)) {
                    return ResultUtil.error("U1998", "该手机号已注册,请勿重复注册");
                }
                //未注册的不允许发重置验证码
            } else {
                //忘记密码短信
                if (StringUtils.isEmpty(user)) {
                    //空的
                    return ResultUtil.error(ResultEnum.DELETE_PHONE_ERROR);
                } else {
                    //不空
                    if (user.getFrozenFlag().equals("1")) {
                        return ResultUtil.error("U1998", "该手机号已被冻结");
                    }
                }
            }
        }

        QueryWrapper<VerifyCode> queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", codeDto.getMobile());
        queryWrapper.eq("codeType", codeDto.getCodeType());
        queryWrapper.ge("expireTime", DateUtils.getCurrentLocalDateTime());
        queryWrapper.eq("deleted", 0);
        List<VerifyCode> getByMobile = verifyCodeMapper.selectList(queryWrapper);
        if (StringUtils.isNotEmpty(getByMobile)) {
            verifyCodeMapper.updateByPrimaryKeyList(getByMobile);
        }
        String s = null;
        try {

            if (codeType.Register.value.equals(codeDto.getCodeType())) {
                s = SendSms.SendSmsCode(SmsConstants.getRegisterTemplateId(), codeDto.getMobile());
            }
            if (codeType.Reset.value.equals(codeDto.getCodeType())) {
                s = SendSms.SendSmsCode(SmsConstants.getResetTemplateId(), codeDto.getMobile());
            }
            if (codeType.RecevicingCode.value.equals(codeDto.getCodeType())) {
                s = SendSms.SendRecevicingSmsCode(SmsConstants.getReceivingCodeTemplateId(), codeDto.getMobile(),codeDto.getCode());
            }
            if (codeType.LoginVerifyCode.value.equals(codeDto.getCodeType())) {
                s = SendSms.SendSmsCode(SmsConstants.getLoginCodeTemplateId(), codeDto.getMobile());
            }
        } catch (Exception e) {
            throw new CustomResultException("验证码发送失败");
        }
        VerifyCode record = new VerifyCode();
        record.setCode(s);
        record.setMobile(codeDto.getMobile());
        record.setCodeType(codeDto.getCodeType());
        record.setExpireTime(DateUtils.date2LocalDateTime(new Date(System.currentTimeMillis() + Integer.parseInt(time) * 60 * 1000)));
        record.setCreateDate(LocalDateTime.now());
        record.setDeleted("0");
        verifyCodeMapper.insert(record);
        MessageIdDto messageIdDto = new MessageIdDto();
        messageIdDto.setVerifyCodeId(record.getId());
        return ResultUtil.success(messageIdDto);


    }

    public enum codeType {
        /**
         *手机登录验证码
         */
        LoginVerifyCode("40"),
        /**
         *收货验证码
         */
        RecevicingCode("30"),
        /**
         * 注册
         */
        Register("10"),
        /**
         * 重置
         */
        Reset("20");
        public String value;

        codeType(String value) {
            this.value = value;
        }
    }
}
