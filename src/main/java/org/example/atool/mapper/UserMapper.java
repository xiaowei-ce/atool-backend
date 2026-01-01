package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.User;

@Mapper
public interface UserMapper {
    void add(@Param("user") User user);
}
