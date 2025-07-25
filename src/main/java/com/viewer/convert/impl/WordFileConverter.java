package com.viewer.convert.impl;

import com.viewer.convert.AbstractFileConverter;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.enumdata.FileType;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.InstalledOfficeManagerHolder;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * word文件转换器
 *
 * @author hcw
 * @date 2025/7/25 10:47:42
 */
@Service
public class WordFileConverter extends AbstractFileConverter {

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
            String uniqueFilename = file.getName().replaceAll("\\.(doc|docx)$", ".pdf");
            Path targetPath = tempDir.resolve(uniqueFilename);



            // 2. 创建转换器
            DocumentConverter converter = LocalConverter.make(InstalledOfficeManagerHolder.getInstance());

            // 3. 执行转换
            converter.convert(file)
                    .to(new File(targetPath.toString()))
                    .execute();

            // 转换成功，设置输出文件路径
            model.setConvertedFilePath("/file/temp/" + model.getUuid() + "/" + uniqueFilename);
            model.setConvertedFileType(FileType.pdf);

        } catch (Exception e) {
            e.printStackTrace();
            model.setConvertedFileType(FileType.error);
        }
    }

}
