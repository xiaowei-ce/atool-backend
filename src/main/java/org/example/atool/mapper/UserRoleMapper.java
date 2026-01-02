package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.securityPOs.UserRole;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    List<UserRole> getByUser(@Param("userId") Long userId);

    void add(@Param("userRole") UserRole userRole);
}
