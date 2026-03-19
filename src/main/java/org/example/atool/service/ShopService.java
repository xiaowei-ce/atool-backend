package org.example.atool.service;

import org.example.atool.entity.dto.GoodsCountDTO;
import org.example.atool.entity.po.Goods;
import org.example.atool.entity.vo.OrderVO;
import org.example.atool.entity.vo.PayVO;

import java.util.List;

public interface ShopService {
    List<Goods> goods();

    OrderVO order(List<GoodsCountDTO> ids);

    PayVO pay(String orderId);
}
