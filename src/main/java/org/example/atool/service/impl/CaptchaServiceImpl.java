package org.example.atool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.properties.CaptchaProp;
import org.example.atool.service.CaptchaService;
import org.example.atool.utils.RedisClient;
import org.example.atool.utils.Throw;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final RedisClient redisClient;
    private final CaptchaProp prop;

    @Override
    public void send(String type, String target) {
        if (StrUtil.hasBlank(type,target)){
            Throw.RTExp("类型或地址为空");
        }
        if(!CollectionUtil.contains(prop.getTypes(),type)){
            Throw.RTExp("不允许的验证码类型");
        }

        String code = RandomUtil.randomNumbers(prop.getCaptchaCodeLen());

        //todo send api
        redisClient.set(String.format("%s:%s",type,target),code, prop.getTimeout());

    }

    @Override
    public void verify(String type, String target, String code) {

        String key = StrUtil.format("{}:{}",type,target);

        if (redisClient.has(key)) {
            String get = redisClient.get(key);
            if(!StrUtil.contains(get,code)){
                Throw.RTExp("验证码不正确");
            }
        }else {
            Throw.RTExp("验证码无效或已过期");
        }
    }

}
