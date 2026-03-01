package org.example.atool.controller;

import lombok.RequiredArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.entity.dto.LoginDTO;
import org.example.atool.entity.dto.RegisterDTO;
import org.example.atool.entity.vo.RecordVO;
import org.example.atool.entity.vo.UserDetailVO;
import org.example.atool.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/register")
    public Result register(@RequestParam("type") String type, @RequestBody RegisterDTO registerDTO) {
        userService.register(type, registerDTO);
        return Result.ok("注册成功", null);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO,@RequestHeader(value = "Authorization" ,required = false) String Authorization) {
        String token = userService.login(loginDTO,Authorization);
        return Result.ok("登录成功", token);
    }

    @GetMapping("/details")
    public Result detail(){
        UserDetailVO details = userService.details();
        return Result.ok("获取成功",details);
    }

    @GetMapping("/records")
    public Result records(){
        List<RecordVO> records = userService.pageGetRecords(0,35);
        return Result.ok("success",records);
    }

    @GetMapping("/exchange")
    public Result exchange_key(@RequestParam("key") String key){
        System.out.println(key);
        userService.exchange(key);
        return Result.ok("兑换成功",null);
    }

}
