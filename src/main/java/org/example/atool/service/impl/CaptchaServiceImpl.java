package org.example.atool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.mapper.UserMapper;
import org.example.atool.props.CaptchaProp;
import org.example.atool.service.CaptchaService;
import org.example.atool.components.sender.captchaSender.AutoCaptchaSender;
import org.example.atool.utils.RedisClient;
import org.example.atool.utils.Throw;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final RedisClient redisClient;
    private final CaptchaProp prop;
    private final UserMapper userMapper;
    private final AutoCaptchaSender captchaSender;

    @Override
    public void send(String type, String target) {

        if (Objects.nonNull(userMapper.getByAccount(target))){
            Throw.BizExp("该帐号已被注册！");
        }
        if (StrUtil.hasBlank(type,target)){
            Throw.BizExp("类型或地址为空");
        }
        if(!CollectionUtil.contains(prop.getTypes(),type)){
            Throw.BizExp("不允许的验证码类型");
        }

        String code = RandomUtil.randomNumbers(prop.getCaptchaLen());
        String key = StrUtil.format("{}:{}",type,target);
        //todo send api
        if(redisClient.has(key)){
            Throw.BizExp("请勿一分钟内重复获取");
        }

        captchaSender.send(type, code, target);

        redisClient.set(key,code, prop.getTimeout(),prop.getUnit());
    }

    @Override
    public void verify(String type, String target, String code) {

        String key = StrUtil.format("{}:{}",type,target);

        if (redisClient.has(key)) {
            String get = redisClient.get(key);
            if(!StrUtil.contains(get,code)){
                Throw.BizExp("验证码不正确");
            }
        }else {
            Throw.BizExp("验证码无效或已过期");
        }
    }

}
