package org.example.atool.service.impl;

import cn.hutool.core.util.ReUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.dto.RegisterDTO;
import org.example.atool.entity.po.User;
import org.example.atool.entity.po.UserDetail;
import org.example.atool.mapper.UserDetailMapper;
import org.example.atool.mapper.UserMapper;
import org.example.atool.properties.RegexProp;
import org.example.atool.service.CaptchaService;
import org.example.atool.service.UserService;
import org.example.atool.utils.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CaptchaService captchaService;
    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;
    private final RegexProp regexProp;

    @Override
    @Transactional
    public void register(String type, RegisterDTO registerDTO) {
        captchaService.verify(type, registerDTO.getAccount(), registerDTO.getCaptcha());

        if(!ReUtil.isMatch(regexProp.getRegex().get("password"), registerDTO.getPassword())){
            Throw.RTExp("密码格式错误");
        }

        String regex = regexProp.getRegex().get(type);
        if (ReUtil.isMatch(regex,registerDTO.getAccount())) {
            Throw.RTExp("账号格式错误");
        }

        User user = new User();
        switch (type){
            case "email" -> user.setEmail(registerDTO.getAccount());
            case "phone" -> user.setPhone(registerDTO.getAccount());
        }
        user.setPassword(registerDTO.getPassword());
        userMapper.add(user);

        UserDetail detail = new UserDetail();
        detail.setUserId(user.getId());

        userDetailMapper.add(detail);
    }
}
