package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.Order;
import org.example.atool.entity.vo.OrderVO;
import org.example.atool.entity.vo.OrdersGoodsInfoVO;

import java.util.List;

@Mapper
public interface OrderMapper {
    void add(@Param("order") Order order);

    Order statusOrder(@Param("userId") Long userId, @Param("status") Integer status);

    Order orderById(@Param("orderId") Long orderId);

    void markStatus(@Param("orderId") Long orderId, @Param("status") Integer status);

    Long totalPoints(@Param("orderId") Long orderId);

    List<OrderVO> pageOrders(@Param("userId") Long userId, @Param("size") Integer size);

    List<OrdersGoodsInfoVO> goodsInfos(@Param("orderId") Long orderId);

    Integer markCanceled(@Param("orderId") Long orderId);
}
