package org.example.atool.components;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.utils.RedisClient;
import org.example.atool.utils.ServletUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutHandler implements LogoutSuccessHandler {

    private final RedisClient redisClient;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(token)) {
            if (!redisClient.del(StrUtil.format("token:{}", token))) {
                ServletUtil.write(response, Result.ok("退出登录失败", null));
                return;
            }
            ServletUtil.write(response, Result.ok("退出登录成功", null));
        }
    }
}
