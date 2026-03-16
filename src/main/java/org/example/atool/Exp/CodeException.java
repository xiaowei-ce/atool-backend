package org.example.atool.Exp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CodeException extends BizException{

    private Integer code;
    public CodeException(Integer code,String msg) {
        super(msg);
        this.code = code;
    }
}
