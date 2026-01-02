package org.example.atool.entity.po.securityPOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Role {
  private Long roleId;
  private String roleName;
}
