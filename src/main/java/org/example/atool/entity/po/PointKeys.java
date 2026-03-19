package org.example.atool.entity.po;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointKeys {
  private String key;
  private Long points;
  private Boolean used;
  private String who;
}
