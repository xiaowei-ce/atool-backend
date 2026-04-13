package org.example.atool.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisClient {
    private final RedisTemplate<String,String> template;

    public void set(String key,String val){
        template.opsForValue().set(key, val);
    }

    public void set(String key, String val, Long time, TimeUnit unit){
        template.opsForValue().set(key,val,time, unit);
    }

    public boolean setNX(String key, String val){
        return Boolean.TRUE.equals(template.opsForValue().setIfAbsent(key, val));
    }
    public boolean setNX(String key, String val,Long timeout, TimeUnit unit){
        return Boolean.TRUE.equals(template.opsForValue().setIfAbsent(key, val,timeout,unit));
    }

    public String get(String key){
        return template.opsForValue().get(key);
    }

    public boolean has(String key){
        return template.hasKey(key);
    }

    public boolean del(String key){
        return template.delete(key);
    }
}
