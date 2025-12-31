package org.example.atool.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Text2JsonConverter {

    private static final Text2JsonConverter Parser = new Text2JsonConverter();

    public String parser(String text) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        // 解析身高分析部分
        ObjectNode heightAnalysis = mapper.createObjectNode();
        String[] lines = text.split("\n");

        // 用于标记当前解析的部分
        String currentSection = "height";

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            // 检查是否是新的部分开始
            if (line.contains("当前角色服装：")) {
                currentSection = "outfit";
                continue;
            } else if (line.contains("查询耗时:") || line.contains("查询时间")) {
                currentSection = "query";
                // 继续处理这一行
            }

            // 根据当前部分处理数据
            switch (currentSection) {
                case "height":
                    parseHeightData(line, heightAnalysis);
                    break;
                case "outfit":
                    parseOutfitData(line, rootNode);
                    break;
                case "query":
                    parseQueryData(line, rootNode);
                    break;
            }
        }

        rootNode.set("height_analysis", heightAnalysis);

        // 输出JSON
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        return json;
    }

    private void parseHeightData(String line, ObjectNode heightAnalysis) {
        // 处理身高分析数据
        if (line.contains("体型值:")) {
            String value = extractValue(line, "体型值:");
            heightAnalysis.put("body_type_value", Double.parseDouble(value));
        } else if (line.contains("身高值:")) {
            String value = extractValue(line, "身高值:");
            heightAnalysis.put("height_value", Double.parseDouble(value));
        } else if (line.contains("最高身高:")) {
            String value = extractValue(line, "最高身高:");
            heightAnalysis.put("max_height", Double.parseDouble(value));
        } else if (line.contains("最矮身高:")) {
            String value = extractValue(line, "最矮身高:");
            heightAnalysis.put("min_height", Double.parseDouble(value));
        } else if (line.contains("当前身高:")) {
            String value = extractValue(line, "当前身高:");
            heightAnalysis.put("current_height", Double.parseDouble(value));
        } else if (line.contains("当前身高描述:")) {
            String value = extractValue(line, "当前身高描述:");
            heightAnalysis.put("current_height_description", value);
        } else if (line.contains("距离最低还差:")) {
            String value = extractValue(line, "距离最低还差:");
            heightAnalysis.put("distance_from_min", Double.parseDouble(value));
        } else if (line.contains("光崽是较瘦体型")) {
            heightAnalysis.put("body_build", "光崽是较瘦体型");
        }
    }

    private void parseOutfitData(String line, ObjectNode rootNode) {
        // 如果outfit节点不存在，创建它
        if (!rootNode.has("current_outfit")) {
            rootNode.set("current_outfit", rootNode.objectNode());
        }

        ObjectNode outfitNode = (ObjectNode) rootNode.get("current_outfit");

        if (line.contains("发型:")) {
            String value = extractValue(line, "发型:");
            outfitNode.put("hair", value);
        } else if (line.contains("面具:")) {
            String value = extractValue(line, "面具:");
            outfitNode.put("mask", value);
        } else if (line.contains("发饰:")) {
            String value = extractValue(line, "发饰:");
            outfitNode.put("hair_accessory", value);
        } else if (line.contains("斗篷:")) {
            String value = extractValue(line, "斗篷:");
            outfitNode.put("cape", value);
        } else if (line.contains("背饰:")) {
            String value = extractValue(line, "背饰:");
            outfitNode.put("back_accessory", value);
        } else if (line.contains("裤子:")) {
            String value = extractValue(line, "裤子:");
            outfitNode.put("pants", value);
        }
    }

    private void parseQueryData(String line, ObjectNode rootNode) {
        // 如果query_info节点不存在，创建它
        if (!rootNode.has("query_info")) {
            rootNode.set("query_info", rootNode.objectNode());
        }

        ObjectNode queryNode = (ObjectNode) rootNode.get("query_info");

        if (line.contains("查询耗时:")) {
            // 提取数字部分，去掉"s"
            String[] parts = line.split(":");
            if (parts.length > 1) {
                String value = parts[1].trim().replace(" s", "");
                queryNode.put("duration_seconds", Double.parseDouble(value));
            }
        } else if (line.contains("查询时间")) {
            // 处理中文冒号
            String[] parts = line.split("：");
            if (parts.length > 1) {
                queryNode.put("timestamp", parts[1].trim());
            }
        }
    }

    private String extractValue(String line, String key) {
        String[] parts = line.split(":");
        if (parts.length > 1) {
            return parts[1].trim();
        }
        return "";
    }
}