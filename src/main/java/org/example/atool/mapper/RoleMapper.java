package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.securityPOs.Role;

import java.util.List;

@Mapper
public interface RoleMapper {
    List<Role> getByRoleIds(@Param("roleIds") List<Long> roleIds);
}
