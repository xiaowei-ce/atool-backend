package org.example.atool.controller;

import lombok.AllArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.entity.dto.PayNotifyDTO;
import org.example.atool.service.PayNotifyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay/notify")
@AllArgsConstructor
public class PayNotifyController {

    private final PayNotifyService payNotifyService;

    @GetMapping("/call")
    public String callback(PayNotifyDTO dto){
        payNotifyService.payment(dto);
        return "success";
    }

    @PostMapping("/href")
    public Result href(@RequestBody PayNotifyDTO dto){
        payNotifyService.payment(dto);
        return Result.ok("success",null);
    }

}
