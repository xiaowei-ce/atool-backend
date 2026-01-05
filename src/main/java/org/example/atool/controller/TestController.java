package org.example.atool.controller;

import org.example.atool.entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public Result test(){
        return Result.ok("success",null);
    }
}
