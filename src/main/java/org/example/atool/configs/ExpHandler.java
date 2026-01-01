package org.example.atool.configs;

import org.example.atool.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExpHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result RTExp(RuntimeException exp){
        return Result.err(exp.getMessage(), null);
    }
}
