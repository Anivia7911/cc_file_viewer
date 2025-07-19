package com.viewer.convert.impl;

import com.viewer.convert.FileConverter;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.FileViewerConst;
import com.viewer.trivial.enumdata.FileType;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * @author hcw
 * @date 2025/7/7 16:45:15
 */
@Component("Default")
public class DefFileConverter implements FileConverter {
    @Override
    public void convert(FileAttributeModel model) {
        model.setConvertedFileType(FileType.pdf);
        try {
            String filePath = model.getFileUrl();
            if (filePath.startsWith("file:")) {
                filePath = java.net.URLDecoder.decode(filePath.substring(5), StandardCharsets.UTF_8); // 修正substring参数
            }
            File file = new File(filePath);

            if (!file.exists() || !file.isFile()) {
                model.setConvertedFilePath(FileViewerConst.ERROR_PAGE);
                return;
            }
            // 创建临时目录（如果不存在）
            Path tempDir = Files.createDirectories(Paths.get(System.getProperty("java.io.tmpdir"), "pdfviewer"));

            // 生成唯一文件名，避免冲突
            String uniqueFilename = UUID.randomUUID() + "_" + file.getName();
            Path targetPath = tempDir.resolve(uniqueFilename);

            // 复制文件到临时目录
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 设置转换后的文件路径
            model.setConvertedFilePath("/pdfviewer/" + uniqueFilename); // 使用相对路径
        } catch (Exception e) {
            e.printStackTrace();
            model.setConvertedFilePath(FileViewerConst.ERROR_PAGE);
        }
    }
}