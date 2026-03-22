package org.example.atool.entity.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
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

  @TableLogic(delval = "1", value = "0")
  private Boolean deleted;
}
