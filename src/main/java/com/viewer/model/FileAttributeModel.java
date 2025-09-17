package com.viewer.model;

import com.viewer.trivial.enumdata.ConverterType;
import com.viewer.trivial.enumdata.FileType;
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
    public String fileName;

    /**
     * 源文件类型
     */
    public FileType fileType;

    /**
     * 源文件url
     */
    public String fileUrl;

    /**
     * 文件大小,以byte为单位
     */
    public long fileSize;

    /**
     * UUID，文件标识
     */
    public String uuid;

    /**
     * 下载后的文件路径
     */
    private String filePath;

    /**
     * 是否需要转换文件
     */
    private boolean needConvert;

    /**
     * 转换方式
     */
    private ConverterType converterType = ConverterType.Default;

    /**
     * 转换后的文件路径
     */
    private String convertedFilePath;

    /**
     * 转换后的文件名
     */
    private String convertedFileName;

    /**
     * 转换后文件类型
     */
    private FileType convertedFileType;
}