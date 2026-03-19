package org.example.atool.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.example.atool.entity.dto.PayNotifyDTO;
import org.example.atool.entity.vo.PayVO;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SignUtil {

    // 预先缓存需要签名的字段，并排好序
    private static final List<Field> PAYVO_CACHED_FIELDS;
    private static final List<Field> PAYNOTIFYDTO_CACHED_FIELDS;

    static {
        PAYVO_CACHED_FIELDS = Arrays.stream(PayVO.class.getDeclaredFields())
                .filter(f -> !f.getName().equals("sign") && !f.getName().equals("sign_type"))
                .sorted(Comparator.comparing(Field::getName)) // ASCII 升序
                .peek(f -> f.setAccessible(true))             // 提前提权
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

        PAYNOTIFYDTO_CACHED_FIELDS = Arrays.stream(PayNotifyDTO.class.getDeclaredFields())
                .filter(f -> !f.getName().equals("sign") && !f.getName().equals("sign_type"))
                .sorted(Comparator.comparing(Field::getName)) // ASCII 升序
                .peek(f -> f.setAccessible(true))             // 提前提权
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    public static String getSignString(Object obj) {
        StringJoiner joiner = new StringJoiner("&");
        try {
            if (obj instanceof PayVO vo) {
                for (Field field : PAYVO_CACHED_FIELDS) {
                    Object value = field.get(vo);
                    // 排除空值
                    if (value != null && !value.toString().isEmpty()) {
                        joiner.add(field.getName() + "=" + value);
                    }
                }

            } else if (obj instanceof PayNotifyDTO dto) {
                for (Field field : PAYNOTIFYDTO_CACHED_FIELDS) {
                    Object value = field.get(dto);
                    // 排除空值
                    if (value != null && !value.toString().isEmpty()) {
                        joiner.add(field.getName() + "=" + value);
                    }
                }
            }else {
                Throw.RTExp("不允许的类型");
            }
        } catch (IllegalAccessException e) {
            Throw.BizExp("签名发生错误");
        }

        return Objects.requireNonNull(joiner.toString(), "签名发生异常");
    }


    public static String md5Sign(Object obj, String ePayKey) {
        return Objects.requireNonNull(DigestUtil.md5Hex(
                StrUtil.format("{}{}", getSignString(obj), ePayKey)
        ), "签名发生异常");
    }
}