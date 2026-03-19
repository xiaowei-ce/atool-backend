package org.example.atool.entity.po.securityPOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class RolePermission {
  private Long roleId;
  private Long permissionId;
}
