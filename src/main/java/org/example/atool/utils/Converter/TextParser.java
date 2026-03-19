package org.example.atool.utils.Converter;

import java.util.HashMap;
import java.util.Map;

public class TextParser {
    /**
     * 解析文本内容为键值对
     */
    public static Map<String, String> parseTextContent(String text) {
        Map<String, String> data = new HashMap<>();
        String[] lines = text.split("\n");

        String currentSection = null;
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue; // 跳过空行
            }

            // 检查是否是章节标题
            if (line.endsWith("：") || line.endsWith(":")) {
                currentSection = line.replace("：", "").replace(":", "").trim();
                continue;
            }

            // 特殊处理"光崽是较瘦体型"这样的行
            if (line.contains("光崽是") && line.contains("体型")) {
                if (currentSection != null) {
                    data.put(currentSection + "_体型描述", line);
                }
                continue;
            }

            // 解析键值对（支持中文和英文冒号）
            if (line.contains(":") || line.contains("：")) {
                // 统一替换中文冒号为英文冒号
                String normalizedLine = line.replace("：", ":");
                String[] parts = normalizedLine.split(":", 2);
                trim(data, currentSection, parts);
            }
        }

        return data;
    }

    private static void trim(Map<String, String> data, String currentSection, String[] parts) {
        if (parts.length == 2) {
            String key = parts[0].trim();
            String value = parts[1].trim();

            // 如果有关联的章节，添加到键名中
            if (currentSection != null && !key.equals("查询耗时") && !key.equals("查询时间")) {
                key = currentSection + "_" + key;
            }

            data.put(key, value);
        }
    }


}
