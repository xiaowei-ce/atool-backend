package org.example.atool.components.sender.captchaSender;

import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
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

        SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = new SendSmsVerifyCodeRequest()
                .setSignName("速通互联验证服务")
                .setTemplateCode("100001")
                .setPhoneNumber(to)
                .setTemplateParam(
                        StrFmt.fmt("{\"code\":\"#{fmt}\",\"min\":\"#{fmt}\"}","#{fmt}",code,captchaProp.getExpireAs(TimeUnit.MINUTES))
                )
                .setSchemeName("短信验证码")
                .setCountryCode("86")
                .setCodeType(1L)
                .setDuplicatePolicy(1L)
//                .setValidTime(captchaProp.getTimeout())
                .setCodeLength(captchaProp.getCaptchaLen())
//                .setReturnVerifyCode(true)
                .setInterval(captchaProp.getResendIn());
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            SendSmsVerifyCodeResponse resp = aliCloudClient.sendSmsVerifyCodeWithOptions(sendSmsVerifyCodeRequest, runtime);
            if(resp.getStatusCode() != 200){
                Throw.BizExp("请求出错！");
            }
            if (resp.getStatusCode() == 200){
                SendSmsVerifyCodeResponseBody respBody = resp.getBody();
                if (!respBody.getSuccess()) {
                    Throw.BizExp(respBody.getMessage());
                }
            }
//            System.out.println(new com.google.gson.Gson().toJson(resp));
        } catch (Exception error) {
            Throw.BizExp(error.getMessage());
            // 诊断地址
//            System.out.println(error.getData().get("Recommend"));
        }




    }

}
