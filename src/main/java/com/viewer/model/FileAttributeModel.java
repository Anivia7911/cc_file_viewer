package com.viewer.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 文件属性
 * @author: Jie Bugui
 * @create: 2025-06-22 20:27
 */
@Data
public class FileAttributeModel implements Serializable {

    /**
     * 源文件名
     */
    public String fileName = "xxx.pdf";

    /**
     * 源文件类型
     */
    public String fileType = "pdf";

    /**
     * 源文件url
     */
    public String fileUrl;

    /**
     * 文件大小,以byte为单位
     */
    public long fileSize;

    /**
     * 临时文件目录,用于存放临时文件
     */
    private String tempDir;

    /**
     * 转换后的文件路径
     */
    private String convertedFilePath;
}
