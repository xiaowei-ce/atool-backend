package org.example.atool.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.entity.dto.GoodsCountDTO;
import org.example.atool.entity.po.Goods;
import org.example.atool.entity.vo.OrderVO;
import org.example.atool.entity.vo.PayVO;
import org.example.atool.service.ShopService;
import org.example.atool.utils.Throw;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shop")
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/goods")
    public Result goods(){
       List<Goods> goods = shopService.goods();
       return Result.ok("success", goods);
    }

    @PostMapping("/order")
    public Result order(@RequestBody List<GoodsCountDTO> ids){
        if (CollUtil.isEmpty(ids)) {
            Throw.BizExp("传递空数据");
        }
        OrderVO vo = shopService.order(ids);
        return Result.ok("success", vo);
    }

    @GetMapping("/pay")
    public Result pay(@RequestParam("orderId") String orderId){
        if (StrUtil.isBlank(orderId)){
            Throw.BizExp("传递空数据");
        }
        PayVO vo = shopService.pay(orderId);
        return Result.ok("success",vo);
    }
}
