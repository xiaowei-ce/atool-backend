package org.example.atool.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j()
public class ControllerAspect {

    @Before("execution(* org.example.atool.controller.*.*(..))")
    public void log(JoinPoint point){
        Signature signature = point.getSignature();
        String name = signature.getName();
        Object[] args = point.getArgs();
        log.info("{}() args:{}",name,Arrays.toString(args));
    }
}
