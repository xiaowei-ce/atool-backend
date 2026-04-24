package org.example.atool.service;

import org.example.atool.entity.dto.SubmitGoodsDTO;
import org.example.atool.entity.po.Goods;
import org.example.atool.entity.vo.OrdersGoodsInfoVO;
import org.example.atool.entity.vo.OrderVO;
import org.example.atool.entity.vo.PayVO;

import java.util.List;

public interface ShopService {
    List<Goods> goods();

    OrderVO order(List<SubmitGoodsDTO> ids);

    PayVO pay(Long orderId);

    List<OrderVO> orders(Integer size);

    List<OrdersGoodsInfoVO> orderGoods(Long orderId);

    void markCanceled(Long orderId);

    OrderVO orderById(Long orderId);
}
