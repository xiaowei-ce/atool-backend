package org.example.atool.components.sender.captchaSender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AutoCaptchaSender {

    private final CaptchaSender emailCaptchaSender;
    private final CaptchaSender smsCaptchaSender;
    //todo fuckkkkkkkkkkk
    private final Map<String, CaptchaSender> senders = new HashMap<>(){
        {
            put("email",emailCaptchaSender);
            put("phone",smsCaptchaSender);
        }
    };

    public void send(String type, String code, String to) {
        senders.get(type).sendCaptcha(code,to);
    }
}
