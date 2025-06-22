package com.viewer.service;

import com.viewer.convert.FileConverter;
import com.viewer.convert.FileConverterFactory;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.enumdata.FileType;
import com.viewer.trivial.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.File;

/**
 * @description: 业务层
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

    public String getFileViewerUrl(String fileUrl, String fullFileName, String converterType, Model model) {
        FileAttributeModel fileModel = new FileAttributeModel();
        fileModel.setFileName(fullFileName);
        fileModel.setFileUrl(fileUrl);
        String fileType = FileUtils.getFileSuffix(fileModel.getFileName());
        if (!FileType.validType(fileType)) {
            return "err";
        }
        fileModel.setFileType(fileType);
        FileConverter fileConverter = converterFactory.getFileConverter(converterType);
        if (fileConverter == null) {
            return "err";
        }
        fileConverter.convert(fileModel);
        return convertPreviewUrl(fileModel, model);
    }

    private String convertPreviewUrl(FileAttributeModel fileModel, Model model){
        File file = new File(fileModel.getConvertedFilePath());
        if (!file.exists() || !file.isFile()) {
            return "err";
        }

        return null;
    }
}
