package org.example.atool.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.po.Affiche;
import org.example.atool.mapper.CommonMapper;
import org.example.atool.utils.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl {
    private final CommonMapper commonMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public List<Affiche> affiche(Integer size) {
        String afficheCache = (String) stringRedisTemplate.opsForValue().get("cache:affiche");
        List<Affiche> affiches;
        if (StrUtil.isNotBlank(afficheCache)) {
            affiches = JSONUtil.toList(afficheCache, Affiche.class);
        } else {
            affiches = commonMapper.affiche(size);
            stringRedisTemplate.opsForValue().set("cache:affiche",JSONUtil.toJsonStrIncludeNull(affiches));
        }
        return affiches;
    }
}
