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

/**
 * 图片文件预览（直接展示）
 * @author hcw
 */
@Service
public class ImageFileConverter extends AbstractFileConverter {

    @Override
    protected void convertFileHandle(FileAttributeModel model) {
        try {
            System.out.println("ImageFileConverter 正在处理: " + model.getFilePath());
            File file = new File(model.getFilePath());
            if (!file.exists()) {
                System.err.println("错误: 待预览的图片文件不存在: " + model.getFilePath());
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
            String uniqueFilename = model.getFileName();
            Path targetPath = tempDir.resolve(uniqueFilename);

            // 将图片复制到可供 Web 访问的 temp 目录
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("图片复制完成: " + targetPath.toAbsolutePath());

            // 设置预览路径
            model.setConvertedFilePath("/file/temp/" + model.getUuid() + "/" + uniqueFilename);
            model.setConvertedFileType(FileType.getFileType(model.getFileType().name())); // 重新通过 getFileType 获取确保一致性

        } catch (Exception e) {
            System.err.println("ImageFileConverter 转换异常: " + e.getMessage());
            e.printStackTrace();
            model.setConvertedFileType(FileType.error);
        }
    }
}
