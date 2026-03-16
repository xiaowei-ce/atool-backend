package org.example.atool.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PayVO {
    private String money;
    private String name;
    private String notifyUrl;
    private String outTradeNo;
    private String pid;
    private String returnUrl;
    private String sign;
    private String signType;
}
