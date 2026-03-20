package org.example.atool.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class OrderVO {
    private String id;
    private String name;
    private BigDecimal amount;
    private Integer status;
    private Timestamp createTime;
}

