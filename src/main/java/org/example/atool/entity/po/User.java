package org.example.atool.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class User {

  private Long id;
  private String account;
  private String password;

  private Boolean enable;
  private Boolean delete;
}
