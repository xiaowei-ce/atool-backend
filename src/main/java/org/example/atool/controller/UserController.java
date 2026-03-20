package org.example.atool.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.entity.dto.LoginDTO;
import org.example.atool.entity.dto.RegisterDTO;
import org.example.atool.entity.vo.RecordVO;
import org.example.atool.entity.vo.UserDetailVO;
import org.example.atool.props.RegexProp;
import org.example.atool.service.UserService;
import org.example.atool.utils.RegexUtil;
import org.example.atool.utils.Throw;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RegexProp regexProp;

    @PutMapping("/register")
    public Result register(@RequestParam("type") String type, @RequestBody RegisterDTO registerDTO) {
        if (!RegexUtil.isMatch(regexProp.get("password"), registerDTO.getPassword())) {
            Throw.BizExp("密码格式错误");
        }
        if (!RegexUtil.isMatch(regexProp.get(type), registerDTO.getAccount())) {
            Throw.BizExp("账号格式错误");
        }
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
    public Result records(@RequestParam(value = "pageSize",required = false,defaultValue = "90")Integer pageSize){
        if (pageSize > 150) {
            Throw.BizExp("每页大小不超过150");
        }
        //暂时不用前端参数
        List<RecordVO> records = userService.pageGetRecords(0,pageSize);
        return Result.ok("success",records);
    }

    @GetMapping("/exchange")
    public Result exchange_key(@RequestParam("key") String key){
        userService.exchange(key);
        return Result.ok("兑换成功",null);
    }

    @PostMapping("/lottery")
    public Result lottery(){
        Long point = userService.lottery();
        return Result.ok(StrUtil.format("签到成功，获得了{}积分",point),null);
    }

}
