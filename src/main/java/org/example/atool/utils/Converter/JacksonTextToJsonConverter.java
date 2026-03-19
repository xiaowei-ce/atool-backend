package org.example.atool.utils.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

/**
 * 文本到JSON转换工具类
 * 将特定格式的文本转换为JSON格式
 */
public class JacksonTextToJsonConverter extends TextToJsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将文本内容转换为JSON字符串
     * @param text 文本内容
     * @return JSON字符串
     * @throws IllegalArgumentException 如果文本格式不符合要求
     */
    @Override
    public String convert(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("文本内容不能为空");
        }

        try {
            ObjectNode rootNode = objectMapper.createObjectNode();

            // 解析文本内容
            Map<String, String> parsedData = TextParser.parseTextContent(text);

            // 构建JSON结构
            buildJsonStructure(rootNode, parsedData);

            // 美化输出
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (Exception e) {
            throw new IllegalArgumentException("文本格式不符合要求: " + e.getMessage(), e);
        }
    }

    /**
     * 构建JSON结构
     */
    private void buildJsonStructure(ObjectNode rootNode, Map<String, String> data) {
        // 创建身高分析节点
        ObjectNode heightAnalysis = objectMapper.createObjectNode();
        heightAnalysis.put("body_type_value", parseDouble(data.get("身高解析结果_体型值")));
        heightAnalysis.put("height_value", parseDouble(data.get("身高解析结果_身高值")));
        heightAnalysis.put("max_height", parseDouble(data.get("身高解析结果_最高身高")));
        heightAnalysis.put("min_height", parseDouble(data.get("身高解析结果_最矮身高")));
        heightAnalysis.put("current_height", parseDouble(data.get("身高解析结果_当前身高")));
        heightAnalysis.put("current_height_description", data.get("身高解析结果_当前身高描述"));
        heightAnalysis.put("distance_from_min", parseDouble(data.get("身高解析结果_距离最低还差")));
        heightAnalysis.put("body_build", data.get("身高解析结果_体型描述"));

        rootNode.set("height_analysis", heightAnalysis);

        // 创建当前服装节点
        ObjectNode currentOutfit = objectMapper.createObjectNode();
        currentOutfit.put("hair", data.get("当前角色服装_发型"));
        currentOutfit.put("mask", data.get("当前角色服装_面具"));
        currentOutfit.put("hair_accessory", data.get("当前角色服装_发饰"));
        currentOutfit.put("cape", data.get("当前角色服装_斗篷"));
        currentOutfit.put("back_accessory", data.get("当前角色服装_背饰"));
        currentOutfit.put("pants", data.get("当前角色服装_裤子"));

        rootNode.set("current_outfit", currentOutfit);

        // 创建查询信息节点
        ObjectNode queryInfo = objectMapper.createObjectNode();
        queryInfo.put("duration_seconds", parseDouble(data.get("查询耗时")));
        queryInfo.put("timestamp", data.get("查询时间"));

        rootNode.set("query_info", queryInfo);
    }

    /**
     * 解析字符串为double，移除单位
     */
    private Double parseDouble(String value) {
        if (value == null) {
            return 0.0;
        }

        // 移除单位（如"s"、"秒"等）和空格
        String cleaned = value.replaceAll("[^0-9.-]", "").trim();
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

}