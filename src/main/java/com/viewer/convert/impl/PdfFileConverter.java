package com.viewer.convert.impl;

import com.viewer.convert.AbstractFileConverter;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.enumdata.FileType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * @author hcw
 * @date 2025/7/21 13:59:13
 */
@Service()
public class PdfFileConverter extends AbstractFileConverter {
    @Override
    protected void convertFileHandle(FileAttributeModel model) {
        try {
            File file = new File(model.getFilePath());

            if (!file.exists() || !file.isFile()) {
                model.setConvertedFileType(FileType.error);
                return;
            }
            // 获取项目资源目录下的 static/temp 路径
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path tempDir = projectRoot.resolve("file/temp/" + model.getUuid());

            // 创建目录（如果不存在）
            if (!Files.exists(tempDir)) {
                Files.createDirectories(tempDir);
            }

            // 生成唯一文件名，避免冲突
            String uniqueFilename = file.getName();
            Path targetPath = tempDir.resolve(uniqueFilename);

            // 复制文件到临时目录
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 设置转换后的文件路径
            model.setConvertedFilePath("/file/temp/"  + model.getUuid() + "/" + uniqueFilename); // 使用相对路径
            model.setConvertedFileType(FileType.pdf); // 使用相对路径
        } catch (Exception e) {
            e.printStackTrace();
            model.setConvertedFileType(FileType.error);
        }
    }
}
