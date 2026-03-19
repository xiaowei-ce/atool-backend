package org.example.atool.entity.po;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EPayOrderRecord {
    private String epayNo;
    private String orderId;
    private String payType;
    private String epayStatus;
}
