package org.example.atool.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class UserDetailVO {
    private String account;
    private String username;
    private String avatar;
    private Long points;
    private String staus;
}