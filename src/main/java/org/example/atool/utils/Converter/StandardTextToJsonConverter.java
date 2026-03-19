package org.example.atool.utils.Converter;

import java.util.Map;

/**
 * 简单的文本到JSON转换工具类（不依赖外部库）
 * 将特定格式的文本转换为JSON格式
 */
public class StandardTextToJsonConverter extends TextToJsonConverter {


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
            // 解析文本内容
            Map<String, String> parsedData = TextParser.parseTextContent(text);

            // 验证是否解析到了必要的数据
            validateParsedData(parsedData);

            // 构建JSON字符串
            return buildJsonString(parsedData);
        } catch (Exception e) {
            throw new IllegalArgumentException("文本格式不符合要求: " + e.getMessage(), e);
        }
    }

    /**
     * 验证解析到的数据
     */
    private void validateParsedData(Map<String, String> data) {
        // 检查是否至少解析到一些关键数据
        boolean hasHeightData = data.containsKey("身高解析结果_体型值") ||
                               data.containsKey("身高解析结果_身高值") ||
                               data.containsKey("当前角色服装_发型");

        boolean hasOutfitData = data.containsKey("当前角色服装_发型") ||
                               data.containsKey("当前角色服装_面具");

        if (!hasHeightData && !hasOutfitData) {
            throw new IllegalArgumentException("文本格式不符合要求，未找到有效的键值对数据");
        }
    }

    /**
     * 构建JSON字符串
     */
    private String buildJsonString(Map<String, String> data) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        // 身高分析部分
        json.append("  \"height_analysis\": {\n");
        json.append("    \"body_type_value\": ").append(parseDouble(data.get("身高解析结果_体型值"))).append(",\n");
        json.append("    \"height_value\": ").append(parseDouble(data.get("身高解析结果_身高值"))).append(",\n");
        json.append("    \"max_height\": ").append(parseDouble(data.get("身高解析结果_最高身高"))).append(",\n");
        json.append("    \"min_height\": ").append(parseDouble(data.get("身高解析结果_最矮身高"))).append(",\n");
        json.append("    \"current_height\": ").append(parseDouble(data.get("身高解析结果_当前身高"))).append(",\n");
        json.append("    \"current_height_description\": \"").append(escapeJson(data.get("身高解析结果_当前身高描述"))).append("\",\n");
        json.append("    \"distance_from_min\": ").append(parseDouble(data.get("身高解析结果_距离最低还差"))).append(",\n");
        json.append("    \"body_build\": \"").append(escapeJson(data.get("身高解析结果_体型描述"))).append("\"\n");
        json.append("  },\n");

        // 当前服装部分
        json.append("  \"current_outfit\": {\n");
        json.append("    \"hair\": \"").append(escapeJson(data.get("当前角色服装_发型"))).append("\",\n");
        json.append("    \"mask\": \"").append(escapeJson(data.get("当前角色服装_面具"))).append("\",\n");
        json.append("    \"hair_accessory\": \"").append(escapeJson(data.get("当前角色服装_发饰"))).append("\",\n");
        json.append("    \"cape\": \"").append(escapeJson(data.get("当前角色服装_斗篷"))).append("\",\n");
        json.append("    \"back_accessory\": \"").append(escapeJson(data.get("当前角色服装_背饰"))).append("\",\n");
        json.append("    \"pants\": \"").append(escapeJson(data.get("当前角色服装_裤子"))).append("\"\n");
        json.append("  },\n");

        // 查询信息部分
        json.append("  \"query_info\": {\n");
        json.append("    \"duration_seconds\": ").append(parseDouble(data.get("查询耗时"))).append(",\n");
        json.append("    \"timestamp\": \"").append(escapeJson(data.get("查询时间"))).append("\"\n");
        json.append("  }\n");

        json.append("}");
        return json.toString();
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

    /**
     * 转义JSON字符串中的特殊字符
     */
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

}