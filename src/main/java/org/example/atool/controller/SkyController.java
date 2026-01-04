package org.example.atool.controller;

import lombok.RequiredArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.service.SkyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sky")
public class SkyController {

    private final SkyService skyService;

    @GetMapping("/data")
    public Result data(@RequestParam("code") String code){
        String data = skyService.data(code);
        return Result.ok("ok",data);
    }
}
