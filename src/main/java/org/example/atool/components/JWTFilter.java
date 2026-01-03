package org.example.atool.components;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.props.JWTProp;
import org.example.atool.security.SecurityDetails;
import org.example.atool.utils.RedisClient;
import org.example.atool.utils.ServletUtil;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final RedisClient redisClient;
    private final JWTProp jWTProp;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (StrUtil.isBlank(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }
        if(!JWTUtil.verify(authorization,jWTProp.getKey())){
            ServletUtil.write(response, Result.err("登录信息校验失败!",null));
            return;
        }

        String key = StrUtil.format("token:{}", authorization);
        if (!redisClient.has(key)) {
            filterChain.doFilter(request, response);
            return;
        }
        String detailJson = redisClient.get(key);
        if (StrUtil.isBlank(detailJson)) {
            filterChain.doFilter(request,response);
            return;
        }

        SecurityDetails details = JSONUtil.toBean(detailJson, SecurityDetails.class);
        if (Objects.nonNull(details)) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
