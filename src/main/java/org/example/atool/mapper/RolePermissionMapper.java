package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.securityPOs.RolePermission;

import java.util.List;

@Mapper
public interface RolePermissionMapper {
   List<RolePermission> getByRoles(@Param("roleIds") List<Long> roleIds);
}
