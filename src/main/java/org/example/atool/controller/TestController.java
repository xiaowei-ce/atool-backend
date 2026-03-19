package org.example.atool.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.atool.entity.Result;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @RequestMapping("/test")
    public Result test(@RequestHeader(value = "User-Agent") String ua){
        return Result.ok("success",null);
    }
}
