package org.example.atool.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.RequiredArgsConstructor;
import org.example.atool.entity.dto.SubmitGoodsDTO;
import org.example.atool.entity.po.Goods;
import org.example.atool.entity.po.Order;
import org.example.atool.entity.po.OrderGoods;
import org.example.atool.entity.po.User;
import org.example.atool.entity.vo.OrderVO;
import org.example.atool.entity.vo.OrdersGoodsInfoVO;
import org.example.atool.entity.vo.PayVO;
import org.example.atool.mapper.GoodsMapper;
import org.example.atool.mapper.OrderGoodsMapper;
import org.example.atool.mapper.OrderMapper;
import org.example.atool.props.EPayProp;
import org.example.atool.service.ShopService;
import org.example.atool.utils.JSONUtil;
import org.example.atool.utils.PrincipalUtil;
import org.example.atool.utils.SignUtil;
import org.example.atool.utils.Throw;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final GoodsMapper goodsMapper;
    private final OrderMapper orderMapper;
    private final OrderGoodsMapper orderGoodsMapper;
    private final EPayProp ePayProp;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<Goods> goods() {
        String saleGoodsCache = (String) stringRedisTemplate.opsForValue().get("cache:sale_goods");

        List<Goods> saleGoods;
        if (StrUtil.isNotBlank(saleGoodsCache)){
            saleGoods = JSONUtil.toList(saleGoodsCache, Goods.class);
        }else {
            saleGoods = goodsMapper.onSaleGoods(true);
            stringRedisTemplate.opsForValue().set("cache:sale_goods",JSONUtil.toJsonStrIncludeNull(saleGoods));
        }
        return saleGoods;
    }

    @Override
    @Transactional(rollbackFor = {Error.class, Exception.class})
    public OrderVO order(List<SubmitGoodsDTO> counts) {

        User user = PrincipalUtil.user();
        Order pendingOrder = orderMapper.statusOrder(user.getId(), Order.PENDING);
        if (Objects.nonNull(pendingOrder)){
            Throw.BizExp("有未支付订单，请先取消或完成支付再下单");
        }
        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String sha256Hex = DigestUtil.sha256Hex(user.getAccount());
        String sub = StrUtil.sub(sha256Hex, sha256Hex.length() - 8, sha256Hex.length());
        String orderId = StrUtil.format("{}{}", nowStr, sub);

        Order order = new Order();
        order.setId(orderId);
        order.setName("积分订单");
        order.setStatus(Order.PENDING);
        order.setCreateBy(user.getId());
        order.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        order.setAmount(goodsMapper.totalPrice(counts));
        orderMapper.add(order);

        List<OrderGoods> orderGoodsList = counts.stream().map(count -> {
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setOrderId(orderId);
            orderGoods.setGoodsId(count.getId());
            orderGoods.setGoodsCount(count.getCount());
            return orderGoods;
        }).toList();
        orderGoodsMapper.add(orderGoodsList);
        OrderVO vo = new OrderVO();
        BeanUtil.copyProperties(order, vo);
        return vo;
    }

    @Override
    public PayVO pay(String orderId) {
        Order order = orderMapper.orderById(orderId);
        if (Objects.isNull(order)){
            Throw.BizExp("订单号不存在");
        }
        if (!Objects.equals(order.getStatus(), Order.PENDING)){
            Throw.BizExp("订单状态不一致");
        }
        PayVO vo = new PayVO();
        vo.setMoney(order.getAmount().setScale(2, RoundingMode.HALF_DOWN).toString());
        vo.setName(order.getName());
        vo.setOut_trade_no(order.getId());
        vo.setPid(ePayProp.getPid());
        vo.setReturn_url(ePayProp.getReturn_url());
        vo.setNotify_url(ePayProp.getNotify_url());
        vo.setSign_type("MD5");
        vo.setSign(SignUtil.md5Sign(vo,ePayProp.getKey()));
        return vo;
    }

    @Override
    public List<OrderVO> orders(Integer size) {
        User user = PrincipalUtil.user();
        return orderMapper.pageOrders(user.getId(), size);
    }

    @Override
    public List<OrdersGoodsInfoVO> orderGoods(String orderId) {
        return orderMapper.goodsInfos(orderId);
    }

    @Override
    public void markCanceled(String orderId) {
        orderMapper.markStatus(orderId,Order.CANCELED);
    }
}
