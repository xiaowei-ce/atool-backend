package org.example.atool.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.dto.GoodsCountDTO;
import org.example.atool.entity.po.Goods;
import org.example.atool.entity.po.Order;
import org.example.atool.entity.po.User;
import org.example.atool.entity.vo.OrderGoods;
import org.example.atool.entity.vo.OrderVO;
import org.example.atool.mapper.GoodsMapper;
import org.example.atool.service.ShopService;
import org.example.atool.utils.PrincipalUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final GoodsMapper goodsMapper;

    @Override
    public List<Goods> goods() {
        return goodsMapper.onSaleGoods(true);
    }

    @Override
    public OrderVO order(List<GoodsCountDTO> counts) {
        User user = PrincipalUtil.user();
        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String sha256Hex = DigestUtil.sha256Hex(user.getAccount());
        String sub = StrUtil.sub(sha256Hex, sha256Hex.length() - 8, sha256Hex.length());
        String orderId = StrUtil.format("{}{}", nowStr, sub);

        Order order = new Order();
        order.setId(orderId);
        order.setName("积分订单");
        order.setStatus(Order.PENDING);
        order.setCreateBy(user.getId());
        order.setAmount(goodsMapper.totalPrice(counts));

        OrderVO vo = new OrderVO();
        BeanUtil.copyProperties(order, vo);
        List<Long> ids = counts.stream().map(GoodsCountDTO::getId).toList();
        List<Goods> goods = goodsMapper.onSaleGoodsByIds(ids);
        Map<Long, Integer> countsMap = counts.stream().collect(Collectors.toMap(GoodsCountDTO::getId, GoodsCountDTO::getCount));
        List<OrderGoods> orderGoods = goods.stream().map(it -> new OrderGoods(it, countsMap.get(it.getId()))).toList();
        vo.setOrderGoods(orderGoods);

        return vo;
    }
}
