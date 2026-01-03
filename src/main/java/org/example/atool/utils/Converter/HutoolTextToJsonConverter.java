package org.example.atool.utils.Converter;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import org.example.atool.utils.JSONUtil;

import java.util.Map;

/**
 * 使用Hutool库的文本到JSON转换工具类
 * 将特定格式的文本转换为JSON格式
 * Hutool提供了更简洁的API和更好的工具类支持
 */
public class HutoolTextToJsonConverter extends TextToJsonConverter {

    /**
     * 将文本内容转换为JSON字符串
     * @param text 文本内容
     * @return JSON字符串
     * @throws IllegalArgumentException 如果文本格式不符合要求
     */
    @Override
    public String convert(String text) {
        if (StrUtil.isBlank(text)) {
            throw new IllegalArgumentException("文本内容不能为空");
        }

        try {
            // 解析文本内容
            Map<String, String> parsedData = TextParser.hutoolParseTextContent(text);

            // 验证是否解析到了必要的数据
            validateParsedData(parsedData);

            // 构建JSON对象
            JSONObject jsonObject = buildJsonObject(parsedData);

            // 使用Hutool的JSONUtil生成格式化的JSON字符串
            return JSONUtil.toJsonPrettyStr(jsonObject);
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
     * 构建JSON对象
     */
    private JSONObject buildJsonObject(Map<String, String> data) {
        JSONObject rootObject = JSONUtil.createObj();

        // 身高分析部分
        JSONObject heightAnalysis = JSONUtil.createObj();
        heightAnalysis.set("body_type_value", parseDouble(data.get("身高解析结果_体型值")));
        heightAnalysis.set("height_value", parseDouble(data.get("身高解析结果_身高值")));
        heightAnalysis.set("max_height", parseDouble(data.get("身高解析结果_最高身高")));
        heightAnalysis.set("min_height", parseDouble(data.get("身高解析结果_最矮身高")));
        heightAnalysis.set("current_height", parseDouble(data.get("身高解析结果_当前身高")));
        heightAnalysis.set("current_height_description", StrUtil.nullToEmpty(data.get("身高解析结果_当前身高描述")));
        heightAnalysis.set("distance_from_min", parseDouble(data.get("身高解析结果_距离最低还差")));
        heightAnalysis.set("body_build", StrUtil.nullToEmpty(data.get("身高解析结果_体型描述")));
        rootObject.set("height_analysis", heightAnalysis);

        // 当前服装部分
        JSONObject currentOutfit = JSONUtil.createObj();
        currentOutfit.set("hair", StrUtil.nullToEmpty(data.get("当前角色服装_发型")));
        currentOutfit.set("mask", StrUtil.nullToEmpty(data.get("当前角色服装_面具")));
        currentOutfit.set("hair_accessory", StrUtil.nullToEmpty(data.get("当前角色服装_发饰")));
        currentOutfit.set("cape", StrUtil.nullToEmpty(data.get("当前角色服装_斗篷")));
        currentOutfit.set("back_accessory", StrUtil.nullToEmpty(data.get("当前角色服装_背饰")));
        currentOutfit.set("pants", StrUtil.nullToEmpty(data.get("当前角色服装_裤子")));
        rootObject.set("current_outfit", currentOutfit);

        // 查询信息部分
        JSONObject queryInfo = JSONUtil.createObj();
        queryInfo.set("duration_seconds", parseDouble(data.get("查询耗时")));
        queryInfo.set("timestamp", StrUtil.nullToEmpty(data.get("查询时间")));
        rootObject.set("query_info", queryInfo);

        return rootObject;
    }

    /**
     * 解析字符串为double，移除单位
     * 使用Hutool的NumberUtil进行更安全的解析
     */
    private Double parseDouble(String value) {
        if (StrUtil.isBlank(value)) {
            return 0.0;
        }

        // 移除单位（如"s"、"秒"等）和空格
        String cleaned = value.replaceAll("[^0-9.-]", "").trim();
        try {
            // 这里可以使用Hutool的NumberUtil.parseDouble，但为了保持一致性，使用标准方法
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}