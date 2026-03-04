package com.viewer.convert;

import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.FileViewerConst;
import com.viewer.trivial.cache.FileConvertCache;
import com.viewer.trivial.enumdata.FileType;
import com.viewer.trivial.utils.FileUtils;
import com.viewer.trivial.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hcw
 * @date 2025/7/21 11:14:54
 */
public abstract class AbstractFileConverter {
    private FileConvertCache fileConvertCache;

    @Autowired
    void setService(FileConvertCache fileConvertCache) {
        this.fileConvertCache = fileConvertCache;
    }

    public void convert(FileAttributeModel model) {
        try {
            System.out.println("开始处理文件: " + model.getFileName() + ", URL: " + model.getFileUrl());
            cleanupIfExceeds(Paths.get("file/temp"), 100 * 1024 * 1024);
            
            // 查询缓存 (只有非压缩包子文件预览时才查缓存，或者缓存 key 包含 zipEntry)
            String cacheKey = model.getFileUrl() + (StringUtils.isNotBlank(model.getZipEntry()) ? "@" + model.getZipEntry() : "");
            final FileAttributeModel cacheModel = fileConvertCache.getIfPresent(cacheKey);
            if (cacheModel != null && StringUtils.isNotBlank(cacheModel.getConvertedFilePath())) {
                System.out.println("命中缓存: " + cacheModel.getConvertedFilePath());
                BeanUtils.copyProperties(cacheModel, model);
                return;
            }
            //下载文件
            System.out.println("正在下载文件...");
            FileUtils.downLoadFile(model);
            System.out.println("下载完成，本地路径: " + model.getFilePath());

            // 如果是预览压缩包内的文件，先提取出来
            if (StringUtils.isNotBlank(model.getZipEntry())) {
                extractZipEntry(model);
            }

            //文件转换
            System.out.println("正在进行转换处理 [" + this.getClass().getSimpleName() + "]...");
            convertFileHandle(model);
            System.out.println("转换处理完成，结果状态: " + model.getConvertedFileType());
            //存入缓存
            fileConvertCache.put(cacheKey, model);
        } catch (Exception e) {
            System.err.println("文件处理过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            model.setConvertedFilePath(FileViewerConst.ERROR_PAGE);
            model.setConvertedFileType(com.viewer.trivial.enumdata.FileType.error);
        }
    }

    protected abstract void convertFileHandle(FileAttributeModel model);

    /**
     * 从压缩包中提取指定文件并更新 model 的 filePath
     */
    private void extractZipEntry(FileAttributeModel model) throws IOException {
        String zipPath = model.getFilePath();
        String entryName = model.getZipEntry();
        System.out.println("准备从压缩包提取文件: " + entryName);

        Path zipFile = Paths.get(zipPath);
        Path targetDir = zipFile.getParent().resolve("extracted_" + model.getUuid());
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        boolean extracted = false;
        String[] encodings = {"UTF-8", "GBK"};

        for (String encoding : encodings) {
            try (org.apache.commons.compress.archivers.zip.ZipFile zip = new org.apache.commons.compress.archivers.zip.ZipFile(zipFile.toFile(), encoding)) {
                org.apache.commons.compress.archivers.zip.ZipArchiveEntry entry = zip.getEntry(entryName);
                if (entry != null) {
                    Path targetPath = targetDir.resolve(model.getFileName());
                    try (java.io.InputStream is = zip.getInputStream(entry)) {
                        Files.copy(is, targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                    model.setFilePath(targetPath.toString());
                    System.out.println("提取成功 [" + encoding + "], 子文件路径: " + model.getFilePath());
                    extracted = true;
                    break;
                }
            } catch (Exception e) {
                System.out.println("尝试使用 " + encoding + " 解析压缩包子文件失败，准备切换编码...");
            }
        }

        if (!extracted) {
            throw new IOException("压缩包内未找到文件或编码不支持: " + entryName);
        }
    }


    /**
     * 清理临时目录，保持目录大小不超过指定大小
     */
    private void cleanupIfExceeds(Path tempDir, long maxSizeInBytes) {
        try {
            if (!Files.exists(tempDir) || !Files.isDirectory(tempDir)) {
                return;
            }

            // 获取目录总大小
            AtomicLong totalSize = new AtomicLong(getTotalSize(tempDir));

            if (totalSize.get() > maxSizeInBytes) {
                // 删除缓存
                fileConvertCache.invalidateAll();
                // 获取所有文件，按最后修改时间升序排序（旧文件在前）
                Files.list(tempDir)
                        .filter(path -> !Files.isDirectory(path))
                        .sorted((a, b) -> {
                            try {
                                return Files.getLastModifiedTime(a).compareTo(Files.getLastModifiedTime(b));
                            } catch (IOException e) {
                                return 0;
                            }
                        })
                        .forEach(path -> {
                            try {
                                long fileSize = Files.size(path);
                                String filename = path.getFileName().toString();
                                Files.delete(path);
                                System.out.println("已删除临时文件: " + filename);
                                // 删除足够多的文件，直到空间释放
                                totalSize.addAndGet(-fileSize);
                                if (totalSize.get() <= maxSizeInBytes) {
                                    return;
                                }
                            } catch (IOException e) {
                                System.err.println("无法删除文件: " + path + "，原因: " + e.getMessage());
                            }
                        });
            }

        } catch (Exception e) {
            System.err.println("清理临时目录失败");
            e.printStackTrace();
        }
    }

    /**
     * 计算目录总大小
     */
    private long getTotalSize(Path dir) throws IOException {
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            return 0;
        }

        return Files.walk(dir)
                .filter(path -> !Files.isDirectory(path))
                .mapToLong(path -> {
                    try {
                        return Files.size(path);
                    } catch (IOException e) {
                        return 0;
                    }
                })
                .sum();
    }

}
