package org.example.atool.configs;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.atool.Exp.BizException;
import org.example.atool.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExpHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result RTExp(RuntimeException exp){
        log.error("{}{}","RTExp -> ", mkMsg( exp));
        return Result.err("服务内部错误！", null);
    }

    @ExceptionHandler(BizException.class)
    public Result BizExp(BizException exp){
        log.error("{}{}","BizExp ", mkMsg(exp));
        return Result.err(exp.getMessage(), null);
    }

    private String mkMsg(Exception exp) {
        List<String> stackTraces = Arrays.stream(exp.getStackTrace())
                .map(StackTraceElement::toString)
                .filter(it -> StrUtil.containsAll(it, "org.example.atool",".java"))
                .toList();

        StringBuilder stringBuilder = new StringBuilder(exp.getMessage()).append("\n");
        stackTraces.forEach((element)->{
            stringBuilder.append(element);
            stringBuilder.append("\n");
        });
        return stringBuilder.toString();
    }
}
