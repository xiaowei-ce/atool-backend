package org.example.atool.configs;

import org.example.atool.Exp.BizException;
import org.example.atool.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExpHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result RTExp(RuntimeException exp){
        exp.printStackTrace();
        return Result.err("服务内部错误！", null);
    }

    @ExceptionHandler(BizException.class)
    public Result BizExp(BizException exp){
        exp.printStackTrace();
        return Result.err(exp.getMessage(), null);
    }


}
