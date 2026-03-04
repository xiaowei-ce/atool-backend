package org.example.atool.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class SkyGiftVO {
    private Integer totalCount;
    private Integer totalPrice;
    private List<GiftItem> items;
    private List unknownProductIds;

    @Data
    static class GiftItem{
        private String name;
        private Integer price;
    }
}
