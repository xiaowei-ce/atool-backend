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

    public String get(String key){
        return template.opsForValue().get(key);
    }

    public Boolean has(String key){
        return template.hasKey(key);
    }

    public Boolean del(String key){
        return template.delete(key);
    }
}
