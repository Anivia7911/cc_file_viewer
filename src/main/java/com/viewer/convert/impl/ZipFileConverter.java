package com.viewer.convert.impl;

import com.viewer.convert.AbstractFileConverter;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.enumdata.FileType;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 压缩包预览转换器（解析目录树）
 * @author hcw
 */
@Service
public class ZipFileConverter extends AbstractFileConverter {

    @Override
    protected void convertFileHandle(FileAttributeModel model) {
        File zipFile = new File(model.getFilePath());
        if (!zipFile.exists()) {
            model.setConvertedFileType(FileType.error);
            return;
        }

        List<String> fileEntries = new ArrayList<>();
        // 尝试使用 UTF-8 编码读取，如果不成功则可能需要 GBK
        try (ZipFile zip = new ZipFile(zipFile, "UTF-8")) {
            Enumeration<ZipArchiveEntry> entries = zip.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                fileEntries.add(entry.getName());
            }
            // 将文件结构列表存入 model，前端解析渲染
            model.setResultData(fileEntries);
            model.setConvertedFileType(FileType.zip);
            // 路径设为原路径，但在模板中通过 resultData 展示
            model.setConvertedFilePath(model.getFileUrl());
        } catch (Exception e) {
            System.err.println("解析压缩包失败: " + e.getMessage());
            e.printStackTrace();
            // 如果 UTF-8 失败，尝试系统默认编码或 GBK (针对 Windows 压缩包)
            try (ZipFile zip = new ZipFile(zipFile, "GBK")) {
                fileEntries.clear();
                Enumeration<ZipArchiveEntry> entries = zip.getEntries();
                while (entries.hasMoreElements()) {
                    ZipArchiveEntry entry = entries.nextElement();
                    fileEntries.add(entry.getName());
                }
                model.setResultData(fileEntries);
                model.setConvertedFileType(FileType.zip);
                model.setConvertedFilePath(model.getFileUrl());
            } catch (Exception e2) {
                model.setConvertedFileType(FileType.error);
            }
        }
    }
}
