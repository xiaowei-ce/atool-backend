package org.example.atool.controller;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.Result;
import org.example.atool.entity.dto.SubmitGoodsDTO;
import org.example.atool.entity.po.Goods;
import org.example.atool.entity.vo.OrderVO;
import org.example.atool.entity.vo.OrdersGoodsInfoVO;
import org.example.atool.entity.vo.PayVO;
import org.example.atool.service.ShopService;
import org.example.atool.utils.Throw;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
    public Result order(@RequestBody List<SubmitGoodsDTO> submits){
        if (CollUtil.isEmpty(submits)) {
            Throw.BizExp("商品数据为空");
        }
        OrderVO vo = shopService.order(submits);
        return Result.ok("success", vo);
    }

    @GetMapping("/pay/{orderId}")
    public Result pay(@PathVariable Long orderId){
        if (Objects.isNull(orderId)) {
            Throw.BizExp("订单号为空");
        }
        PayVO vo = shopService.pay(orderId);
        return Result.ok("success",vo);
    }

    @GetMapping("/order/by/{orderId}")
    public Result orderByOrderId(@PathVariable("orderId") Long orderId){
        if (Objects.isNull(orderId)) {
            Throw.BizExp("订单号为空");
        }
        OrderVO orderVO = shopService.orderById(orderId);
        return Result.ok("success",orderVO);
    }

    @GetMapping("/orderGoodsInfos/{orderId}")
    public Result orderGoodsInfos(@PathVariable Long orderId){
        if (Objects.isNull(orderId)) {
            Throw.BizExp("订单号为空");
        }
        List<OrdersGoodsInfoVO> orderGoods = shopService.orderGoods(orderId);
        return Result.ok("success",orderGoods);
    }

    @GetMapping("/orders")
    public Result orders(){
        List<OrderVO> orders = shopService.orders(20);
        return Result.ok("success",orders);
    }

    @PutMapping("/markCanceled/{orderId}")
    public Result markOrderCanceled(@PathVariable Long orderId){
        if (Objects.isNull(orderId)) {
            Throw.BizExp("订单号为空");
        }
        shopService.markCanceled(orderId);
        return Result.ok("取消成功",null);
    }
}
