package com.viewer.convert;

import com.viewer.model.FileAttributeModel;
import com.viewer.trivial.FileViewerConst;
import com.viewer.trivial.cache.FileConvertCache;
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
            cleanupIfExceeds(Paths.get("file/temp"), 1 * 1024 * 1024);
            //查询缓存
            final FileAttributeModel cacheModel = fileConvertCache.getIfPresent(model.getFileUrl());
            if (cacheModel != null && StringUtils.isNotBlank(cacheModel.getConvertedFilePath())) {
                BeanUtils.copyProperties(cacheModel, model);
                return;
            }
            //下载文件
            FileUtils.downLoadFile(model);
            //文件转换
            convertFileHandle(model);
            //存入缓存
            fileConvertCache.put(model.getFileUrl(), model);
        } catch (Exception e) {
            e.printStackTrace();
            model.setConvertedFilePath(FileViewerConst.ERROR_PAGE);
        }
    }

    protected abstract void convertFileHandle(FileAttributeModel model);


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
