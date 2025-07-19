package com.viewer.convert;

import com.viewer.convert.impl.DefFileConverter;
import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.cache.FileConvertCache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @description: 文件转换器
 * @author: Jie Bugui
 * @create: 2025-06-22 20:49
 */
public interface FileConverter {

    FileConvertCache cache = null;

    default void convertFile(FileAttributeModel model){
        cleanupIfExceeds(Paths.get("temp"), 1);
        convert(model);
    }

    void convert(FileAttributeModel model);
    private void cleanupIfExceeds(Path tempDir, long maxSizeInBytes) {
        try {
            if (!Files.exists(tempDir) || !Files.isDirectory(tempDir)) {
                return;
            }

            // 获取目录总大小
            AtomicLong totalSize = new AtomicLong(getTotalSize(tempDir));

            if (totalSize.get() > maxSizeInBytes) {
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

                                // 删除缓存
                                if (this instanceof DefFileConverter defFileConverter) {
                                    if (cache != null) {
                                        cache.removeByConvertedFilename(filename);
                                    }
                                }

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
