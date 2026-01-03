package org.example.atool.security;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import org.example.atool.entity.po.User;
import org.example.atool.entity.po.securityPOs.Permission;
import org.example.atool.entity.po.securityPOs.RolePermission;
import org.example.atool.entity.po.securityPOs.UserRole;
import org.example.atool.mapper.*;
import org.example.atool.utils.Throw;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class SecurityDetailServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {

        User user = userMapper.getByAccount(account);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("帐号不存在");
        }

        SecurityDetails details = new SecurityDetails();
        details.setUser(user);

        List<UserRole> userRoles = userRoleMapper.getByUser(user.getId());
        if(CollUtil.isEmpty(userRoles)){
            Throw.BizExp("获取用户组信息失败");
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).toList();
        if(CollUtil.isNotEmpty(roleIds)) {
            details.setRoles(roleMapper.getByRoleIds(roleIds));

            List<RolePermission> rolePermissions = rolePermissionMapper.getByRoles(roleIds);
            List<Long> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).toList();
            if(CollUtil.isNotEmpty(permissionIds)) {
                List<Permission> permissions = permissionMapper.getByIds(permissionIds);
                details.setPermissions(permissions);
            }
        }

        return details;
    }
}
