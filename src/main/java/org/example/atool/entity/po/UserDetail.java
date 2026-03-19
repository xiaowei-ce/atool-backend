package org.example.atool.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserDetail {
  private Long userId;
  private String username;
  private String avatar;
  private Long points;
  private Long count;
  private Date latestLottery;
}
