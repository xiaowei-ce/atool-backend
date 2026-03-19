package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.dto.GoodsCountDTO;
import org.example.atool.entity.po.Goods;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface GoodsMapper {
    List<Goods> onSaleGoods(@Param("froSale") Boolean forSale);

    List<Goods> onSaleGoodsByIds(@Param("ids") List<Long> ids);

    BigDecimal totalPrice(@Param("counts") List<GoodsCountDTO> counts);
}
