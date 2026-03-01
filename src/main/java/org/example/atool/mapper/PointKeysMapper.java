package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.PointKeys;

import java.util.List;

@Mapper
public interface PointKeysMapper {
    PointKeys get(@Param("key") String key);
    void update(@Param("pointKeys") PointKeys pointKeys);
    void add(@Param("pointKeys") PointKeys pointKeys);

    void addBatch(@Param("list")List<PointKeys> list);

}
