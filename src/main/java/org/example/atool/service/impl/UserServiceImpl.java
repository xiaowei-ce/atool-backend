package org.example.atool.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.dto.LoginDTO;
import org.example.atool.entity.dto.RegisterDTO;
import org.example.atool.entity.po.User;
import org.example.atool.entity.po.UserDetail;
import org.example.atool.entity.po.securityPOs.UserRole;
import org.example.atool.mapper.UserDetailMapper;
import org.example.atool.mapper.UserMapper;
import org.example.atool.mapper.UserRoleMapper;
import org.example.atool.props.JWTProp;
import org.example.atool.props.RegexProp;
import org.example.atool.service.CaptchaService;
import org.example.atool.service.UserService;
import org.example.atool.utils.JSONUtil;
import org.example.atool.utils.RedisClient;
import org.example.atool.utils.Throw;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.util.HashMap;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CaptchaService captchaService;
    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;
    private final RegexProp regexProp;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleMapper userRoleMapper;
    private final AuthenticationManager authenticationManager;
    private final RedisClient redisClient;
    private final JWTProp jWTProp;

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void register(String type, RegisterDTO registerDTO) {

        if (Objects.nonNull(userMapper.getByAccount(registerDTO.getAccount()))) {
            Throw.BizExp("该帐号已被注册！");
        }

        if (!ReUtil.isMatch(regexProp.get("password"), registerDTO.getPassword())) {
            Throw.BizExp("密码格式错误");
        }
        if (!ReUtil.isMatch(regexProp.get(type), registerDTO.getAccount())) {
            Throw.BizExp("账号格式错误");
        }
        captchaService.verify(type, registerDTO.getAccount(), registerDTO.getCaptcha());

        User user = new User();
        user.setAccount(registerDTO.getAccount());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userMapper.add(user);

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRoleMapper.add(userRole);

        UserDetail detail = new UserDetail();
        detail.setUserId(user.getId());
        userDetailMapper.add(detail);
    }

    @Override
    public String login(LoginDTO loginDTO, String authorization) {

        if (StrUtil.isNotBlank(authorization) && JWTUtil.verify(authorization, jWTProp.getKey())) {

            LoginDTO dto = (LoginDTO) JWTUtil.parseToken(authorization).getPayload("loginDTO");
            if(ObjectUtil.equals(loginDTO,dto)){
                redisClient.del(StrUtil.format("token:{}", authorization));
            }
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginDTO.getAccount(), loginDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (Objects.isNull(authenticate)) {
            Throw.BizExp("登录验证失败");
        }

        HashMap<String, Object> payload = new HashMap<>() {
            @Serial
            private static final long serialVersionUID = 1L;

            {
                put("loginDTO", loginDTO);
            }
        };
        String token = JWTUtil.createToken(payload, jWTProp.getKey());
        String key = StrUtil.format("token:{}", token);
        redisClient.set(key, JSONUtil.toJsonStrNoIgnoreNull(authenticate.getPrincipal()), jWTProp.getExpire(), jWTProp.getUnit());
        return token;
    }

}
