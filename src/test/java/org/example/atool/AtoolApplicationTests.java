package org.example.atool;

import org.example.atool.components.sender.captchaSender.AutoCaptchaSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AtoolApplicationTests {


    @Autowired
    private AutoCaptchaSender autoCaptchaSender;

    @Test
    void contextLoads() {


//        System.out.println(EmailFormat.captchaFmt(captchaProp.getEmailTemplate(),"78645"));

//        captchaService.send("email","starryflow@outlook.com");

//        System.out.println(StrFmt.fmt("{\"code\":\"#{fmt}\",\"min\":\"#{fmt}\"}", "#{fmt}", "43545", captchaProp.getTimeOutAsMin()));
//        System.out.println(StrFmt.fmt("{\"code\":\"#{fmt}\",\"min\":\"#{fmt}\"}", "#{fmt}", "23549", captchaProp.getTimeoutAs(TimeUnit.MINUTES)));
//        emailService.send("TestEmail","hello this is test email","starryflow@outlook.com");

        autoCaptchaSender.send("phone","666666","13479066201");
    }
}
