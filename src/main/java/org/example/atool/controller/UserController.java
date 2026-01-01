package org.example.atool.controller;

import lombok.RequiredArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.entity.dto.RegisterDTO;
import org.example.atool.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/register")
    public Result register(@RequestParam("type") String type,@RequestBody RegisterDTO registerDTO){
        userService.register(type,registerDTO);
        return Result.ok("success",null);
    }

}
