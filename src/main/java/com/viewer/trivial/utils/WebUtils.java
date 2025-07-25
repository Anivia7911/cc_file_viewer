package com.viewer.trivial.utils;

import io.micrometer.common.util.StringUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author hcw
 * @date 2025/7/7 15:20:41
 */
public class WebUtils {

    public static String decodeUrl(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        return decodeBase64String(filePath, StandardCharsets.UTF_8);
    }

    public static String decodeBase64String(String filePath, Charset charsets) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        /*
         * url 传入的参数里加号会被替换成空格，导致解析出错，这里需要把空格替换回加号
         * 有些 Base64 实现可能每 76 个字符插入换行符，也一并去掉
         */
        return new String(decodeFromString(filePath.replaceAll(" ", "+").replaceAll("\n", "")), charsets);
    }


    public static byte[] decodeFromString(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return new byte[0];
        }
        return Base64.getDecoder().decode(base64String);
    }

    /**
     * 判断url是否是http资源
     */
    public static boolean isHttpUrl(URL url) {
        return url.getProtocol().toLowerCase().startsWith("file") || url.getProtocol().toLowerCase().startsWith("http");
    }

    /**
     * 判断url是否是ftp资源
     */
    public static boolean isFtpUrl(URL url) {
        return "ftp".equalsIgnoreCase(url.getProtocol());
    }
}