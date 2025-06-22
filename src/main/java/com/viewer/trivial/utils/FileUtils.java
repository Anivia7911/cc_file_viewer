package com.viewer.trivial.utils;

/**
 * @description: TODO
 * @author: Jie Bugui
 * @create: 2025-06-22 20:43
 */
public class FileUtils {

    /**
     * 获取文件后缀
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
