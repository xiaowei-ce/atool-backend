package org.example.atool.controller;

import cn.hutool.core.util.StrUtil;
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
    public Result data(@RequestParam(value = "id", required = false) String id){
        if(StrUtil.isBlank(id)){
            return Result.err("光遇代码为空",null);
        }
        String data = skyService.data(id);
        return Result.ok("ok",data);
    }
}
