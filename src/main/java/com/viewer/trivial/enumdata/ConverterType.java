package com.viewer.trivial.enumdata;

/**
 * @author hcw
 * @date 2025/8/11 10:47:37
 */
public enum ConverterType {
    Default("default", "默认"),
    LibreOffice("libreOffice", "LibreOffice转换"),
    Aspose("aspose", "Aspose转换"),

    ;

    private final String type;
    private final String desc;
    ConverterType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
