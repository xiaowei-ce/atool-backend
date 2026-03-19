package org.example.atool.controller;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.example.atool.entity.dto.PayNotifyDTO;
import org.example.atool.props.EPayProp;
import org.example.atool.service.PayNotifyService;
import org.example.atool.utils.SignUtil;
import org.example.atool.utils.Throw;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/pay/notify")
@AllArgsConstructor
public class PayNotifyController {

    private final PayNotifyService payNotifyService;
    private final EPayProp ePayProp;

    @GetMapping("/call")
    public String callback(PayNotifyDTO dto){
        return check(dto);
    }

    @GetMapping("/href")
    public String href(PayNotifyDTO dto){
        return check(dto);
    }

    @NonNull
    private String check(PayNotifyDTO dto) {
        if (Objects.nonNull(dto)){
            String md5Sign = SignUtil.md5Sign(dto, ePayProp.getKey());
            if (!StrUtil.equals(md5Sign,dto.getSign())){
                Throw.BizExp("签名校验失败");
            }
            if (!StrUtil.equals(dto.getTrade_status(),"TRADE_SUCCESS")){
                Throw.BizExp("支付未成功");
            }
            payNotifyService.payment(dto);
            return "success";
        }
        return "";
    }
}
