package org.example.atool.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.atool.entity.po.Goods;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderGoodsVO {
    private Goods goods;
    private Integer count;
}
