package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.securityPOs.Permission;

import java.util.List;

@Mapper
public interface PermissionMapper {
    List<Permission> getByIds(@Param("ids") List<Long> ids);
}
