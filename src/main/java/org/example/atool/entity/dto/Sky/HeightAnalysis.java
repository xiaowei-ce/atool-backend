package org.example.atool.entity.dto.Sky;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class HeightAnalysis {
    private Double bodyTypeValue; // 体型值
    private Double heightValue; // 身高值
    private Double maxHeight; // 最高身高值
    private Double minHeight; // 最矮身高值
    private Double currentHeight; // 当前身高值
    private String currentHeightDescription; // 当前身高描述
    private Double distanceFromMin; // 距离最低还差
    private String bodyBuild; // 体型描述
}
