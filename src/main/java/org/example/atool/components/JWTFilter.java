package org.example.atool.components;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.atool.security.SecurityUserDetails;
import org.example.atool.utils.RedisClient;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final RedisClient redisClient;

    public JWTFilter(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (StrUtil.isBlank(authorization)) {
            filterChain.doFilter(request, response);
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

        SecurityUserDetails details = JSONUtil.toBean(detailJson, SecurityUserDetails.class);

        if (Objects.nonNull(details)) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
