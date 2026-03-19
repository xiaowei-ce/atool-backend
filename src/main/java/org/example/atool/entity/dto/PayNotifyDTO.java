package org.example.atool.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayNotifyDTO {
    private String pid;
    private String trade_no;
    private String out_trade_no;
    private String type;
    private String name;
    private String money;
    private String trade_status;
    private String sign;
    private String sign_type;
}
