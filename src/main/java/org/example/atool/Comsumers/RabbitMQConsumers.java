package org.example.atool.Comsumers;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.atool.components.sender.captchaSender.AutoCaptchaSender;
import org.example.atool.entity.Msgs;
import org.example.atool.props.CaptchaProp;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMQConsumers {

    private final AutoCaptchaSender captchaSender;
    private final CaptchaProp captchaProp;
    private final StringRedisTemplate stringRedisTemplate;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = Msgs.CAPTCHA_WILL_SEND_QUEUE, durable = "true" , arguments = {@Argument(name = "x-message-ttl", value = "60000", type = "java.lang.Integer")}),
                    exchange = @Exchange(name = Msgs.EXCHANGE, durable = "true", type = ExchangeTypes.TOPIC),
                    key =  Msgs.CAPTCHA_WILL_SEND_ROUTING_KEY
            )
    })
    public void sendConsumer(Msgs.CaptchaSendMsg msg){
        String code = RandomUtil.randomNumbers(Math.toIntExact(captchaProp.getCaptchaLen()));
        captchaSender.send(msg.getType(),code ,msg.getTarget());
        log.info("send:{}",msg);
        String checkKey = StrUtil.format("captcha:{}:{}", msg.getType(), msg.getTarget());
        stringRedisTemplate.opsForValue().set(checkKey,code,captchaProp.getExpire(),captchaProp.getExpireUnit());
    }
}
