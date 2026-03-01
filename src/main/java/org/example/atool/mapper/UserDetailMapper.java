package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.UserDetail;

@Mapper
public interface UserDetailMapper {
    void add(@Param("detail") UserDetail detail);

    UserDetail getByUserId(@Param("userId") Long userId);

    void update(@Param("detail") UserDetail detail);

    void changePoints(@Param("userId") Long userId,@Param("change") Long change);
}
