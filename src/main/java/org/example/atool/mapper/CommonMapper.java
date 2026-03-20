package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.Affiche;

import java.util.List;

@Mapper
public interface CommonMapper {
    List<Affiche> affiche(@Param("size") Integer size);
}
