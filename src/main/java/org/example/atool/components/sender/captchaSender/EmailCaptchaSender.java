package org.example.atool.components.sender.captchaSender;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.props.CaptchaProp;
import org.example.atool.components.sender.EmailSender;
import org.example.atool.utils.StrFmt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class EmailCaptchaSender implements CaptchaSender {
    private final EmailSender emailSender;
    private final CaptchaProp captchaProp;

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void sendCaptcha(String code, String to) {
        String content = StrFmt.fmt(captchaProp.getEmailTemplate(),"#{fmt}",appName,"注册帐号",code,captchaProp.getExpireAs(TimeUnit.MINUTES));
        emailSender.send(StrUtil.format("{}验证码",appName),content,to);
    }
}
