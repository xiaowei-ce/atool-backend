package org.example.atool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.Msgs;
import org.example.atool.props.CaptchaProp;
import org.example.atool.props.RegexProp;
import org.example.atool.service.CaptchaService;
import org.example.atool.utils.RedisLock;
import org.example.atool.utils.RegexUtil;
import org.example.atool.utils.Throw;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {


    private final CaptchaProp prop;
    private final RegexProp regexProp;
    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void send(String type, String target) {
        if (StrUtil.hasBlank(type, target)) {
            Throw.BizExp("类型或地址为空");
        }
        if (!CollectionUtil.contains(prop.getTypes(), type)) {
            Throw.BizExp("不允许的验证码类型");
        }
        if (!RegexUtil.isMatch(regexProp.get(type), target)) {
            Throw.BizExp("邮箱或手机号格式错误");
        }

        RedisLock lock = new RedisLock(target, 10L, stringRedisTemplate);
        try {
            if (!lock.tryLock()) {
                Throw.BizExp("重复请求!");
            }
            String checkKey = StrUtil.format("captcha:{}:{}", type, target);
            if (stringRedisTemplate.hasKey(checkKey)) {
                Throw.BizExp(StrUtil.format("请勿{}分钟内重复获取", prop.getExpireAs(TimeUnit.MINUTES)));
            }
            Msgs.CaptchaSendMsg msg = new Msgs.CaptchaSendMsg(type, target);
            rabbitTemplate.convertAndSend(Msgs.EXCHANGE, Msgs.CAPTCHA_WILL_SEND_ROUTING_KEY, msg);

        } finally {
            lock.unlock();
        }
    }

    @Override
    public void verify(String type, String target, String code) {
        String expireCheckKey = StrUtil.format("captcha:{}:{}", type, target);
        if (stringRedisTemplate.hasKey(expireCheckKey)) {
            String get = stringRedisTemplate.opsForValue().get(expireCheckKey);
            if (!StrUtil.contains(get, code)) {
                Throw.BizExp("验证码不正确");
            }
        } else {
            Throw.BizExp("验证码无效或已过期");
        }
    }

}
