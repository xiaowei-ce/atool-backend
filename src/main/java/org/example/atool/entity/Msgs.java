package org.example.atool.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class Msgs {

    public static final String CAPTCHA_WILL_SEND_QUEUE = "atool.captcha.will_send";
    public static final String CAPTCHA_WILL_SEND_ROUTING_KEY = "atool.captcha.will_send";
    public static final String EXCHANGE = "atool.topic";



    @Data
    @RequiredArgsConstructor
    public static class CaptchaSendMsg {

       private final String type, target;
    }
}
