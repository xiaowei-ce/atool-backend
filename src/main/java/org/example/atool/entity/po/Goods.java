package org.example.atool.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Goods {
    private Long id;
    private String name;
    private BigDecimal price;
    private Boolean forSale;
    private Long point;
}
