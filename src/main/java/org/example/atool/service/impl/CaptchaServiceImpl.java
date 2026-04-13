package org.example.atool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.components.sender.captchaSender.AutoCaptchaSender;
import org.example.atool.mapper.UserMapper;
import org.example.atool.props.CaptchaProp;
import org.example.atool.props.RegexProp;
import org.example.atool.service.CaptchaService;
import org.example.atool.utils.RedisClient;
import org.example.atool.utils.RedisLock;
import org.example.atool.utils.RegexUtil;
import org.example.atool.utils.Throw;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final RedisClient redisClient;
    private final CaptchaProp prop;
    private final AutoCaptchaSender captchaSender;
    private final UserMapper userMapper;
    private final RegexProp regexProp;

    @Override
    public void send(String type, String target) {
        if (StrUtil.hasBlank(type,target)){
            Throw.BizExp("类型或地址为空");
        }
        if(!CollectionUtil.contains(prop.getTypes(),type)){
            Throw.BizExp("不允许的验证码类型");
        }
        if (!RegexUtil.isMatch(regexProp.get(type),target)){
            Throw.BizExp("邮箱或手机号格式错误");
        }

        RedisLock lock = new RedisLock(target,60L);
        if (!lock.tryLock()) {
            Throw.BizExp("重复请求!");
        }
        try {
            String code = RandomUtil.randomNumbers(Math.toIntExact(prop.getCaptchaLen()));
            String checkKey = StrUtil.format("captcha:{}:{}", type, target);
            if (!redisClient.setNX(checkKey, code, prop.getExpire(), TimeUnit.SECONDS)){
                Throw.BizExp(StrUtil.format("请勿{}分钟内重复获取", prop.getResendInAs(TimeUnit.MINUTES)));
            }
            if (Objects.nonNull(userMapper.getByAccount(target))) {
                Throw.BizExp("该帐号已被注册！");
            }
            captchaSender.send(type, code, target);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void verify(String type, String target, String code) {
        String expireCheckKey = StrUtil.format("captcha:{}:{}", type, target);
        if (redisClient.has(expireCheckKey)) {
            String get = redisClient.get(expireCheckKey);
            if(!StrUtil.contains(get,code)){
                Throw.BizExp("验证码不正确");
            }
        }else {
            Throw.BizExp("验证码无效或已过期");
        }
    }

}
