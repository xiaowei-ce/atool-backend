package org.example.atool.security;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.atool.entity.po.User;
import org.example.atool.entity.po.securityPOs.Permission;
import org.example.atool.entity.po.securityPOs.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SecurityDetails implements UserDetails {

    private User user;
    
    private List<Role> roles;
    private List<Permission> permissions;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new LinkedList<>();

        if(CollUtil.isNotEmpty(roles)){
            roles.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(StrUtil.format("ROLE_{}",role.getRoleName())));
            });
        }

        if(CollUtil.isNotEmpty(permissions)){
            permissions.forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getPermissionName()));
            });
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAccount();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnable();
    }
}
