package org.example.atool.entity.po;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderGoods {
    private Long orderId;
    private Long goodsId;
    private Integer goodsCount;
}
