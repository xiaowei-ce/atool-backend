package org.example.atool.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {

  public static final Integer PENDING = 1, PAYED = 2, CANCELED = 3;

  private String id;
  private BigDecimal amount;
  private String name;
  private Long createBy;
  private Integer status;
  private Timestamp createTime;
}
