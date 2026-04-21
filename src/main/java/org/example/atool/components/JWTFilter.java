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
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private final StringRedisTemplate stringRedisTemplate;
    private final JWTProp jWTProp;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (StrUtil.isNotBlank(authorization)) {
            try {
                if (JWTUtil.verify(authorization, jWTProp.getKey())) {
                    String key = StrUtil.format("token:{}", authorization);
                    if (stringRedisTemplate.hasKey(key)) {
                        String detailJson = (String) stringRedisTemplate.opsForValue().get(key);
                        if (StrUtil.isNotBlank(detailJson)) {
                            SecurityDetails details = JSONUtil.toBean(detailJson, SecurityDetails.class);
                            if (Objects.nonNull(details)) {
                                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            }
                        }
                    }
                } else {
                    ServletUtil.write(response, Result.err("登录信息校验失败!", null));
                }
            } catch (Exception e) {
                ServletUtil.write(response, Result.err("登录信息校验失败!", null));
                return;
            }
        }
        filterChain.doFilter(request,response);
    }
}
