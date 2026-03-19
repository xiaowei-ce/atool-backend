package org.example.atool.utils;

import org.example.atool.entity.po.User;
import org.example.atool.security.SecurityDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class PrincipalUtil {
    public static SecurityDetails principal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityDetails principal = (SecurityDetails) authentication.getPrincipal();
        if(Objects.isNull(principal)){
            Throw.BizExp("获取当前用户信息失败");
        }
        return principal;
    }

    public static User user() {
        User user = principal().getUser();
        if(Objects.isNull(user)){
            Throw.BizExp("获取当前用户信息失败");
        }
        return user;
    }
}