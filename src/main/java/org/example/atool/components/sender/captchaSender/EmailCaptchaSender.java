package org.example.atool.components.sender.captchaSender;

import lombok.RequiredArgsConstructor;
import org.example.atool.props.CaptchaProp;
import org.example.atool.components.sender.EmailSender;
import org.example.atool.utils.StrFmt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailCaptchaSender implements CaptchaSender {
    private final EmailSender emailSender;
    private final CaptchaProp captchaProp;

    @Override
    public void sendCaptcha(String code, String to) {
        String content = StrFmt.fmt(captchaProp.getEmailTemplate(),"#{captchaCode}",code);
        emailSender.send("CaptchaEmail",content,to);
    }
}
