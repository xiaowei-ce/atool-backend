package org.example.atool.components.sender.captchaSender;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AutoCaptchaSender {

    private final Map<String, CaptchaSender> senders = new HashMap<>();
    public AutoCaptchaSender(@Qualifier("emailCaptchaSender") CaptchaSender emailCaptchaSender,@Qualifier("smsCaptchaSender") CaptchaSender smsCaptchaSender){
        this.senders.put("email",emailCaptchaSender);
        this.senders.put("phone",smsCaptchaSender);
    }

    public void send(String type, String code, String to) {
        senders.get(type).sendCaptcha(code,to);
    }
}
