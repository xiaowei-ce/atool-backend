package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.atool.entity.dto.GoodsCountDTO;
import org.example.atool.entity.po.Goods;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface GoodsMapper {
    List<Goods> onSaleGoods(Boolean forSale);

    List<Goods> onSaleGoodsByIds(List<Long> ids);

    BigDecimal totalPrice(List<GoodsCountDTO> counts);
}
