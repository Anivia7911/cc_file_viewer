package com.viewer.service;

import com.viewer.convert.FileConverterFactory;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.FileViewerConst;
import com.viewer.trivial.enumdata.FileType;
import com.viewer.trivial.utils.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * @author: Jie Bugui
 * @create: 2025-06-22 20:31
 */
@Service
public class FileViewerService {

    private FileConverterFactory converterFactory;

    @Autowired
    void setService(
            FileConverterFactory converterFactory
    ) {
        this.converterFactory = converterFactory;
    }

    public String filePreview(HttpServletRequest req, String fileUrl, String zipEntry, Model model) {
        FileAttributeModel fileAttribute = FileUtils.getFileAttribute(req, fileUrl);
        
        // 处理压缩包内部文件
        if (com.viewer.trivial.utils.StringUtils.isNotBlank(zipEntry)) {
            fileAttribute.setZipEntry(zipEntry);
            // 将文件名和类型修正为子文件
            String fileName = zipEntry.substring(zipEntry.lastIndexOf("/") + 1);
            fileAttribute.setFileName(fileName);
            fileAttribute.setFileType(FileUtils.typeFromFileName(fileName));
            
            // 重要：重置可能存在的压缩包转换信息，确保子文件重新转换
            fileAttribute.setConvertedFilePath(null);
            fileAttribute.setConvertedFileType(null);
            fileAttribute.setResultData(null);
        }

        model.addAttribute("fileAttribute", fileAttribute);
        var fileConverter = converterFactory.getFileConverter(fileAttribute.getFileType().getConverter());
        if (fileConverter == null) {
            return FileViewerConst.ERROR_PAGE;
        }
        //文件转换处理
        fileConverter.convert(fileAttribute);
        return fileAttribute.getConvertedFileType().getPage();
    }
}