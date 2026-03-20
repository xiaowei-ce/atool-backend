package org.example.atool.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrdersGoodsInfoVO {
    private Integer count;
    private Long id;
    private String name;
    private BigDecimal price;
    private Boolean forSale;
}
