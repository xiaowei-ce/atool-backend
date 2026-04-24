package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.OrderGoods;

import java.util.List;

@Mapper
public interface OrderGoodsMapper {
    void add(@Param("orderGoods") List<OrderGoods> orderGoods);

    List<OrderGoods> orderGoods(@Param("orderId") Long orderId);
}
