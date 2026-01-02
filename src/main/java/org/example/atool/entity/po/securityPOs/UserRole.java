package org.example.atool.entity.po.securityPOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserRole {
  private Long userId;
  private Long roleId;
}
