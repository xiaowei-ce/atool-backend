package org.example.atool.utils.Converter;

import java.util.HashMap;
import java.util.Map;

public abstract class TextToJsonConverter {
    private static final Map<ContertersType, TextToJsonConverter> converters = new HashMap<>() {
        {
            put(ContertersType.JACKSON, new JacksonTextToJsonConverter());
            put(ContertersType.HUTOOL, new HutoolTextToJsonConverter());
            put(ContertersType.STANDARD, new StandardTextToJsonConverter());
        }
    };


    abstract String convert(String text);

    public static String convert(String text, ContertersType type) {
        TextToJsonConverter textToJsonConverter = converters.get(type);
        return textToJsonConverter.convert(text);
    }
}
