package org.example.atool.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.atool.entity.Result;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@RestController
public class TestController {

    @RequestMapping("/test")
    public Result test(HttpServletRequest request) throws IOException {
        Map<String, Object> map = new LinkedHashMap<>();

        // --- 1. 请求行与网络基础信息 ---
        map.put("Method", request.getMethod());
        map.put("URL", request.getRequestURL().toString());
        map.put("URI", request.getRequestURI());
        map.put("QueryString", request.getQueryString());
        map.put("Protocol", request.getProtocol());
        map.put("RemoteAddr", request.getRemoteAddr());
        map.put("RemotePort", request.getRemotePort());
        map.put("LocalAddr", request.getLocalAddr());
        map.put("LocalPort", request.getLocalPort());
        map.put("Scheme", request.getScheme());

        // --- 2. Headers (全量) ---
        Map<String, String> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        map.put("Headers", headers);

        // --- 3. Cookies ---
        if (request.getCookies() != null) {
            List<String> cookies = new ArrayList<>();
            for (Cookie cookie : request.getCookies()) {
                cookies.add(cookie.getName() + "=" + cookie.getValue());
            }
            map.put("Cookies", cookies);
        }

        // --- 4. Parameters (Query + Form Data) ---
        // 注意：getParameterMap() 会包含 URL 后的参数和 application/x-www-form-urlencoded 的 Body 参数
        map.put("Parameters", request.getParameterMap());

        // --- 5. Body (Raw 内容) ---
        // 只有非 Form 表单提交时，读取 Body 才有意义（如 JSON, XML, Text）
        String contentType = request.getContentType();
        if (contentType != null && !contentType.contains("application/x-www-form-urlencoded")) {
            byte[] bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
            map.put("Body", new String(bodyBytes, StandardCharsets.UTF_8));
        } else {
            map.put("Body", "[Form Data is in Parameters section]");
        }

        // --- 6. 身份认证信息 ---
        map.put("AuthType", request.getAuthType());
        map.put("RemoteUser", request.getRemoteUser());
        map.put("UserPrincipal", request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null);

        return Result.ok("success",map);
    }
}
