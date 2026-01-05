package org.example.atool.components.sender.captchaSender;

import com.aliyun.dypnsapi20170525.Client;
import lombok.RequiredArgsConstructor;
import org.example.atool.props.CaptchaProp;
import org.example.atool.utils.StrFmt;
import org.example.atool.utils.Throw;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SmsCaptchaSender implements CaptchaSender {
    private final Client aliCloudClient;
    private final CaptchaProp captchaProp;

    @Override
    public void sendCaptcha(String code, String to) {

        com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = new com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest()
                .setSignName("速通互联验证服务")
                .setTemplateCode("100001")
                .setPhoneNumber(to)
                .setTemplateParam(
                        StrFmt.fmt("{\"code\":\"#{fmt}\",\"min\":\"#{fmt}\"}","#{fmt}",code,captchaProp.getTimeoutAs(TimeUnit.MINUTES))
                )
                .setSchemeName("短信验证码")
                .setCountryCode("86")
                .setCodeType(1L)
                .setDuplicatePolicy(1L)
//                .setValidTime(captchaProp.getTimeout())
                .setCodeLength(captchaProp.getCaptchaLen())
//                .setReturnVerifyCode(true)
                .setInterval(captchaProp.getTimeout());
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse resp = aliCloudClient.sendSmsVerifyCodeWithOptions(sendSmsVerifyCodeRequest, runtime);
//            System.out.println(new com.google.gson.Gson().toJson(resp));
        } catch (Exception error) {
            Throw.BizExp(error.getMessage());
            // 诊断地址
//            System.out.println(error.getData().get("Recommend"));
        }




    }

}
