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

    public String filePreview(HttpServletRequest req, String fileUrl, Model model) {
        FileAttributeModel fileAttribute = FileUtils.getFileAttribute(req, fileUrl);
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