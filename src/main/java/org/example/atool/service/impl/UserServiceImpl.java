package org.example.atool.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.dto.LoginDTO;
import org.example.atool.entity.dto.RegisterDTO;
import org.example.atool.entity.po.PointKeys;
import org.example.atool.entity.po.Record;
import org.example.atool.entity.po.User;
import org.example.atool.entity.po.UserDetail;
import org.example.atool.entity.po.securityPOs.UserRole;
import org.example.atool.entity.vo.RecordVO;
import org.example.atool.entity.vo.UserDetailVO;
import org.example.atool.mapper.*;
import org.example.atool.props.JWTProp;
import org.example.atool.props.RegexProp;
import org.example.atool.service.CaptchaService;
import org.example.atool.service.UserService;
import org.example.atool.utils.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
    private final RecordMapper recordMapper;
    private final PointKeysMapper pointKeysMapper;

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void register(String type, RegisterDTO registerDTO) {

        if (Objects.nonNull(userMapper.getByAccount(registerDTO.getAccount()))) {
            Throw.BizExp("该帐号已被注册！");
        }

        if (!RegexUtil.isMatch(regexProp.get("password"), registerDTO.getPassword())) {
            Throw.BizExp("密码格式错误");
        }
        if (!RegexUtil.isMatch(regexProp.get(type), registerDTO.getAccount())) {
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

        Record record = new Record();
        record.setUserId(user.getId());
        record.setTime(Timestamp.valueOf(LocalDateTime.now()));
        record.setAbstr("新用户注册");
        record.setDetail("新用户注册赠送100积分");
        record.setChange(+100L);
        record.setTypeId(3L);
        recordMapper.add(record);


        UserDetail detail = new UserDetail();
        detail.setUserId(user.getId());
        userDetailMapper.add(detail);
    }

    @Override
    public String login(LoginDTO loginDTO, String authorization) {

        if (StrUtil.isNotBlank(authorization) && JWTUtil.verify(authorization, jWTProp.getKey())) {
            JSONObject object = (JSONObject) JWTUtil.parseToken(authorization).getPayload("loginDTO");

            LoginDTO dto = JSONUtil.toBean(object, LoginDTO.class);

            if (ObjectUtil.equals(loginDTO, dto)) {
                redisClient.del(StrUtil.format("token:{}", authorization));
            }
        }
        String account = loginDTO.getAccount();
        if (!RegexUtil.matchAny(account,regexProp.get())) {
            Throw.BizExp("帐号格式不正确");
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(account, loginDTO.getPassword());
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
        redisClient.set(key, JSONUtil.toJsonStrIncludeNull(authenticate.getPrincipal()), jWTProp.getExpire(), jWTProp.getUnit());
        return token;
    }

    @Override
    public UserDetailVO details() {
        User user = PrincipalUtil.user();
        Long id = user.getId();
        UserDetailVO vo = new UserDetailVO();
        vo.setStatus(user.getEnable() ? "正常" : "禁用");
        vo.setAccount(user.getAccount());
        UserDetail userDetail = userDetailMapper.getByUserId(id);
        BeanUtil.copyProperties(userDetail, vo);
        return vo;
    }

    @Override
    public List<RecordVO> pageGetRecords(Integer page, Integer size) {
        if (page > 150) {
            Throw.BizExp("每页大小不超过150");
        }
        return recordMapper.pageGet(PrincipalUtil.user().getId(), page, size);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void exchange(String key) {

        PointKeys pointKeys = pointKeysMapper.get(key);
        if (Objects.isNull(pointKeys)) {
            Throw.BizExp("未找到卡密，请检查是否输入正确！");
        }
        if (pointKeys.getUsed()) {
            Throw.BizExp("该卡密已经被使用！");
        }
        Long userId = PrincipalUtil.user().getId();
        userDetailMapper.changePoints(userId, pointKeys.getPoints());
        pointKeys.setUsed(true);
        pointKeys.setWho(PrincipalUtil.principal().getUsername());
        pointKeysMapper.update(pointKeys);

        Record record = new Record();
        record.setAbstr("卡密兑换");
        record.setChange(pointKeys.getPoints());
        record.setTime(Timestamp.valueOf(LocalDateTime.now()));
        record.setDetail(StrUtil.format("使用卡密{}兑换了{}积分",key,pointKeys.getPoints()));
        record.setTypeId(2L);
        record.setUserId(userId);
        recordMapper.add(record);
    }

}
