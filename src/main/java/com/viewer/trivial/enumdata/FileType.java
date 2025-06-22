package com.viewer.trivial.enumdata;

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

    public static boolean validType(String fileType){
        for (FileType value : FileType.values()) {
            if (value.name().equals(fileType)){
                return true;
            }
        }
        return false;
    }
}
