package org.example.atool.service.impl;

import org.example.atool.entity.po.Affiche;
import org.example.atool.mapper.CommonMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonServiceImpl {

    private final CommonMapper commonMapper;

    public CommonServiceImpl(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    public List<Affiche> affiche(Integer size) {
        return commonMapper.affiche(size);
    }
}
