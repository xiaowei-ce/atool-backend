package org.example.atool.components.sender.captchaSender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsCaptchaSender implements CaptchaSender {
    @Override
    public void sendCaptcha(String code, String to) {

    }
}
