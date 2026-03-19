package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.Order;

@Mapper
public interface OrderMapper {
    void add(@Param("order") Order order);

    Order statusOrder(@Param("userId") Long userId, @Param("status") Integer status);

    Order orderById(@Param("orderId") String orderId);

    void markStatus(@Param("orderId") String orderId, @Param("status") Integer status);

    Long totalPoints(@Param("orderId") String orderId);
}
