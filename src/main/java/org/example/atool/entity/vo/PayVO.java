package org.example.atool.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PayVO {
    private String money;
    private String name;
    private String notify_url;
    private String out_trade_no;
    private Integer pid;
    private String return_url;
    private String sign;
    private String sign_type;
}
