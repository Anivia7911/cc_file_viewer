package com.viewer.trivial.enumdata;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: TODO
 * @author: Jie Bugui
 * @create: 2025-06-22 20:39
 */
public enum FileType {
    pdf,
    doc,
    docx,
    xls,
    xlsx,
    ppt,
    pptx
    ;

    private static final Map<String, FileType> FILE_TYPE_MAPPER = new HashMap<>();

    static {
        Arrays.stream(FileType.values()).forEach(fileType -> FILE_TYPE_MAPPER.put(fileType.name().toLowerCase(), fileType));
    }

    public static FileType getFileType(String fileType){
        if (StringUtils.hasText(fileType)) {
            return FILE_TYPE_MAPPER.get(fileType.toLowerCase());
        }
        return null;
    }

    public static boolean validType(String fileType){
        for (FileType value : FileType.values()) {
            if (value.name().equals(fileType)){
                return true;
            }
        }
        return false;
    }
}