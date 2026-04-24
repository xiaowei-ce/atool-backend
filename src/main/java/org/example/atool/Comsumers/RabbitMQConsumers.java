package org.example.atool.Comsumers;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.atool.components.sender.captchaSender.AutoCaptchaSender;
import org.example.atool.entity.Msgs;
import org.example.atool.entity.po.Order;
import org.example.atool.mapper.OrderMapper;
import org.example.atool.props.CaptchaProp;
import org.example.atool.service.impl.ShopServiceImpl;
import org.example.atool.utils.RedisLock;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMQConsumers {

    private final AutoCaptchaSender captchaSender;
    private final CaptchaProp captchaProp;
    private final StringRedisTemplate stringRedisTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final ShopServiceImpl shopServiceImpl;
    private final OrderMapper orderMapper;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = Msgs.CAPTCHA_WILL_SEND_QUEUE, durable = "true", arguments = {@Argument(name = "x-message-ttl", value = "60000", type = "java.lang.Integer")}),
                    exchange = @Exchange(name = Msgs.EXCHANGE, durable = "true", type = ExchangeTypes.TOPIC),
                    key = Msgs.CAPTCHA_WILL_SEND_ROUTING_KEY
            )
    })
    public void sendConsumer(Msgs.CaptchaSendMsg msg) {
        String code = RandomUtil.randomNumbers(Math.toIntExact(captchaProp.getCaptchaLen()));
        captchaSender.send(msg.getType(), code, msg.getTarget());
        log.info("send:{}", msg);
        String checkKey = StrUtil.format("captcha:{}:{}", msg.getType(), msg.getTarget());
        stringRedisTemplate.opsForValue().set(checkKey, code, captchaProp.getExpire(), captchaProp.getExpireUnit());
    }


    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = Msgs.ORDER_CANCEL_TTL_QUEUE, durable = "true"),
                    exchange = @Exchange(name = Msgs.DLX_EXCHANGE, durable = "true", type = ExchangeTypes.TOPIC),
                    key = Msgs.ORDER_CANCEL_TTL_ROUTING_KEY
            )
    })
    public void orderCancel(Msgs.CancelOrderMsg msg) {
        Order order = orderMapper.orderById(msg.getOrderId());
        if (order == null || !order.getStatus().equals(Order.PENDING)) {
            return;
        }

        RedisLock lock = new RedisLock(stringRedisTemplate,"orderCancel:"+msg.getOrderId());
        if (!lock.tryLock()) {
            return;
        }
        try {
            if (msg.offest2Next()) {
                rabbitTemplate.convertAndSend(Msgs.EXCHANGE, Msgs.ORDER_CANCEL_ROUTING_KEY, msg, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setExpiration(msg.nowExp());
                        return message;
                    }
                });
                return;
            }
            orderMapper.markCanceled(msg.getOrderId());
        }finally {
            lock.unlock();
        }
    }


    @Bean
    public org.springframework.amqp.core.Queue orderCancelQueue() {
        return QueueBuilder.durable(Msgs.ORDER_CANCEL_QUEUE)
                .deadLetterExchange(Msgs.DLX_EXCHANGE)
                .deadLetterRoutingKey(Msgs.ORDER_CANCEL_TTL_ROUTING_KEY)
                .build();
    }

    @Bean
    public org.springframework.amqp.core.Exchange exchange() {
        return ExchangeBuilder
                .topicExchange(Msgs.EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Binding orderCancelQueueBinding() {
        return BindingBuilder.bind(orderCancelQueue())
                .to(exchange())
                .with(Msgs.ORDER_CANCEL_ROUTING_KEY)
                .noargs();
    }


}
