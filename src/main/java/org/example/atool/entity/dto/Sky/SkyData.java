package org.example.atool.entity.dto.Sky;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkyData {
    private Outfit currentOutfit;
    private QueryInfo queryInfo;
    private HeightAnalysis heightAnalysis;
}