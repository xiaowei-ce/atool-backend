package org.example.atool.utils;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RedisID {

    private final StringRedisTemplate stringRedisTemplate;

    private final long START_TIMESTAMP = 1776767967549L;
    private final String REDIS_KEY = "counter";


    public long withPrefix(String prefix) {
        LocalDate now = LocalDate.now();
        String year = now.format(DateTimeFormatter.ofPattern("yyyy"));
        String month = now.format(DateTimeFormatter.ofPattern("MM"));
        String day = now.format(DateTimeFormatter.ofPattern("dd"));

        String key = StrUtil.isBlank(prefix) ? String.format("%s:%s:%s:%s",REDIS_KEY,year,month,day)
                : String.format("%s:%s:%s:%s:%s",REDIS_KEY,prefix,year,month,day);

        long timestamp = System.currentTimeMillis() - START_TIMESTAMP;
        long increment = Objects.requireNonNull(stringRedisTemplate.opsForValue().increment(key),"ID生成出错!");
        return (timestamp << 32) | increment;
    }

    public long id() {
        return withPrefix(null);
    }

}
