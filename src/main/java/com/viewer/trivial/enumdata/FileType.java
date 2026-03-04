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
    pdf("pdfFileConverter", "pdf"),
    doc("wordFileConverter", "doc"),
    docx("wordFileConverter", "docx"),
    xls("excelFileConverter", "excel"),
    xlsx("excelFileConverter", "excel"),
    ppt("pptFileConverter", "ppt"),
    pptx("pptxFileConverter", "pptx"),
    zip("zipFileConverter", "zip"),
    rar("zipFileConverter", "zip"),
    sevenZ("zipFileConverter", "zip"),
    dwg("cadFileConverter", "cad"),
    dxf("cadFileConverter", "cad"),
    drawio("drawioFileConverter", "drawio"),
    md("markdownFileConverter", "markdown"),
    jpg("imageFileConverter", "image"),
    jpeg("imageFileConverter", "image"),
    png("imageFileConverter", "image"),
    gif("imageFileConverter", "image"),
    bmp("imageFileConverter", "image"),
    error("error", "err")
    ;

    private final String converter;
    private final String page;
    FileType(String converter, String page) {
        this.converter = converter;
        this.page = page;
    }

    public String getConverter() {
        return converter;
    }

    public String getPage() {
        return page;
    }

    private static final Map<String, FileType> FILE_TYPE_MAPPER = new HashMap<>();

    static {
        Arrays.stream(FileType.values()).forEach(fileType -> FILE_TYPE_MAPPER.put(fileType.name().toLowerCase(), fileType));
    }

    public static FileType getFileType(String fileType){
        if (StringUtils.hasText(fileType)) {
            String lowerCase = fileType.toLowerCase();
            if ("7z".equals(lowerCase)) {
                return sevenZ;
            }
            return FILE_TYPE_MAPPER.get(lowerCase);
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