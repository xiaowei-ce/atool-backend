package org.example.atool.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.entity.vo.SkyGiftVO;
import org.example.atool.service.SkyService;
import org.example.atool.utils.RegexUtil;
import org.example.atool.utils.Throw;
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
    public Result data(@RequestParam(value = "id", required = false) String id) {
        if (StrUtil.isBlank(id)) {
            Throw.BizExp("好友代码/ID不能为空");
        }
        if (!RegexUtil.matchAny(id, "^[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}$", "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")) {
            Throw.BizExp("好友代码/ID格式不对");
        }
        String data = skyService.data(id);
        return Result.ok("ok", data);
    }

    @GetMapping("/gift")
    public Result gift(@RequestParam(value = "id", required = false) String id) {
        if (StrUtil.isBlank(id)) {
            Throw.BizExp("光遇ID不能为空");
        }
        if (!RegexUtil.isMatch("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", id)) {
            Throw.BizExp("光遇ID格式不对");
        }
        SkyGiftVO vo = skyService.gift(id);
        return Result.ok("查询成功", vo);
    }


}
