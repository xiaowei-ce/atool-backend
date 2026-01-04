package org.example.atool;

import org.example.atool.props.CaptchaProp;
import org.example.atool.service.CaptchaService;
import org.example.atool.components.sender.EmailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AtoolApplicationTests {


    @Autowired
    private EmailSender emailService;
    @Autowired
    private CaptchaProp captchaProp;
    @Autowired
    private CaptchaService captchaService;

    @Test
    void contextLoads() {


//        System.out.println(EmailFormat.captchaFmt(captchaProp.getEmailTemplate(),"78645"));

        captchaService.send("email","starryflow@outlook.com");

//        emailService.send("TestEmail","hello this is test email","starryflow@outlook.com");
    }
}
