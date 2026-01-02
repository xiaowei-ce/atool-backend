package org.example.atool.controller;

import lombok.RequiredArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.service.CaptchaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping("/send")
    public Result get(@RequestParam("target") String target,@RequestParam("type") String type){
        captchaService.send(type,target);
        return Result.ok("发送成功",null);
    }
}
