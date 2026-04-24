package org.example.atool;

import org.example.atool.mapper.OrderMapper;
import org.example.atool.utils.RedisID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class AtoolApplicationTests {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisID redisID;
    @Autowired
    private OrderMapper orderMapper;

    @Test
    void contextLoads() {
//        System.out.println(orderMapper.goodsInfos(63066389241397249L));
        System.out.println(orderMapper.pageOrders(18L, 10));
    }
}
