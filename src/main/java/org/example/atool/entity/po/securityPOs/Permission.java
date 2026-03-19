package org.example.atool.entity.po.securityPOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Permission {
  private Long permissionId;
  private String permissionName;
}
