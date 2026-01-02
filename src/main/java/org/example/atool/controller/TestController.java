package org.example.atool.controller;

import org.example.atool.entity.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableMethodSecurity
public class TestController {

    @RequestMapping("/test")
    @PreAuthorize(value = "hasAnyRole('user','master')")
    public Result test(){
        return Result.ok("success",null);
    }
}
