package org.example.atool.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class Msgs {


    public static final String  EXCHANGE = "atool.topic";
    public static final String  DLX_EXCHANGE = "atool.dlx.topic";

    public static final String CAPTCHA_WILL_SEND_QUEUE = "atool.captcha.will_send";
    public static final String CAPTCHA_WILL_SEND_ROUTING_KEY = "atool.captcha.will_send";

    public static final String ORDER_CANCEL_TTL_QUEUE = "atool.order.cancel_ttl";
    public static final String ORDER_CANCEL_TTL_ROUTING_KEY = "atool.order.cancel_ttl";

    public static final String ORDER_CANCEL_QUEUE = "atool.order.cancel";
    public static final  String ORDER_CANCEL_ROUTING_KEY = "atool.order.cancel";



    @Data
    @RequiredArgsConstructor
    public static class CaptchaSendMsg {

       private final String type, target;
    }

    @Data
    @RequiredArgsConstructor
    public static class CancelOrderMsg {

        private final List<String> timeCycle = List.of("15000","15000","15000","15000","30000","30000","45000","45000","45000","45000"); //ms

        private Integer now = 0;

        private final Long orderId;

        public boolean offest2Next(){
            if (now + 1 == timeCycle.size()){
                return false;
            }
            this.now = now + 1;
            return true;
        }

        public String nextExp(){
            return timeCycle.get(now + 1);
        }

        public String nowExp(){
            return timeCycle.get(now);
        }
    }
}
