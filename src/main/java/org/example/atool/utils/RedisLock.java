package org.example.atool.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

@RequiredArgsConstructor
public class RedisLock {

    private Duration timeout = Duration.ofSeconds(10);
    private final StringRedisTemplate redis = new StringRedisTemplate();
    private final ValueOperations<String,String> valOps = redis.opsForValue();
    private final String thread_sign = UUID.randomUUID() + "-" + Thread.currentThread().threadId();
    private final String lock;
    public RedisLock(String lock,Long timeout_sec){
        this.lock = lock;
        this.timeout = Duration.ofSeconds(timeout_sec);
    }

    private final RedisScript<Long> unlock_script = new DefaultRedisScript<>(){
        {
            setLocation(new ClassPathResource("luas/unlock.lua"));
            setResultType(Long.class);
        }
    };


    public boolean tryLock(){
        return Boolean.TRUE.equals(valOps.setIfAbsent("lock:" + lock, thread_sign,timeout));
    }

    public boolean unlock(){
        Long executed = redis.execute(unlock_script, Collections.singletonList("lock:" + lock), thread_sign);
        return !executed.equals(0L);
    }
}
